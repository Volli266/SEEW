package groups

import org.json.JSONArray
import org.json.JSONObject

import dino.Dino

import SeewService
import estimation.findConsensus
import kotlin.concurrent.scheduleAtFixedRate

import structure.*
import Error
import cocomo.developerImprovement
import cocomo.finishTask
import cocomo.startTask
import kotlin.random.Random
import connection.SocketMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * "Group" ist die Klasse, die die Daten einer Gruppe von Spielern (User) hält.
 * Der Konstruktor wird von SeewService.createGroup(user: User, params: JSONObject)
 * aufgerufen. Dort sind auch die logischen Bedingungen (wie maximale Gruppengröße)
 * implementiert.
 *
 * @see SeewService#createGroup(user: User, params: JSONObject)
 */
class Group(
    val name: String,
    private val color: String,
    private val structure: Structure) {

    companion object {
        const val SPRINT_BACKLOG_PHASE = 1
        const val SPRINT_ESTIMATION_PHASE = 2
        const val SPRINT_PROGRESS_PHASE = 3
        const val SPRINT_REVIEW_PHASE = 4
        const val SPRINT_RETROSPECTIVE_PHASE = 5
        const val SPRINT_TIME = 240 // seconds
        const val SPRINT_DAILY_TIME = 30 // seconds
        const val SIMULATION_TIME = 1800 // seconds
        const val TIME_PER_IN_GAME_HOUR = 30 // seconds
    }

    private val dino: Dino = Dino(name, color)
    private val members: MutableList<User> = mutableListOf()

    /**
     * Ausganssituation:
     * Methode, zur Verteilung der Werte die den Entwickler später zugewiesen werden. Jedem Entwickler sollen im Schnitt
     * 50 Punkte zugeteilt werden. 50 * 2 = 100 da jeder Spieler zwei Entwickler hat. Es soll aber nur auf das Team
     * gesehen einen Durchschnitt von 50 je Entwickler erreicht werden. Um ein realistischeres Szenario zu liefern,
     * wird mit dieser Methode so verteilt, dass auch im Schnitt schlechtere und bessere Entwickler entstehen
     *
     * Für Gruppen mit 3 Studenten werden durchschnittliche 60 bis 90 Punkte je Entwickler vergeben
     * Für Gruppen mit 4 Mitglieders werden durchschnittlich 20 bis 50 Punkte je Entwickler vergeben
     */
    fun calculateStartValues() {
        val randomValues = IntArray(members.size*2, {Random.nextInt(4, 7)})

        val factor: Int = (members.size * 100) / randomValues.sum()
        var index = 0
        var balance = 0;
        for (user in members) {
            if ( members.size  == 3) {
                balance = 20
            } else if (members.size == 4){
                balance = -20
            }
            user.developerOne.values = randomValues[index++]*factor + balance
            user.developerTwo.values = randomValues[index++]*factor + balance
            user.developerOne.initialize()
            user.developerTwo.initialize()
        }
    }

    private var startedSimulation: Boolean = false

    private var currentPhase: Int = 0
    private var currentSprint: Int = 0
    private var currentEpic: Int = 0
    private var currentStory: Int = 0
    private var currentTime: Int = SIMULATION_TIME
    private var currentSprintTime: Int = SPRINT_TIME
    private var currentTasks: MutableList<Task> = mutableListOf()

    private var timer: Timer = Timer()
    private var sprintProgressTimer: Timer = Timer()

    private var reviewDone: Boolean = false
    private var storyOneEstimated: Boolean = false
    private var storyTwoEstimated: Boolean = false

    private var dailyStatus: Int = 0
    private var dailyTime: Int = SPRINT_DAILY_TIME

    private var evaluationDailyDone: MutableList<Boolean> = mutableListOf(false, false, false, false)
    private var evaluationSprintTime: MutableList<Int> = mutableListOf(0, 0, 0, 0)

    /**
     * Methode, die die aktuelle Größe der Gruppe zurückgibt
     *
     * @return Größe der Gruppe als Int-Wert
     */
    fun getSize(): Int = this.members.size

    /**
     * Methode, um einer Gruppe ein Mitglied (User) hinzuzufügen
     *
     * @param member User, der hinzugefügt wird
     */
    fun addMember(member: User) = members.add(member)

    /**
     * Methode, um ein Mitglied (User) einer Gruppe zu löschen.
     *
     * @param member User, der entfernt wird
     */
    fun removeMember(member: User) = members.remove(member)

    /**
     * Methode die zurückgibt, ob ein User Mitglied der Gruppe ist
     *
     * @param user User, dessen Mitgliedschaft geprüft wird
     * @return <code>true</code> falls der User Mitglied ist
     *         <code>false</code> sonst
     */
    fun contains(user: User): Boolean = members.contains(user)

    /**
     * Methode, um allen Mitgliedern einer Gruppe einen Text zu senden
     *
     * @param msg zuübermittelnder Text
     */
    suspend fun send(msg: String) {
        members.forEach { it.send(msg) }
    }

    /**
     * Startet Timer für die Dauer der Simulation
     */
    private suspend fun startTimer() {
        timer.scheduleAtFixedRate(0, 1000L) {
            currentTime--
            if (currentTime <= 0) {
                currentPhase = 6
                this.cancel()
                sprintProgressTimer.cancel()
                GlobalScope.launch {
                    updateAllViews()
                }
            } else {
                GlobalScope.launch {
                    updateTime()
                }
            }
        }
    }

    /**
     * Nutzer bestätigt seine Bereitschaft für die nächste Phase
     * Sobald alle Nutzer bereit sind wird in die nächste Phase
     * übergegangen.
     *
     * @param user Nutzer
     */
    suspend fun confirmPhase(user: User) {
        user.hasConfirmedPhase = true
        if (members.all { it.hasConfirmedPhase }) {
            if (currentPhase == SPRINT_BACKLOG_PHASE) {
                if (structure.getEpic(currentEpic).getDecision().storiesSet()) {
                    currentPhase++
                    evaluationSprintTime[currentSprint] = currentSprintTime
                    RPC.callGroup(JSON.NOTIFY_SPRINT_OVERVIEW, Error.NO_ERROR.toJSON(), this)
                } else {
                    RPC.callGroup(JSON.NOTIFY_SPRINT_OVERVIEW, Error.STORIES_NOT_CHOSEN.toJSON(), this)
                }
            } else if (currentPhase == SPRINT_ESTIMATION_PHASE) {
                if (storyOneEstimated && storyTwoEstimated) {
                    currentPhase++
                    startSprintProgressPhase()
                    storyOneEstimated = false
                    storyTwoEstimated = false
                    RPC.callGroup(JSON.NOTIFY_SPRINT_OVERVIEW, Error.NO_ERROR.toJSON(), this)
                } else {
                    RPC.callGroup(JSON.NOTIFY_SPRINT_OVERVIEW, Error.NOT_ESTIMATED.toJSON(), this)
                }
            } else if (currentPhase == SPRINT_PROGRESS_PHASE) {
                currentPhase++
                sprintProgressTimer.cancel()
                RPC.callGroup(JSON.NOTIFY_SPRINT_OVERVIEW, Error.NO_ERROR.toJSON(), this)
            } else if (currentPhase == SPRINT_REVIEW_PHASE) {
                if (reviewDone) {
                    currentPhase++
                    reviewDone = false
                    currentTasks.removeIf { it.status == TaskStatus.FINISHED }
                    dailyStatus = 0
                    dailyTime = SPRINT_DAILY_TIME
                    RPC.callGroup(JSON.NOTIFY_SPRINT_OVERVIEW, Error.NO_ERROR.toJSON(), this)
                } else {
                    RPC.callGroup(JSON.NOTIFY_SPRINT_OVERVIEW, Error.NOT_REVIEWED.toJSON(), this)
                }
            } else if (currentPhase == SPRINT_RETROSPECTIVE_PHASE) {
                when (currentSprint) {
                    2 -> {
                        if (currentTasks.isNotEmpty()) {
                            currentPhase = SPRINT_PROGRESS_PHASE
                            currentSprint++
                            startSprintProgressPhase()
                        } else {
                            currentPhase = 6
                            timer.cancel()
                        }
                    }
                    3 -> {
                        currentPhase = 6
                        timer.cancel()
                    }
                    else -> {
                        currentPhase = 1
                        currentSprint++
                        currentEpic++
                    }
                }
                RPC.callGroup(JSON.NOTIFY_SPRINT_OVERVIEW, Error.NO_ERROR.toJSON(), this)
            } else {
                currentPhase++
                RPC.callGroup(JSON.NOTIFY_SPRINT_OVERVIEW, Error.NO_ERROR.toJSON(), this)
            }
            members.forEach { it.hasConfirmedPhase = false }
            updateAllViews()
        } else {
            RPC.callUser(JSON.NOTIFY_SPRINT_OVERVIEW, Error.NOT_ALL_CONFIRMED.toJSON(), user)
        }
    }

    /**
     * Startet die Sprintverlaufs-Phase. Dabei werden die Tasks
     * die in diesem Sprint bearbeitet werden sollen zu currentTasks hinzugefügt.
     * Es wird ein Timer für die Dauer des Sprint gestartet.
     */
    private suspend fun startSprintProgressPhase() {
        val storyOneTasks: List<Task>? = structure.getEpic(currentEpic).getDecision().chosenStoryOne?.getSelectedTasks(dino)
        val storyTwoTasks: List<Task>? = structure.getEpic(currentEpic).getDecision().chosenStoryTwo?.getSelectedTasks(dino)
        storyOneTasks?.forEach { it.sprint = currentSprint+1 }
        storyTwoTasks?.forEach { it.sprint = currentSprint+1 }
        storyOneTasks?.let {
            currentTasks.addAll(it)
            it.forEach { t ->
                startTask(t, dino)
            }
        }
        storyTwoTasks?.let {
            currentTasks.addAll(it)
            it.forEach { t ->
                startTask(t, dino)
            }
        }
        sprintProgressTimer = Timer()
        sprintProgressTimer.scheduleAtFixedRate(0, 1000L) {
            currentSprintTime--
            if (currentSprintTime <= 0) {
                currentPhase++
                this.cancel()
                GlobalScope.launch {
                    updateAllViews()
                }
            } else {
                if (dailyStatus == 1) {
                    dailyTime--
                    if (dailyTime <= 0) {
                        dailyStatus = 2
                        currentTasks.forEach { it.daily = true }
                        evaluationDailyDone[currentSprint] = true
                    }
                } else {
                    currentTasks.forEach { it.update() }
                }
                GlobalScope.launch {
                    updateSprintProgressView()
                }
            }
        }
    }

    // Updating Views /////////////////////////////////////

    /**
     * Aktualisierung für alle Vue-Komponenten
     */
    private suspend fun updateAllViews() {
        updateDinoView()
        updateDeveloperView()
        updateProductBacklogView()
        updateSprintBacklogView()
        updateEstimationView()
        updateSprintProgressView()
        updateTime()
        updateSprintOverview()
        updateSprintReview()
    }

    /**
     * Aktualisierung für Dino-Komponente
     */
    private suspend fun updateDinoView() {
        RPC.callGroup(JSON.UPDATE_DINO, dino.toJSON(), this)
    }

    /**
     * Aktualisierung für Entwickler-Komponente
     */
    private suspend fun updateDeveloperView() {
        members.forEach {
            val json = JSONObject()
            json.put(JSON.DEVELOPER_ONE, it.developerOne.toJSON())
            json.put(JSON.DEVELOPER_TWO, it.developerTwo.toJSON())
            RPC.callUser(JSON.UPDATE_DEVELOPERS, json, it)
        }
    }

    /**
     * Aktualisierung für Übersicht-Komponente
     */
    private suspend fun updateSprintOverview() {
        val json = JSONObject()
        json.put(JSON.CURRENT_PHASE, currentPhase)
        RPC.callGroup(JSON.UPDATE_SPRINT_OVERVIEW, json, this)
    }

    /**
     * Aktualisierung für Product-Backlog-Komponente
     */
    private suspend fun updateProductBacklogView() {
        val json = JSONObject()
        json.put(JSON.EPIC_ORDER, structure.epicOrder.fold(JSONArray()) { acc, i ->
            acc.put(i)
        })
        RPC.callGroup(JSON.UPDATE_PRODUCT_BACKLOG, json, this)
    }

    /**
     * Aktualisierung für Sprint-Backlog-Komponente
     */
    private suspend fun updateSprintBacklogView() {
        val json = JSONObject()
        json.put(JSON.SPRINT_TIME, currentSprintTime)
        json.put(JSON.EPIC, structure.getEpic(currentEpic).toJSON(dino))
        RPC.callGroup(JSON.UPDATE_SPRINT_BACKLOG, json, this)
    }

    /**
     * Aktualisierung für Schätz-Komponente
     */
    private suspend fun updateEstimationView() {
        val json = JSONObject()
        json.put(JSON.ESTIMATION_STORY, currentStory)
        json.put(JSON.CHOSEN_STORY_ONE, structure.getEpic(currentEpic).getDecision().chosenStoryOne?.toJSON(dino))
        json.put(JSON.CHOSEN_STORY_TWO, structure.getEpic(currentEpic).getDecision().chosenStoryTwo?.toJSON(dino))
        RPC.callGroup(JSON.UPDATE_ESTIMATION, json, this)
    }

    /**
     * Aktualisierung für Sprintverlauf-Komponente
     */
    private suspend fun updateSprintProgressView() {
        val json = JSONObject()
        json.put(JSON.CURRENT_TASKS, currentTasks.fold(JSONArray()) { acc, task -> acc.put(task.toJSON()) })
        json.put(JSON.CURRENT_SPRINT, currentSprint)
        json.put(JSON.CURRENT_SPRINT_TIME, currentSprintTime)
        json.put(JSON.SPRINT_DAILY_TIME, dailyTime)
        json.put(JSON.SPRINT_DAILY_STATUS, dailyStatus)
        members.forEach {
            json.put(JSON.DEVELOPER_ONE, it.developerOne.toJSON())
            json.put(JSON.DEVELOPER_TWO, it.developerTwo.toJSON())
            RPC.callUser(JSON.UPDATE_SPRINT_PROGRESS, json, it)
        }
    }

    /**
     * Aktualisierung für Review-Komponente
     */
    private suspend fun updateSprintReview() {
        val json = JSONObject()
        json.put(JSON.CURRENT_TASKS, currentTasks.fold(JSONArray()) { acc, task -> acc.put(task.toJSON()) })
        RPC.callGroup(JSON.UPDATE_SPRINT_REVIEW, json, this)
    }

    /**
     * Aktualisierung für Retrospektive-Komponente
     */
    private suspend fun updateSprintRetro() {
        RPC.callGroup(JSON.UPDATE_SPRINT_RETRO, JSONObject(), this)
    }

    // todo: Richtige Auswertung ob der Gorilla besiegt wurde (Modell ist sehr kompliziert)
    /**
     * Auswerung ob der Dino den Gorilla besiegt hat
     */
    private fun evaluationSuccess(): Boolean {
        return dino.social + dino.logic + dino.courage + dino.reaction + dino.agility +
                dino.dexterity + dino.strength + dino.weight + dino.size >= 45
    }

    /**
     * Aktualisierung für Auswertungs-Komponente
     */
    private suspend fun updateEvaluation() {
        val json = JSONObject()
        json.put(JSON.EPICS, structure.epics.fold(JSONArray()){ acc, t ->  acc.put(t.toJSONForEvaluation(dino))})
        json.put(JSON.SUCCESS, evaluationSuccess())
        json.put(JSON.LEFTOVER_TASKS, currentTasks.fold(JSONArray()) { acc, task ->
            acc.put(task.toJSON())
        })
        json.put(JSON.DAILY_DONE_EVAL, evaluationDailyDone)
        json.put(JSON.SPRINT_TIME_EVAL, evaluationSprintTime)
        RPC.callGroup(JSON.UPDATE_EVALUATION, json, this)
    }

    /**
     * Aktualisierung für Time-Komponente
     */
    private suspend fun updateTime() {
        val json = JSONObject()
        json.put(JSON.CURRENT_TIME, currentTime)
        RPC.callGroup(JSON.UPDATE_TIME, json, this)
    }

    /**
     * Wird vom Nutzer aufgerufen sobald er bereit für die Simulation ist.
     */
    suspend fun readyForSimulation(user: User) {
        user.readyForSimulation = true
        if (membersReadyForSimulation()) {
            RPC.callGroup(JSON.SHOW_SIMULATION, JSONObject(), this)
            startTimer()
        } else {
            val msg = RPC.makeMessage(JSON.NOTIFY_INTRO, Error.NOT_READY.toJSON())
            user.connection.sendMessage(SocketMessage.Text(msg, user.connection))
        }
    }

    // Requesting Update //////////////////////////////////

    /**
     * Anforderung eines Updates für alle Vue-Komponenten
     */
    suspend fun requestUpdateAll() {
        updateAllViews()
    }

    /**
     * Anforderung eines Updates der Dino-Komponente
     */
    suspend fun requestUpdateDino() {
        updateDinoView()
    }

    /**
     * Anforderung eines Updates der Entwickler-Komponente
     */
    suspend fun requestUpdateDevelopers() {
        updateDeveloperView()
    }

    /**
     * Anforderung eines Updates der Übersicht-Komponente
     */
    suspend fun requestUpdateSprintOverview() {
        updateSprintOverview()
    }

    /**
     * Anforderung eines Updates der Product-Backlog-Komponente
     */
    suspend fun requestUpdateProductBacklog() {
        updateProductBacklogView()
    }

    /**
     * Anforderung eines Updates der Sprint-Backlog-Komponente
     */
    suspend fun requestUpdateSprintBacklog() {
        updateSprintBacklogView()
    }

    /**
     * Anforderung eines Updates der Schätz-Komponente
     */
    suspend fun requestUpdateEstimation() {
        updateEstimationView()
    }

    /**
     * Anforderung eines Updates der Sprintverlauf-Komponente
     */
    suspend fun requestUpdateSprintProgress() {
        updateSprintProgressView()
    }

    /**
     * Anforderung eines Updates der Review-Komponente
     */
    suspend fun requestUpdateSprintReview() {
        updateSprintReview()
    }

    /**
     * Anforderung eines Updates der Retrospektive-Komponente
     */
    suspend fun requestUpdateSprintRetro() {
        updateSprintRetro()
    }

    /**
     * Anforderung eines Updates der Auswertungs-Komponente
     */
    suspend fun requestUpdateEvaluation() {
        updateEvaluation()
    }


    // Altering the structure /////////////////////////////

    /**
     * Parst die Nummer der Entscheidungsmöglichkeit aus JSON-Objekt
     *
     * @param json JSON-Objekt
     */
    private fun parseDecisionId(json: JSONObject): Int {
        return json.getInt(JSON.DECISION_ID)
    }

    /**
     * Parst die Nummer der Story aus JSON-Objekt
     *
     * @param json JSON-Objekt
     */
    private fun parseStoryId(json: JSONObject): Int {
        return when (json.length() > 0) {
            true ->  json.getInt(JSON.STORY_ID)
            false ->  -1
        }
    }

    /**
     * Parst die Nummer der Task aus JSON-Objekt
     *
     * @param json JSON-Objekt
     */
    private fun parseTaskId(json: JSONObject): Int {
        return json.getInt(JSON.TASK_ID)
    }

    /**
     * Sucht Task mit angegebener Nummer aus der Liste der aktuellen Tasks heraus.
     *
     * @param json JSON-Objekt
     */
    private fun getCurrentTask(json: JSONObject): Task? {
        return currentTasks.find { it.id == parseTaskId(json) }
    }

    /**
     * Parst die Priorität des Epics
     *
     * @param json JSON-Objekt
     */
    private fun parseEpicPriority(json: JSONObject): Int {
        return json.getInt(JSON.EPIC_PRIORITY)
    }

    /**
     * Parst die Nummer des Entwicklers
     *
     * @param json JSON-Objekt
     */
    private fun parseDeveloperId(json: JSONObject): Int {
        return json.getInt(JSON.DEVELOPER_ID)
    }

    /**
     * Setzt die neue Priorität von Epic 1
     *
     * @param params Parameter
     */
    suspend fun setEpicOne(params: JSONObject) {
        structure.changeEpicOrder(0, parseEpicPriority(params))
        updateProductBacklogView()
    }

    /**
     * Setzt die neue Priorität von Epic 2
     *
     * @param params Parameter
     */
    suspend fun setEpicTwo(params: JSONObject) {
        structure.changeEpicOrder(1, parseEpicPriority(params))
        updateProductBacklogView()
    }

    /**
     * Setzt die neue Priorität von Epic 3
     *
     * @param params Parameter
     */
    suspend fun setEpicThree(params: JSONObject) {
        structure.changeEpicOrder(2, parseEpicPriority(params))
        updateProductBacklogView()
    }

    /**
     * Setzt die Entscheidung
     *
     * @param params Parameter
     */
    suspend fun setDecision(params: JSONObject) {
        structure.getEpic(currentEpic).setDecision(parseDecisionId(params))
        updateSprintBacklogView()
    }

    /**
     * Setzt die erste Story für die sich entschieden wurde
     *
     * @param params Parameter
     */
    suspend fun setStoryOne(params: JSONObject) {
        val decision = structure.getEpic(currentEpic).getDecision()
        decision.setStoryOne(parseStoryId(params))
        updateSprintBacklogView()
    }

    /**
     * Setzt die zweite Story für die sich entschieden wurde
     *
     * @param params Parameter
     */
    suspend fun setStoryTwo(params: JSONObject) {
        val decision = structure.getEpic(currentEpic).getDecision()
        decision.setStoryTwo(parseStoryId(params))
        updateSprintBacklogView()
    }

    /**
     * Setzt die Zeit die für den Sprint einkalkuliert wurde
     *
     * @param params Parameter
     */
    suspend fun setSprintTime(params: JSONObject) {
        if (params.has(JSON.SPRINT_TIME)) {
            currentSprintTime = TIME_PER_IN_GAME_HOUR * params.getInt(JSON.SPRINT_TIME)
            updateSprintBacklogView()
        }
    }

    /**
     * Setzt die Schätung für die erste Story
     * Sobald alle Nutzer geschätzt haben wird ein Konsens berechnet
     * und es kann in die nächste Phase übergegangen werden sobald
     * auch die andere Story geschätzt wurde
     *
     * @param user Benutzer der schätzt
     * @param params Parameter
     */
    suspend fun estimateStoryOne(user: User, params: JSONObject) {
        if (params.has(JSON.ESTIMATION_VALUE)) {
            user.estimationValueStoryOne = params.getInt(JSON.ESTIMATION_VALUE)
            user.hasEstimatedStoryOne = true
            if (members.all { it.hasEstimatedStoryOne }) {
                val res = findConsensus(members.map { it.estimationValueStoryOne })
                if (res == -666) {
                    RPC.callGroup(JSON.NOTIFY_SPRINT_OVERVIEW, Error.ESTIMATION_REJECTION.toJSON(), this)
                } else {
                    structure.getEpic(currentEpic).getDecision().chosenStoryOne?.estimation = res
                    storyOneEstimated = true
                }
                members.forEach { it.hasEstimatedStoryOne = false }
            }
            updateEstimationView()
        }
    }

    /**
     * Setzt die Schätung für die zweite Story
     * Sobald alle Nutzer geschätzt haben wird ein Konsens berechnet
     * und es kann in die nächste Phase übergegangen werden sobald
     * auch die andere Story geschätzt wurde
     *
     * @param user Benutzer der schätzt
     * @param params Parameter
     */
    suspend fun estimateStoryTwo(user: User, params: JSONObject) {
        if (params.has(JSON.ESTIMATION_VALUE)) {
            user.estimationValueStoryTwo = params.getInt(JSON.ESTIMATION_VALUE)
            user.hasEstimatedStoryTwo = true
            if (members.all { it.hasEstimatedStoryTwo }) {
                val res = findConsensus(members.map { it.estimationValueStoryTwo })
                if (res == -666) {
                    RPC.callGroup(JSON.NOTIFY_SPRINT_OVERVIEW, Error.ESTIMATION_REJECTION.toJSON(), this)
                } else {
                    structure.getEpic(currentEpic).getDecision().chosenStoryTwo?.estimation = res
                    storyTwoEstimated = true
                }
                members.forEach { it.hasEstimatedStoryTwo = false }
            }
            updateEstimationView()
        }
    }

    /**
     * Startet Daily sobald alle Benutzer das Daily bestätigt haben.
     * Es wird ein Timer für die Dauer des Daily gestartet.
     *
     * @param user Nutzer der das Daily starten will
     */
    suspend fun startSprintDaily(user: User) {
        user.hasConfirmedDaily = true
        if (members.all { it.hasConfirmedDaily }) {
            if (dailyStatus == 0) {
                dailyStatus = 1
                dailyTime = SPRINT_DAILY_TIME
                updateSprintProgressView()
            }
            members.forEach { it.hasConfirmedDaily = false }
        } else {
            RPC.callUser(JSON.NOTIFY_SPRINT_OVERVIEW, Error.DAILY_NOT_CONFIRMED.toJSON(), user)
        }
    }

    /**
     * Setzt den Status des Tasks auf Bereit
     *
     * @param params Parameter
     */
    suspend fun setTaskReady(params: JSONObject) {
        getCurrentTask(params)?.status = TaskStatus.READY
        updateSprintProgressView()
    }

    /**
     * Setzt den Status des Tasks auf in Arbeit
     *
     * @param params Parameter
     */
    suspend fun setTaskInProgress(params: JSONObject) {
        val task = getCurrentTask(params)
        if (task != null && task.hasDeveloper()) {
            task.status = TaskStatus.IN_PROGRESS
            updateSprintProgressView()
            RPC.callGroup(JSON.NOTIFY_SPRINT_OVERVIEW, Error.NO_ERROR.toJSON(), this)
        } else {
            RPC.callGroup(JSON.NOTIFY_SPRINT_OVERVIEW, Error.NO_DEVELOPER.toJSON(), this)
        }
    }

    /**
     * Setzt den ersten Entwickler der den Task bearbeiten soll
     *
     * @param user Nutzer der seinen Entwickler zu Task zuweist
     * @param params Parameter
     */
    suspend fun setTaskDeveloperOne(user: User, params: JSONObject) {
        when (parseDeveloperId(params)) {
            user.developerOne.id -> getCurrentTask(params)?.assignDeveloperOne(user.developerOne, dino)
            user.developerTwo.id -> getCurrentTask(params)?.assignDeveloperOne(user.developerTwo, dino)
        }
        updateSprintProgressView()
    }

    /**
     * Setzt den zweiten Entwickler der den Task bearbeiten soll
     *
     * @param user Nutzer der seinen Entwickler zu Task zuweist
     * @param params Parameter
     */
    suspend fun setTaskDeveloperTwo(user: User, params: JSONObject) {
        when (parseDeveloperId(params)) {
            user.developerOne.id -> getCurrentTask(params)?.assignDeveloperTwo(user.developerOne, dino)
            user.developerTwo.id -> getCurrentTask(params)?.assignDeveloperTwo(user.developerTwo, dino)
        }
        updateSprintProgressView()
    }

    /**
     * Setzt dass für den Task Tests durchgeführt werden sollen
     *
     * @param params Parameter
     */
    suspend fun setTaskTest(params: JSONObject) {
        getCurrentTask(params)?.test = true
        updateSprintProgressView()
    }

    /**
     * Setzt dass für den Task keine Tests durchgeführt werden sollen
     *
     * @param params Parameter
     */
    suspend fun setTaskNotTest(params: JSONObject) {
        getCurrentTask(params)?.test = false
        updateSprintProgressView()
    }

    /**
     * Methode zur Sprint Review. Falls ein Task nicht erfolgreich bearbeitet wurde
     * wird sein Fortschritt halbiert.
     *
     * @param user der User für den Review durchgeführt werden soll
     */
    suspend fun startSprintReview(user: User) {
        user.hasConfirmedReview = true
        if (members.all { it.hasConfirmedReview }) {
            currentTasks.forEach {
                if (it.completion == 100.0) {
                    if (it.success()) {
                        it.status = TaskStatus.FINISHED
                        it.sprint = currentSprint
                        finishTask(it, dino) // improves dino
                        developerImprovement(it)
                    } else {
                        it.completion = 50.0
                        it.tested /= 2
                        it.status = TaskStatus.READY
                    }
                }
            }
            reviewDone = true
            currentSprintTime = SPRINT_TIME
            members.forEach { it.hasConfirmedReview = false }
            updateSprintReview()
        } else {
            RPC.callUser(JSON.NOTIFY_SPRINT_OVERVIEW, Error.NOT_REVIEW_CONFIRMED.toJSON(), user)
        }
    }

    /**
     * Methode, die die Daten einer Gruppe (Gruppenname, -farbe und Nutzernamen
     * der Mitglieder) in ein JSON-Objekt umwandelt.
     *
     * @return JSON-Objekt mit den Gruppendaten
     */
    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put(JSON.GROUP_NAME, name)
        json.put(JSON.GROUP_COLOR, color)
        json.put(JSON.GROUP_MEMBERS, members.fold(JSONArray()) { acc, user ->  acc.put(user.toJSON())})
        return json
    }

    /**
     * @return Ob alle Mitglieder bereit für das Intro sind
     */
    fun membersReadyForIntro() = members.all { it.readyForIntro }

    /**
     * @return Ob alle Mitglieder mit dem Intro fertig und bereit für die Simulation sind
     */
    private fun membersReadyForSimulation() = members.all { it.readyForSimulation }

    fun getStartedSimulation() = startedSimulation

    fun setStartedSimulation() { startedSimulation = !startedSimulation}
}

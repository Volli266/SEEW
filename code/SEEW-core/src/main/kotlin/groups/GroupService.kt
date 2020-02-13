package groups

import connection.SocketMessage
import org.json.JSONArray
import org.json.JSONObject
import RPC
import Error
import kotlinx.coroutines.*
import mapIndices
import mapKeys
import structure.*
import toIntList
import java.io.File

/**
 * Funktionalitäten zur Gruppenverwaltung
 */
object GroupService {

    private val groups: MutableList<Group> = mutableListOf()
    private lateinit var defaultStructure: Structure


    // Grouping ///////////////////////////////////////////

    /**
     * Falls alle Nutzer aus der Gruppe ausgetreten sind wird sie gelöscht.
     *
     * @param group zu überprüfende Gruppe
     */
    private fun removeIfEmpty(group: Group?) {
        if (group?.getSize() == 0) {
            groups.remove(group)
        }
    }

    /**
     * Gruppe wird mit Name und Farbe erstellt. Ersteller wird
     * aus seiner alten Gruppe entfernt und der neuen zugewiesen.
     *
     * @param user Benutzer der die Gruppe erstellt
     * @param params weitere Parameter mit denen die Grupper erstellt wird
     */
    private suspend fun createGroup(user: User, params: JSONObject) {
        if (params.has(JSON.GROUP_NAME) && params.has(JSON.GROUP_COLOR)) {
            val groupName = params.getString(JSON.GROUP_NAME)
            val groupColor = params.getString(JSON.GROUP_COLOR)
            if (groups.all {it.name != groupName }) {
                val group = Group(groupName, groupColor, defaultStructure.copy())
                user.group?.removeMember(user)
                removeIfEmpty(user.group)
                group.addMember(user)
                user.group = group
                groups.add(group)
                updateGroupList()
            } else {
                RPC.callUser(JSON.NOTIFY_GROUP_OVERVIEW, Error.GROUP_NAME_TAKEN.toJSON(), user)
            }
        }
    }

    /**
     * Nutzer kann Gruppe beitreten falls diese weniger als 4 Nutzer hat.
     * Der Nutzer wird dabei aus seiner alten Gruppe, falls er in einer war, entfernt.
     *
     * @param user Benutzer der der Gruppe beitreten will
     * @param params weitere Parameter mit denen die Grupper erstellt wird
     */
    private suspend fun joinGroup(user: User, params: JSONObject) {
        if (params.has(JSON.GROUP_NAME)) {
            val groupName = params.getString(JSON.GROUP_NAME)
            val group = groups.find { it.name == groupName }
            if (group != null && !group.contains(user)) {
                if (group.getSize() < 4) {
                    if (group.getStartedSimulation()) {
                        RPC.callUser(JSON.NOTIFY_GROUP_OVERVIEW, Error.GROUP_STARTED.toJSON(), user)
                    } else {
                        user.group?.removeMember(user)
                        removeIfEmpty(user.group)
                        group.addMember(user)
                        user.group = group
                        updateGroupList()
                    }
                } else {
                    RPC.callUser(JSON.NOTIFY_GROUP_OVERVIEW, Error.GROUP_COMPLETE.toJSON(), user)
                }
            }
        }
    }

    /**
     * Gruppenaustritt eines Nutzers.
     * Falls die Gruppe danach leer ist wird sie entfernt.
     *
     * @param user Nutzer der austreten will
     */
    private suspend fun leaveGroup(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.let {
            it.removeMember(user)
            removeIfEmpty(it)
            user.group = null
            updateGroupList()
        }
    }

    /**
     * Nutzer zeigt mit dem Aufruf dieser Methode an dass er bereit für den Beginn des Intros ist.
     * Mit einer Gruppe mit 3 oder 4 Mitgliedern wird das Intro begonnen sobald alle Nutzer diese
     * Funktion aufgerufen haben.
     *
     * @param user Benutzer der seine Bereitschaft anzeigt
     */
    private suspend fun readyForIntro(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        if (user.group == null) {
            val msg = RPC.makeMessage(JSON.NOTIFY_GROUP_OVERVIEW, Error.NOT_IN_GROUP.toJSON())
            user.connection.sendMessage(SocketMessage.Text(msg, user.connection))
        } else if (user.group!!.getSize() < 3) {
            val msg = RPC.makeMessage(JSON.NOTIFY_GROUP_OVERVIEW, Error.GROUP_INCOMPLETE.toJSON())
            user.connection.sendMessage(SocketMessage.Text(msg, user.connection))
        } else {
            user.readyForIntro = true
            if (user.group!!.membersReadyForIntro()) {
                user.group!!.setStartedSimulation()
                user.group!!.calculateStartValues()
                RPC.callGroup(JSON.SHOW_INTRO, JSONObject(), user.group!!)
            } else {
                val msg = RPC.makeMessage(JSON.NOTIFY_GROUP_OVERVIEW, Error.NOT_READY.toJSON())
                user.connection.sendMessage(SocketMessage.Text(msg, user.connection))
            }
        }
    }

    /**
     * Gruppenliste wird nach einer Änderung aktualisiert
     */
    private suspend fun updateGroupList() {
        val json = JSONObject()
        json.put(JSON.GROUPS, groups.fold(JSONArray()) { acc, group -> acc.put(group.toJSON()) })
        RPC.callAllUsers(JSON.UPDATE_GROUP_LIST, json)
    }

    /**
     * Anforderung einer Übermittlung der Gruppenliste
     */
    private suspend fun requestGroupList(@Suppress("UNUSED_PARAMETER") user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        updateGroupList()
    }


    // Updating ///////////////////////////////////////////

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun readyForSimulation(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.readyForSimulation(user)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun requestUpdateAll(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.requestUpdateAll()
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun requestUpdateDino(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.requestUpdateDino()
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun requestUpdateDevelopers(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.requestUpdateDevelopers()
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun requestUpdateSprintOverview(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.requestUpdateSprintOverview()
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun requestUpdateProductBacklog(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.requestUpdateProductBacklog()
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun requestUpdateSprintBacklog(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.requestUpdateSprintBacklog()
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun requestUpdateEstimation(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.requestUpdateEstimation()
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun requestUpdateSprintProgress(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.requestUpdateSprintProgress()
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun requestUpdateSprintReview(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.requestUpdateSprintReview()
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun requestUpdateSprintRetro(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.requestUpdateSprintRetro()
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun confirmPhase(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.confirmPhase(user)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun requestUpdateEvaluation(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.requestUpdateEvaluation()
    }

    // Altering the structure /////////////////////////////

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun setEpicOne(user: User, params: JSONObject) {
        user.group?.setEpicOne(params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun setEpicTwo(user: User, params: JSONObject) {
        user.group?.setEpicTwo(params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun setEpicThree(user: User, params: JSONObject) {
        user.group?.setEpicThree(params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun setDecision(user: User, params: JSONObject) {
        user.group?.setDecision(params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun setStoryOne(user: User, params: JSONObject) {
        user.group?.setStoryOne(params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun setStoryTwo(user: User, params: JSONObject) {
        user.group?.setStoryTwo(params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun setSprintTime(user: User, params: JSONObject) {
        user.group?.setSprintTime(params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun estimateStoryOne(user: User, params: JSONObject) {
        user.group?.estimateStoryOne(user, params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun estimateStoryTwo(user: User, params: JSONObject) {
        user.group?.estimateStoryTwo(user, params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun startSprintDaily(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.startSprintDaily(user)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun setTaskReady(user: User, params: JSONObject) {
        user.group?.setTaskReady(params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun setTaskInProgress(user: User, params: JSONObject) {
        user.group?.setTaskInProgress(params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun setTaskDeveloperOne(user: User, params: JSONObject) {
        user.group?.setTaskDeveloperOne(user, params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun setTaskDeveloperTwo(user: User, params: JSONObject) {
        user.group?.setTaskDeveloperTwo(user, params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun setTaskTest(user: User, params: JSONObject) {
        user.group?.setTaskTest(params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     * @param params Parameter
     */
    private suspend fun setTaskNotTest(user: User, params: JSONObject) {
        user.group?.setTaskNotTest(params)
    }

    /**
     * Weiterleitung eines Funktionsaufrufs zur Gruppe des Nutzers
     *
     * @param user aufrufender Nutzer
     */
    private suspend fun startSprintReview(user: User, @Suppress("UNUSED_PARAMETER") params: JSONObject) {
        user.group?.startSprintReview(user)
    }

    // Loading the structure //////////////////////////////

    /**
     * Parst Epic aus Dino.json
     */
    private fun parseEpic(epicJSON: JSONObject): Epic {
        return Epic(mapKeys(epicJSON) {
            parseDecision(it, epicJSON.getJSONObject(it))
        })
    }

    /**
     * Parst Entscheidungsmöglichkeit aus Dino.json
     */
    private fun parseDecision(decisionName: String, decisionJSON: JSONObject): Decision {
        val poolOneJSON = decisionJSON.getJSONObject(JSON.L_POOL_1)
        val poolTwoJSON = decisionJSON.getJSONObject(JSON.L_POOL_2)
        return Decision(
            decisionName,
            mapKeys(poolOneJSON) { parseStory(poolOneJSON.getJSONObject(it)) },
            mapKeys(poolTwoJSON) { parseStory(poolTwoJSON.getJSONObject(it)) }
        )
    }

    /**
     * Parst Story aus Dino.json
     */
    private fun parseStory(storyJSON: JSONObject): Story {
        val rewardsJSON = storyJSON.getJSONArray(JSON.L_REWARD)
        val rewards: MutableList<Int> = mutableListOf()
        for (i in 0 until rewardsJSON.length()) {
            val rewardJSON = rewardsJSON.getJSONObject(i)
            rewards.add(rewardJSON.getInt(JSON.L_ID))
        }
        val containsJSON = storyJSON.getJSONArray(JSON.L_CONTAIN)
        val contains: MutableList<Int> = mutableListOf()
        for (i in 0 until containsJSON.length()) {
            val containJSON = containsJSON.getJSONObject(i)
            contains.add(containJSON.getInt(JSON.L_ID))
        }
        val tasksJSON = storyJSON.getJSONObject(JSON.L_TASKS)
        val tasksAlwaysJSON = tasksJSON.getJSONArray(JSON.L_TASKS_ALWAYS)
        val tasksChooseJSON = tasksJSON.getJSONArray(JSON.L_TASKS_CHOOSE)
        val tasksAlways = mapIndices(tasksAlwaysJSON) {
            parseTask(tasksAlwaysJSON.getJSONObject(it))
        }
        val tasksChoose = mapIndices(tasksChooseJSON) {
            parseTask(tasksChooseJSON.getJSONObject(it))
        }
        tasksAlways.addAll(tasksChoose)
        return Story(
            storyJSON.getString(JSON.L_NAME),
            storyJSON.getString(JSON.L_DESCR),
            toIntList(storyJSON.getJSONArray(JSON.L_REQUIRE)),
            contains,
            rewards,
            tasksAlways,
            tasksChoose
        )
    }

    /**
     * Parst Task aus Dino.json
     */
    private fun parseTask(taskJSON: JSONObject): Task {
        val dinoRelJSON = taskJSON.getJSONObject(JSON.L_DINO_R)
        val dinoRel: MutableMap<String, Int> = mutableMapOf()
        dinoRelJSON.keys().forEach { dinoRelKey ->
            dinoRel[dinoRelKey] = dinoRelJSON.getInt(dinoRelKey)
        }
        val dinoAbsJSON = taskJSON.getJSONObject(JSON.L_DINO_A)
        val dinoAbs: MutableMap<String, Triple<String, Int, Int>> = mutableMapOf()
        dinoAbsJSON.keys().forEach { dinoAbsKey ->
            val dinoAbsRuleJSON = dinoAbsJSON.getJSONObject(dinoAbsKey)
            val operator = dinoAbsRuleJSON.getString(JSON.L_OP)
            if (operator == JSON.L_GE || operator == JSON.L_LE || operator == JSON.L_EQ) {
                dinoAbs[dinoAbsKey] = Triple(operator, dinoAbsRuleJSON.getInt(JSON.L_VAL), 0)
            } else if (operator == JSON.L_BT) {
                dinoAbs[dinoAbsKey] = Triple(
                    operator,
                    dinoAbsRuleJSON.getInt(JSON.L_VALL),
                    dinoAbsRuleJSON.getInt(JSON.L_VALH)
                )
            }
        }
        return Task(
            taskJSON.getString(JSON.L_NAME),
            taskJSON.getDouble(JSON.L_TIME),
            toIntList(taskJSON.getJSONArray(JSON.L_REQUIRE)),
            if (taskJSON.isNull(JSON.L_CONTAIN)) null else taskJSON.getInt(JSON.L_CONTAIN),
            dinoRel,
            dinoAbs
        )
    }

    /**
     * Die Dino-Struktur die in resourced/Dino.json als vorliegt wird eingelesen
     * damit sie als Objekt verarbeitet werden kann.
     *
     * @return Objekt welches die Struktur repräsentiert.
     */
    fun loadDinoJSON(): Structure {
        val content = File("src/main/resources/Dino.json").readText(Charsets.UTF_8)
        val json = JSONObject(content)
        val epics: MutableList<Epic> = mutableListOf(
            parseEpic(json.getJSONObject("e1")),
            parseEpic(json.getJSONObject("e2")),
            parseEpic(json.getJSONObject("e3"))
        )
        return Structure(epics)
    }

    /**
     * initialisiert alle möglichen Funktionsaufrufe die der Client
     * auf dem Server aufruft
     */
    fun initialize() {
        RPC.register(JSON.REQUEST_GROUP_LIST, ::requestGroupList)
        RPC.register(JSON.CREATE_GROUP, ::createGroup)
        RPC.register(JSON.JOIN_GROUP, ::joinGroup)
        RPC.register(JSON.LEAVE_GROUP, ::leaveGroup)
        RPC.register(JSON.READY_FOR_INTRO, ::readyForIntro)
        RPC.register(JSON.READY_FOR_SIMULATION, ::readyForSimulation)
        RPC.register(JSON.REQUEST_UPDATE_ALL, ::requestUpdateAll)
        RPC.register(JSON.REQUEST_UPDATE_DINO, ::requestUpdateDino)
        RPC.register(JSON.REQUEST_UPDATE_DEVELOPERS, ::requestUpdateDevelopers)
        RPC.register(JSON.REQUEST_UPDATE_SPRINT_OVERVIEW, ::requestUpdateSprintOverview)
        RPC.register(JSON.REQUEST_UPDATE_PRODUCT_BACKLOG, ::requestUpdateProductBacklog)
        RPC.register(JSON.REQUEST_UPDATE_SPRINT_BACKLOG, ::requestUpdateSprintBacklog)
        RPC.register(JSON.REQUEST_UPDATE_ESTIMATION, ::requestUpdateEstimation)
        RPC.register(JSON.REQUEST_UPDATE_SPRINT_PROGRESS, ::requestUpdateSprintProgress)
        RPC.register(JSON.REQUEST_UPDATE_SPRINT_REVIEW, ::requestUpdateSprintReview)
        RPC.register(JSON.REQUEST_UPDATE_SPRINT_RETRO, ::requestUpdateSprintRetro)
        RPC.register(JSON.REQUEST_UPDATE_EVALUATION, ::requestUpdateEvaluation)
        RPC.register(JSON.CONFIRM_PHASE, ::confirmPhase)
        RPC.register(JSON.SET_EPIC_ONE, ::setEpicOne)
        RPC.register(JSON.SET_EPIC_TWO, ::setEpicTwo)
        RPC.register(JSON.SET_EPIC_THREE, ::setEpicThree)
        RPC.register(JSON.SET_DECISION, ::setDecision)
        RPC.register(JSON.SET_STORY_ONE, ::setStoryOne)
        RPC.register(JSON.SET_STORY_TWO, ::setStoryTwo)
        RPC.register(JSON.SET_SPRINT_TIME, ::setSprintTime)
        RPC.register(JSON.ESTIMATE_STORY_ONE, ::estimateStoryOne)
        RPC.register(JSON.ESTIMATE_STORY_TWO, ::estimateStoryTwo)
        RPC.register(JSON.START_SPRINT_DAILY, ::startSprintDaily)
        RPC.register(JSON.SET_TASK_READY, ::setTaskReady)
        RPC.register(JSON.SET_TASK_IN_PROGRESS, ::setTaskInProgress)
        RPC.register(JSON.SET_TASK_DEVELOPER_ONE, ::setTaskDeveloperOne)
        RPC.register(JSON.SET_TASK_DEVELOPER_TWO, ::setTaskDeveloperTwo)
        RPC.register(JSON.SET_TASK_TEST, ::setTaskTest)
        RPC.register(JSON.SET_TASK_NOT_TEST, ::setTaskNotTest)
        RPC.register(JSON.START_SPRINT_REVIEW, ::startSprintReview)

        defaultStructure = loadDinoJSON()
        println("Server started.")
    }
}

package structure

import JSON
import cocomo.timeCalcDeveloperDependent
import cocomo.timeCalcDinoDependent
import dino.Dino
import groups.Developer
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

/**
 * Task
 *
 * @param Name der Task
 * @param Zeit welche die Task in Anspruch nimmt
 * @param requirements Voraussetzungen die der Dino erfüllen muss
 * @param contains Abschlüsse die der Dino durch Bearbeitung der Task erhalten kann
 * @param dinoRel Relative Verbesserungen der Fähigkeiten des Dinos
 * @param dinoAbs Absolute Verbesserungen der Fähigkeiten des Dinos
 */
class Task(
    val name: String,
    val time: Double,
    val requirements: List<Int>,
    val contains: Int?,
    val dinoRel: Map<String, Int>,
    val dinoAbs: Map<String, Triple<String, Int, Int>>
    ) {

    companion object {
        private var counter: Int = 0
        const val BASE_FAILURE_PROBABILITY: Double = 0.5
    }

    val id = counter++

    var developerOne: Developer? = null
    var developerTwo: Developer? = null

    var status: TaskStatus = TaskStatus.READY
    var test: Boolean = false
    var sprint: Int = 0

    var completion: Double = 0.0
    var tested: Double = 0.0
    private var speed: Double = 1.0

    var daily: Boolean = false

    val involvedDevelopers: MutableSet<Developer> = mutableSetOf()

    /**
     * Berechnung der Geschwindigkeit in Abhängigkeit
     * vom Dino mit der die Task bearbeitet werden kann
     *
     * @param dino Dino
     * @return Geschwindigkeit
     */
    private fun calcSpeed(dino: Dino): Double {
        return 3.0 * timeCalcDeveloperDependent(this) * timeCalcDinoDependent(this, dino)
    }

    /**
     * Aktualisiert den Fortschritt der Task in Abhängigkeit von Geschwindigkeit, Dino, Entwickler und ob
     * getestet werden soll oder nicht.
     */
    fun update() {
        if (status == TaskStatus.IN_PROGRESS) {
            var calculatedSpeed = speed
            if (test) {
                calculatedSpeed /= 2
                tested += calculatedSpeed
            }
            completion += calculatedSpeed
            if (completion >= 100.0) {
                completion = 100.0
                status = TaskStatus.IN_REVIEW
            }
        }
    }

    /**
     * Berechnet die Fehlerwahrscheinlichkeit für diese Aufgabe
     *
     * @return Fehlerwahrscheinlichkeit
     */
    private fun getFailureProbability(): Double {
        var prob = BASE_FAILURE_PROBABILITY
        if (daily) {
            prob /= 2.0
        }
        prob /= (tested / 100.0) + 1
        return prob
    }

    /**
     * Entscheidet in Abhängigkeit von der Fehlerwahrscheinlichkeit
     * per Zufall ob die Task erfolgreich bearbeitet wurde oder nicht
     */
    fun success(): Boolean {
        return Random.nextDouble() > getFailureProbability()
    }

    /**
     * @return Ob ein Entwickler zugewiesen ist oder nicht
     */
    fun hasDeveloper(): Boolean {
        return developerOne != null || developerTwo != null
    }

    /**
     * Weist ersten Entwickler der Task zu und berechnet die neue
     * Bearbeitungsgeschwindigkeit
     *
     * @param dev Entwickler der zugewiesen wird
     * @param dino Dino
     */
    fun assignDeveloperOne(dev: Developer, dino: Dino) {
        dev.task?.unassignDeveloper(dev, dino)
        developerOne = dev
        involvedDevelopers.add(dev)
        speed = calcSpeed(dino)
        dev.task = this
    }

    /**
     * Weist zweiten Entwickler der Task zu und berechnet die neue
     * Bearbeitungsgeschwindigkeit
     *
     * @param dev Entwickler der zugewiesen wird
     * @param dino Dino
     */
    fun assignDeveloperTwo(dev: Developer, dino: Dino) {
        dev.task?.unassignDeveloper(dev, dino)
        developerTwo = dev
        involvedDevelopers.add(dev)
        speed = calcSpeed(dino)
        dev.task = this
    }

    /**
     * Zieht Entwickler von der Task ab und berechnet die neue
     * Bearbeitungsgeschwindigkeit
     *
     * @param dev Entwickler der abgezogen wird
     * @param dino Dino
     */
    private fun unassignDeveloper(dev: Developer, dino: Dino) {
        if (developerOne == dev) {
            developerOne = null
            dev.task = null
        } else if (developerTwo == dev) {
            developerTwo = null
            dev.task = null
        }
        speed = calcSpeed(dino)
        if (developerOne == null && developerTwo == null && status == TaskStatus.IN_PROGRESS) {
            status = TaskStatus.READY
        }
    }

    /**
     * Tiefe Kopie der Tasks
     *
     * @return tiefe Kopie
     */
    fun copy(): Task {
        return Task(name, time, requirements, contains, dinoRel, dinoAbs)
    }

    /**
     * @return JSON-Repräsentation der Task
     */
    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put(JSON.TASK_ID, id)
        json.put(JSON.TASK_NAME, name)
        json.put(JSON.TASK_DEVELOPER_ONE, developerOne?.toJSON())
        json.put(JSON.TASK_DEVELOPER_TWO, developerTwo?.toJSON())
        json.put(JSON.TASK_STATUS, status.toJSON())
        json.put(JSON.TASK_TIME, time)
        json.put(JSON.TASK_TEST, test)
        json.put(JSON.TASK_SPRINT, sprint)
        json.put(JSON.TASK_COMPLETION, completion)
        json.put(JSON.TASK_FAILURE_PROBABILITY, (getFailureProbability() * 100).toInt())
        json.put(JSON.TASK_REQUIREMENTS, requirements.fold(JSONArray()) { acc, i -> acc.put(i) })
        return json
    }

    /**
     * @return JSON-Repräsentation der Task mit Zusatzinformationen für die Auswertung
     */
    fun toJSONForEvaluation(): JSONObject {
        val json = JSONObject()
        json.put(JSON.TASK_ID, id)
        json.put(JSON.TASK_NAME, name)
        json.put(JSON.TASK_STATUS, status.toJSON())
        json.put(JSON.TASK_TEST, test)
        json.put(JSON.TASK_SPRINT, sprint)
        json.put(JSON.TASK_COMPLETION, completion)
        json.put(JSON.TASK_FAILURE_PROBABILITY, (getFailureProbability() * 100).toInt())
        json.put(JSON.INVOLVED_DEVELOPERS, involvedDevelopers.toList().fold(JSONArray()) {
                acc, developer -> acc.put(developer.toJSON())
        })
        json.put(JSON.TESTED, tested)
        return json
    }
}

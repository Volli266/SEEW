package structure

import JSON
import dino.Dino
import org.json.JSONArray
import org.json.JSONObject

/**
 * Story
 *
 * @param name Name der Story
 * @param description Beschreibung der Story
 * @param requirements Voraussetzungen die der Dino erfüllen muss
 * @param contains Abschlüsse die der Dino durch Bearbeitung der Story erhalten kann
 * @param rewards Verbesserungen die der Dino durch Bearbeitung der Story erhält
 * @param tasksAlways Liste von Tasks die immer bearbeitet werden müssen
 * @param tasksChoose Liste von Tasks aus denen gewählt wird
 */
class Story(
    private val name: String,
    private val description: String,
    val requirements: List<Int>,
    private val contains: List<Int>,
    private val rewards: List<Int>,
    private val tasksAlways: List<Task>,
    private val tasksChoose: List<Task>
    ) {

    companion object {
        private var counter: Int = 0
    }

    val id = counter++

    var sprint: Int = 0
    var estimation: Int = 0

    /**
     * Liefert alle Tasks die diese Story beinhaltet
     *
     * @return Tasks die diese Story beinhaltet
     */
    fun getAllTasks(): List<Task> {
        val all: MutableList<Task> = mutableListOf()
        all.addAll(tasksAlways)
        all.addAll(tasksChoose)
        return all
    }

    /**
     * Liefert alle Tasks die ausgehend von den aktuellen Fähigkeiten
     * des Dinos bearbeitet werden dürfen.
     *
     * @param dino Dino
     * @return Tasks die bearbeitet werden dürfen
     */
    fun getSelectedTasks(dino: Dino): List<Task> {
        val selected: MutableList<Task> = mutableListOf()
        selected.addAll(tasksAlways)
        for (task in tasksChoose) {
            if (dino.shadowAbilities.containsAll(task.requirements)) {
                for (task2 in tasksChoose) {
                    if (task2.requirements.equals(task.requirements)) {
                        selected.add(task2)
                    }
                }
                break
            }
        }
        return selected
    }

    /**
     * Tiefe Kopie der Story
     *
     * @return tiefe Kopie
     */
    fun copy(): Story {
        return Story(name, description, requirements, contains, rewards, tasksAlways.map { it.copy() }, tasksChoose.map { it.copy() })
    }

    /**
     * @param dino Dino
     * @return JSON-Repräsentation der Story
     */
    fun toJSON(dino: Dino): JSONObject {
        val json = JSONObject()
        json.put(JSON.STORY_ID, id)
        json.put(JSON.STORY_NAME, name)
        json.put(JSON.STORY_DESCRIPTION, description)
        json.put(JSON.STORY_SPRINT, sprint)
        json.put(JSON.STORY_ESTIMATION, estimation)
        json.put(JSON.STORY_REQUIREMENTS, requirements.fold(JSONArray()) { acc, i ->  acc.put(i) })
        json.put(JSON.STORY_REWARDS, rewards.fold(JSONArray()) { acc, i -> acc.put(i) })
        json.put(JSON.STORY_TASKS, getSelectedTasks(dino).fold(JSONArray()) { acc, task -> acc.put(task.toJSON())})
        return json
    }

    /**
     * @return JSON-Repräsentation der Story mit Zusatzinformationen für die Auswertung
     */
    fun toJSONForEvaluation(): JSONObject {
        val json = JSONObject()
        json.put(JSON.STORY_ID, id)
        json.put(JSON.STORY_NAME, name)
        json.put(JSON.STORY_DESCRIPTION, description)
        json.put(JSON.STORY_ESTIMATION, estimation)
        json.put(JSON.STORY_TASKS, getAllTasks().fold(JSONArray()) { acc, task -> acc.put(task.toJSONForEvaluation())})
        return json
    }
}

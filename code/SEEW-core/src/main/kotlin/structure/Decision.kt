package structure

import dino.Dino
import org.json.JSONArray
import org.json.JSONObject

/**
 * Entscheidungsmöglichkeit
 *
 * @param name Name der Entscheidungsmöglichkeit
 * @param poolOne Stories von denen eine gewählt werden kann
 * @param poolTwo Stories von denen eine gewählt werden kann
 */
class Decision(
    private val name: String,
    private val poolOne: List<Story>,
    private val poolTwo: List<Story>
) {

    companion object {
        private var counter: Int = 0
    }

    val id = counter++

    var chosenStoryOne: Story? = null
    var chosenStoryTwo: Story? = null

    /**
     * Setzt die Story aus Pool 1 für die sich entschieden für wurde
     *
     * @param Nummer der Story
     */
    fun setStoryOne(id: Int?) {
        id?.let { chosenStoryOne = poolOne.find { innerIt -> innerIt.id == it } }
    }

    /**
     * Setzt die Story aus Pool 2 für die sich entschieden für wurde
     *
     * @param Nummer der Story
     */
    fun setStoryTwo(id: Int?) {
        id?.let { chosenStoryTwo = poolTwo.find { innerIt -> innerIt.id == it } }
    }

    /**
     * @return Ob sich schon für zwei Stories entschieden wurde
     */
    fun storiesSet(): Boolean {
        return chosenStoryOne != null && chosenStoryTwo != null
    }

    /**
     * Tiefe Kopie der Entscheidungsmöglichkeit
     *
     * @return tiefe Kopie
     */
    fun copy(): Decision {
        return Decision(name, poolOne.map { it.copy() }, poolTwo.map { it.copy() })
    }

    /**
     * Liefert Liste von Stories aus Pool 1 für die sich abhängig von den
     * Fähigkeiten des Dinos entschieden werden kann
     *
     * @param dino Dino
     * @return Liste von Stories für die sich entschieden werden kann
     */
    private fun poolOneSelected(dino: Dino): List<Story> {
        return poolOne.filter { dino.shadowAbilities.containsAll(it.requirements) }
    }

    /**
     * Liefert Liste von Stories aus Pool 2 für die sich abhängig von den
     * Fähigkeiten des Dinos entschieden werden kann
     *
     * @param dino Dino
     * @return Liste von Stories für die sich entschieden werden kann
     */
    private fun poolTwoSelected(dino: Dino): List<Story> {
        return poolTwo.filter { dino.shadowAbilities.containsAll(it.requirements) }
    }

    /**
     * @param dino Dino
     * @return JSON-Repräsentation des Entscheidungsmöglichkeit
     */
    fun toJSON(dino: Dino): JSONObject {
        val json = JSONObject()
        json.put(JSON.DECISION_NAME, name)
        json.put(JSON.DECISION_ID, id)
        json.put(JSON.STORY_POOL_ONE, poolOneSelected(dino).fold(JSONArray()) { acc, story -> acc.put(story.toJSON(dino)) })
        json.put(JSON.STORY_POOL_TWO, poolTwoSelected(dino).fold(JSONArray()) { acc, story -> acc.put(story.toJSON(dino)) })
        json.put(JSON.CHOSEN_STORY_ONE, chosenStoryOne?.id)
        json.put(JSON.CHOSEN_STORY_TWO, chosenStoryTwo?.id)
        return json
    }

    /**
     * @param dino Dino
     * @return JSON-Repräsentation der Entscheidungsmöglichkeit mit Zusatzinformationen für die Auswertung
     */
    fun toJSONForEvaluation(dino: Dino): JSONObject {
        val json = JSONObject()
        json.put(JSON.DECISION_NAME, name)
        json.put(JSON.DECISION_ID, id)
        json.put(JSON.STORY_POOL_ONE, poolOne.fold(JSONArray()) { acc, story -> acc.put(story.toJSON(dino)) })
        json.put(JSON.STORY_POOL_TWO, poolTwo.fold(JSONArray()) { acc, story -> acc.put(story.toJSON(dino)) })
        json.put(JSON.CHOSEN_STORY_ONE, chosenStoryOne?.id)
        json.put(JSON.CHOSEN_STORY_TWO, chosenStoryTwo?.id)
        json.put(JSON.CHOSEN_STORY_ONE_EVAL, chosenStoryOne?.toJSONForEvaluation())
        json.put(JSON.CHOSEN_STORY_TWO_EVAL, chosenStoryTwo?.toJSONForEvaluation())
        return json
    }
}

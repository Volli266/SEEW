package structure

import dino.Dino
import org.json.JSONArray
import org.json.JSONObject

/**
 * Epic
 *
 * @param decisions Liste von Entscheidungen die in diesem Epic getroffen werden können
 */
class Epic(private val decisions: List<Decision>) {

    private var chosenDecision: Int = 0

    /**
     * Setzen der Entscheidung
     *
     * @param id Entscheidungs-ID
     */
    fun setDecision(id: Int) {
        if (id in 0 until decisions.size) chosenDecision = id
    }

    /**
     * Liefert die Entscheidungsmöglichkeit für die sich entschieden wurde
     *
     * @return Entscheidungsmöglichkeit
     */
    fun getDecision(): Decision {
        return decisions[chosenDecision]
    }

    /**
     * Tiefe Kopie des Epics
     *
     * @return tiefe Kopie
     */
    fun copy(): Epic {
        return Epic(decisions.map { it.copy() })
    }

    /**
     * @param dino Dino
     * @return JSON-Repräsentation des Epics
     */
    fun toJSON(dino: Dino): JSONObject {
        val json = JSONObject()
        json.put(JSON.DECISIONS, decisions.fold(JSONArray()) { acc, decision -> acc.put(decision.toJSON(dino)) })
        json.put(JSON.CHOSEN_DECISION, chosenDecision)
        return json
    }

    /**
     * @param dino Dino
     * @return JSON-Repräsentation des Epics mit Zusatzinformationen für die Auswertung
     */
    fun toJSONForEvaluation(dino: Dino): JSONObject {
        val json = JSONObject()
        json.put(JSON.DECISIONS, decisions.fold(JSONArray()) { acc, decision -> acc.put(decision.toJSON(dino)) })
        json.put(JSON.CHOSEN_DECISION, chosenDecision)
        json.put(JSON.CHOSEN_DECISION_EVAL, decisions[chosenDecision].toJSONForEvaluation(dino))
        return json
    }
}

package dino

import org.json.JSONObject

/**
 * Speichert die Eigenschaften des Dinos
 */
class Dino(val name: String, val color: String) {

    var social: Int = 0
        set(value){ field = betweenZeroTen(value) }
    var logic: Int = 0
        set(value){ field = betweenZeroTen(value) }
    var courage: Int = 0
        set(value){ field = betweenZeroTen(value) }
    var reaction: Int = 0
        set(value){ field = betweenZeroTen(value) }
    var agility: Int = 0
        set(value){ field = betweenZeroTen(value) }
    var dexterity: Int = 0
        set(value){ field = betweenZeroTen(value) }
    var strength: Int = 0
        set(value){ field = betweenZeroTen(value) }
    var weight: Int = 0
        set(value){ field = betweenZeroTen(value) }
    var size: Int = 0
        set(value){ field = betweenZeroTen(value) }

    var front: FrontLimb = FrontLimb.NORMAL
    var back: BackLimb = BackLimb.NORMAL
    var tail: Tail = Tail.NORMAL

    var abilities: MutableSet<Int> = mutableSetOf()
    var shadowAbilities: MutableSet<Int> = mutableSetOf()

    /**
     * Prüft ob Wert zwischen 0 und 10 ist.
     * Wenn ja wird dieser Wert zurückgegeben
     * Falls nicht der nächstgelegen Wert in diesem
     * Intervall
     *
     * @param value zu prüfender Wert
     */
    private fun betweenZeroTen(value: Int): Int = when {
        value < 0 -> 0
        value > 10 -> 10
        else -> value
    }

    /**
     * Liefert die Stärke der Ausprägung von Eigenschaften
     * des Dinos. Schlüssel sind hier die in resources/Dino.json
     * genutzten Schlüssel.
     *
     * @param key Schlüssel (Name) der Eigenschaft
     * @return Wert der Ausprägung der Eigenschaft
     */
    fun getSkill(key: String): Int {
        return when (key) {
            "sf" -> social
            "l" -> logic
            "m" -> courage
            "rz" -> reaction
            "b" -> agility
            "bg" -> dexterity
            "st" -> strength
            "gw" -> weight
            "gr" -> size
            else -> 0
        }
    }

    /**
     * @return JSON-Repräsentation des Dino-Objekts
     */
    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put(JSON.DINO_NAME, name)
        json.put(JSON.DINO_COLOR, color)
        json.put(JSON.DINO_SOCIAL, social)
        json.put(JSON.DINO_LOGIC, logic)
        json.put(JSON.DINO_COURAGE, courage)
        json.put(JSON.DINO_REACTION, reaction)
        json.put(JSON.DINO_AGILITY, agility)
        json.put(JSON.DINO_DEXTERITY, dexterity)
        json.put(JSON.DINO_STRENGTH, strength)
        json.put(JSON.DINO_WEIGHT,weight)
        json.put(JSON.DINO_SIZE, size)
        json.put(JSON.DINO_FRONT, front)
        json.put(JSON.DINO_BACK, back)
        json.put(JSON.DINO_TAIL, tail)
        json.put(JSON.DINO_ABILITIES, abilities.toIntArray())
        return json
    }
}

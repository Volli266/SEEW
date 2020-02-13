package dino

/**
 * Auflistung der möglichen hinteren Extremitäten
 * des Dinos
 */
enum class BackLimb(val id: String) {
    NORMAL("Normal"),
    CLAW("Klaue"),
    SUCTIONCUP("Saugnapf"),
    SUCTIONCUP_AND_CLAWS("Saugnäpfe und Klauen")
}

package dino

/**
 * Auflistung der möglichen vorderen Extremitäten
 * des Dinos
 */
enum class FrontLimb(val id: String) {
    NORMAL("Normal"),
    CLAW("Klauen"),
    SUCTIONCUP("Saugnäpfe"),
    WING("Flügel"),
    SUCTIONCUP_AND_CLAWS("Saugnäpfe und Klauen")
}

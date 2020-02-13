package estimation

import kotlin.math.roundToInt

val FIB_NUMS: List<Int> = listOf(0, 1, 2, 3, 5, 8, 13, 21)

/**
 * Methode die zurückgibt, ob die Schaetzwerte zu weit auseinander liegen
 *
 * @param listValues Liste von Werten bei denen geprueft werden soll
 * @return <code>true</code> sonst
 *         <code>false</code> Werte liegen zu weit auseinander
 */
fun estimatesValid(estimates: List<Int>): Boolean {
    val max: Int? = estimates.max()
    val min: Int? = estimates.min()
    return if (max == null || min == null) true else max - min < 4
}

/**
 * Methode die den Schätzwert zurückgibt
 *
 * @param listValues Liste von Werten bei der Wert bestimmt werden soll
 * @return Schaetzwert
 */
fun average(estimates: List<Int>)
        = (estimates.sum().toDouble() / estimates.size).roundToInt()

fun findConsensus(estimates: List<Int>)
        = if (estimatesValid(estimates)) FIB_NUMS[average(estimates)] else -666

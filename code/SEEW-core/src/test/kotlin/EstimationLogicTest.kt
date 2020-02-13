
import estimation.*
import org.junit.Assert.assertEquals
import org.junit.Test

internal class EstimationLogicTest {

    @Test
    fun test() {

        val listVal1: List<Int> = listOf(6, 6, 7, 7)
        assertEquals(estimatesValid(listVal1), true)
        assertEquals(average(listVal1), 7)
        assertEquals(findConsensus(listVal1), 21)

        val listVal2: List<Int> = listOf(1, 2, 4, 7)
        assertEquals(estimatesValid(listVal2), false)
        assertEquals(average(listVal2), 4)
        assertEquals(findConsensus(listVal2), -666)

        val listVal3: List<Int> = listOf(1, 2, 4)
        assertEquals(estimatesValid(listVal3), true)
        assertEquals(average(listVal3), 2)
        assertEquals(findConsensus(listVal3), 2)

        val listVal4: List<Int> = listOf(1, 2, 6)
        assertEquals(estimatesValid(listVal4), false)
        assertEquals(average(listVal4), 3)
        assertEquals(findConsensus(listVal4), -666)

    }
}

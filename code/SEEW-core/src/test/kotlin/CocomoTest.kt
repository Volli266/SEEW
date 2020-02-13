import cocomo.developerImprovement
import cocomo.timeCalcDeveloperDependent
import cocomo.timeCalcDinoDependent
import dino.Dino
import groups.Developer
import org.junit.Assert.assertEquals
import org.junit.Test
import structure.Task


internal class CocomoTest {

    @Test
    fun test() {

        val horst = Dino("Horst", "green")
        horst.logic = 5
        horst.courage = 8
        horst.social = 2
        horst.reaction = 1
        horst.agility = 6
        horst.dexterity = 4
        horst.strength = 3
        horst.weight = 8
        horst.size = 10

        val testDevOne = Developer()
        val testDevTwo = Developer()
        val testDevThree = Developer()
        val testDevFour = Developer()
        val testDevFive = Developer()

        testDevOne.incSocial(1)
        testDevOne.incLogic(3)
        testDevOne.incCourage(6)
        testDevOne.incReaction(9)
        testDevOne.incAgility(1)
        testDevOne.incDexterity(3)
        testDevOne.incStrength(7)
        testDevOne.incWeight(3)
        testDevOne.incSize(2)
        testDevOne.incBody(4)

        testDevTwo.incSocial(2)
        testDevTwo.incLogic(1)
        testDevTwo.incCourage(2)
        testDevTwo.incReaction(1)
        testDevTwo.incAgility(2)
        testDevTwo.incDexterity(1)
        testDevTwo.incStrength(2)
        testDevTwo.incWeight(1)
        testDevTwo.incSize(2)
        testDevTwo.incBody(1)

        testDevThree.incSocial(1)
        testDevThree.incLogic(0)
        testDevThree.incCourage(1)
        testDevThree.incReaction(2)
        testDevThree.incAgility(1)
        testDevThree.incDexterity(0)
        testDevThree.incStrength(1)
        testDevThree.incWeight(2)
        testDevThree.incSize(2)
        testDevThree.incBody(1)

        testDevFour.incSocial(6)
        testDevFour.incLogic(5)
        testDevFour.incCourage(6)
        testDevFour.incReaction(7)
        testDevFour.incAgility(4)
        testDevFour.incDexterity(6)
        testDevFour.incStrength(6)
        testDevFour.incWeight(5)
        testDevFour.incSize(5)
        testDevFour.incBody(6)

        testDevFive.incSocial(10)
        testDevFive.incLogic(5)
        testDevFive.incCourage(7)
        testDevFive.incReaction(10)
        testDevFive.incAgility(10)
        testDevFive.incDexterity(5)
        testDevFive.incStrength(10)
        testDevFive.incWeight(5)
        testDevFive.incSize(5)
        testDevFive.incBody(10)

        val dinR3 = mutableMapOf<String,Int>()
        dinR3["m"] = 1
        dinR3["st"] = 1
        val dinA3 = mutableMapOf<String, Triple<String,Int,Int>>()
        dinA3["m"] =Triple("ge",6,0)
        dinA3["st"] =Triple("ge",5,0)
        val task1 = Task("Strkg", 1.0, listOf(), null,dinR3, dinA3)
        task1.assignDeveloperOne(testDevOne, horst)
        task1.assignDeveloperTwo(testDevTwo, horst)
        assertEquals(timeCalcDinoDependent(task1, horst), 1.0, 1e-5)
        assertEquals(timeCalcDeveloperDependent(task1), 1.0, 1e-5)

        val dinR4 = mutableMapOf<String,Int>()
        dinR4["b"] = 1
        dinR4["bg"] = 2
        val dinA4 = mutableMapOf<String, Triple<String,Int,Int>>()
        dinA4["b"] =Triple("ge",4,0)
        dinA4["bg"] =Triple("ge",6,0)
        val task2 = Task("BwgTrng", 1.5, listOf(), null, dinR4, dinA4 )
        task2.assignDeveloperOne(testDevThree, horst)
        assertEquals(timeCalcDinoDependent(task2, horst), 1.5, 1e-5)
        assertEquals(timeCalcDeveloperDependent(task2), 2.0, 1e-5)

        val dinR5 = mutableMapOf<String,Int>()
        val dinA5 = mutableMapOf<String, Triple<String,Int,Int>>()
        dinA5["extr"] = Triple("eq",1,0)
        val task3 = Task("Bodyshape", 2.0, listOf(), null, dinR5, dinA5 )
        task3.assignDeveloperOne(testDevFour, horst)
        assertEquals(timeCalcDinoDependent(task3, horst), 4.0, 1e-5)
        assertEquals(timeCalcDeveloperDependent(task3), 1.0, 1e-5)

        val dinR6 = mutableMapOf<String,Int>()
        dinR6["m"] = 3
        val dinA6 = mutableMapOf<String, Triple<String,Int,Int>>()
        dinA6["extr"] = Triple("eq",3,0)
        val task4 = Task("Bodyshape", 2.0, listOf(), null, dinR6, dinA6 )
        task4.assignDeveloperOne(testDevTwo, horst)
        task4.assignDeveloperTwo(testDevFive, horst)
        assertEquals(timeCalcDinoDependent(task4, horst), 5.0, 1e-5)
        assertEquals(timeCalcDeveloperDependent(task4), 0.75, 1e-5)

        val dinR7 = mutableMapOf<String,Int>()
        dinR7["m"] = -5
        val dinA7 = mutableMapOf<String, Triple<String,Int,Int>>()
        dinA7["extr"] = Triple("eq",2,0)
        dinA7["m"] = Triple("le",8,0)
        val task5 = Task("Bodyshape", 2.0, listOf(), null, dinR7, dinA7 )
        task5.assignDeveloperOne(testDevFive, horst)
        assertEquals(timeCalcDinoDependent(task5, horst), 5.0, 1e-5)
        assertEquals(timeCalcDeveloperDependent(task5), 0.75, 1e-5)
        developerImprovement(task5)
        assertEquals(task5.developerOne!!.getBody(), 10)


        val dinR8 = mutableMapOf<String,Int>()
        dinR8["m"] = -6
        val dinA8 = mutableMapOf<String, Triple<String,Int,Int>>()
        dinA8["extr"] = Triple("eq",1,0)
        dinA8["m"] = Triple("ge",8,0)
        val task6 = Task("Bodyshape", 2.0, listOf(), null, dinR8, dinA8 )
        task6.assignDeveloperOne(testDevOne, horst)
        task6.assignDeveloperTwo(testDevFour, horst)
        assertEquals(timeCalcDinoDependent(task6, horst), 7.0, 1e-5)
        assertEquals(timeCalcDeveloperDependent(task6), 1.0, 1e-5)
        developerImprovement(task6)

    }
}

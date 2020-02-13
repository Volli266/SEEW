import cocomo.*
import dino.Dino
import org.junit.Test
import structure.Task

internal class ProcessTest {
    fun printDino(currentDino: Dino) {
        println("DINO:")
        println("Name: " + currentDino.name)
        println("Social: " + currentDino.social)
        println("Logic: " + currentDino.logic)
        println("Courage: " + currentDino.courage)
        println("Reaction: " + currentDino.reaction)
        println("Agility: : " + currentDino.agility)
        println("Dexterity: " + currentDino.dexterity)
        println("Strength: " + currentDino.strength)
        println("Weight: " + currentDino.weight)
        println("Size: " + currentDino.size)
        println("Front: " + currentDino.front)
        println("Back: " + currentDino.back)
        println("Tail: " + currentDino.tail)
        println("Abilities" + currentDino.abilities)
        println("Shadow-Abilities" + currentDino.shadowAbilities)
        println()
    }
    fun printTask(currentTask: Task) {
        println("TASK:")
        println("Name: " + currentTask.name)
        println("Time: " + currentTask.time)
        println("Requirements: " + currentTask.requirements)
        println("Contains: " + currentTask.contains)
        println("DinoRel: " + currentTask.dinoRel)
        println("DinoAbs: " + currentTask.dinoAbs)
        println()
    }
    @Test
    fun changeDino() {
        var bodo = Dino("Bodo", "tomato")
        //var struct = GroupService.loadDinoJSON()

        bodo.logic=5
        bodo.courage=8
        bodo.social=2
        bodo.reaction=1
        bodo.agility=6
        bodo.dexterity=4
        bodo.strength=3
        bodo.weight=8
        bodo.size=10

        val dinR1 = mutableMapOf<String,Int>()
        val dinA1 = mutableMapOf<String, Triple<String,Int,Int>>()
        dinA1["gr"] = Triple("le",6,0)
        dinA1["gw"] = Triple("le",5,0)
        var task1 = Task("GrAnp", 0.0, listOf(), null, dinR1, dinA1 )

        val dinR2 = mutableMapOf<String,Int>()
        dinR2["b"] = 0
        dinR2["bg"] = 0
        dinR2["st"] = 0
        val dinA2 = mutableMapOf<String, Triple<String,Int,Int>>()
        var task2 = Task("GrAusb", 2.0, listOf(), 11, dinR2, dinA2 )

        val dinR3 = mutableMapOf<String,Int>()
        dinR3["m"] = 1
        dinR3["st"] = 1
        val dinA3 = mutableMapOf<String, Triple<String,Int,Int>>()
        dinA3["m"] =Triple("ge",6,0)
        dinA3["st"] =Triple("ge",5,0)
        var task3 = Task("Strkg", 1.0, listOf(), null, dinR3, dinA3 )

        val dinR4 = mutableMapOf<String,Int>()
        dinR4["b"] = 1
        dinR4["bg"] = 2
        val dinA4 = mutableMapOf<String, Triple<String,Int,Int>>()
        dinA4["b"] =Triple("ge",4,0)
        dinA4["bg"] =Triple("ge",6,0)
        var task4 = Task("BwgTrng", 1.5, listOf(), null, dinR4, dinA4 )

        val dinR5 = mutableMapOf<String,Int>()
        //dinR5["extr"] = 1
        val dinA5 = mutableMapOf<String, Triple<String,Int,Int>>()
        dinA5["extr"] = Triple("eq",1,0)
        var task5 = Task("Bodyshape", 2.0, listOf(), null, dinR5, dinA5 )

        val dinR6 = mutableMapOf<String,Int>()
        dinR6["m"] = 3
        val dinA6 = mutableMapOf<String, Triple<String,Int,Int>>()
        dinA6["extr"] = Triple("eq",3,0)
        var task6 = Task("Bodyshape", 2.0, listOf(), null, dinR6, dinA6 )

        val dinR7 = mutableMapOf<String,Int>()
        dinR7["m"] = -5
        val dinA7 = mutableMapOf<String, Triple<String,Int,Int>>()
        dinA7["extr"] = Triple("eq",2,0)
        dinA7["m"] = Triple("le",8,0)
        var task7 = Task("Bodyshape", 2.0, listOf(), null, dinR7, dinA7 )

        val dinR8 = mutableMapOf<String,Int>()
        dinR8["m"] = -6
        val dinA8 = mutableMapOf<String, Triple<String,Int,Int>>()
        dinA8["extr"] = Triple("eq",1,0)
        dinA8["m"] = Triple("ge",8,0)
        var task8 = Task("Bodyshape", 2.0, listOf(), null, dinR8, dinA8 )


        println("before:")
        printDino(bodo)
        printTask(task1)

        startTask(task1,bodo)
        println("\"Größenanpassung\" in Progress:")
        printDino(bodo)

        startTask(task2,bodo)
        println("\"Grundausbildung\" in Progress:")
        printDino(bodo)

        startTask(task3,bodo)
        println("\"Stärkung\" in Progress:")
        printDino(bodo)

        startTask(task4,bodo)
        println("\"Bewegungstraining\" in Progress:")
        printDino(bodo)

        startTask(task5,bodo)
        println("\"Bodyshaping\" in Progress:")
        printDino(bodo)

        finishTask(task1,bodo)
        println("\"Größenanpassung\" finished:")
        printDino(bodo)

        finishTask(task2,bodo)
        println("\"Grundausbildung\" finished:")
        printDino(bodo)

        finishTask(task3,bodo)
        println("\"Stärkung\" finished:")
        printDino(bodo)

        finishTask(task4,bodo)
        println("\"Bewegungstraining\" finished:")
        printDino(bodo)

        finishTask(task5,bodo)
        println("\"Bodyshaping\" finished:")
        printDino(bodo)

        finishTask(task6,bodo)
        println("\"Extremitäten nochmal ändern und Mut über 10\" finished:")
        printDino(bodo)

        finishTask(task7,bodo)
        println("\"Krallen und Saugnäpfe und Mut senken\" finished:")
        printDino(bodo)

        finishTask(task8,bodo)
        println("\"Wings again und irgendwas mit Mut\" finished:")
        printDino(bodo)
    }
}

package cocomo

import dino.Dino
import structure.Task
import kotlin.math.abs


///**
// * Methode zur Berechnung ob eine Aufgabe erfolgreich bearbeitet wird
// *
// * @param currentTask die Aufgabe für die die Berechnung durchgeführt werden soll
// * @param dino der Dino dessen Eigenschaften für die Berechnung in Betracht gezogen werden sollen
// * @return <code>true</code> die Aufgabe wurde erfolgreich bearbeitet
// *         <code>flase</code> die Aufage wurde nicht erfolgreich bearbeitet
// */
//fun evaluateSuccess(currentTask: Task, dino: Dino): Boolean {
//    val sprintLength = 6.0
//    return (((timeCalcDinoDependent(currentTask, dino) * timeCalcDeveloperDependent(currentTask)) <= sprintLength) || currentTask.test )
//}

/**
 * Methode zur Berechnung der Bearbeitungslänge einer Aufgabe
 *
 * @param currentTask die Aufgabe für die die Berechnung durchgeführt werden soll
 * @param dino der Dino dessen Eigenschaften für die Berechnung in Betracht gezogen werden sollen
 * @return Wert der Berechnung
 */
fun timeCalcDinoDependent(currentTask: Task, dino: Dino): Double {
    var timeValue = currentTask.time
    val dinoAbsMap: Map<String, Triple<String, Int, Int>> = currentTask.dinoAbs
    dinoAbsMap.keys.forEach {
        val temp = dino.getSkill(it) + relChanges(currentTask.dinoRel, it)
        val el = dinoAbsMap[it]
        if (el != null) {
            when {
                el.first == JSON.L_LE -> timeValue += maxOf(temp - el.second, 0) / 2
                el.first == JSON.L_GE -> timeValue += maxOf(el.second - temp, 0) / 2
                el.first == JSON.L_BT -> {
                    if (temp < el.second) {
                        timeValue += maxOf(temp - el.second, 0) / 2
                    } else if (temp > el.third) {
                        timeValue += maxOf(el.third - temp, 0) / 2
                    }
                }
                el.first == JSON.L_EQ -> timeValue += abs(temp - el.second) / 2
            }
        }
    }
    dinoAbsMap[JSON.L_EXTR]?.let { timeValue += if (dinoBodyOkay(dinoAbsMap.getValue("extr"), dino)) 0.0 else 2.0 }
    return timeValue
}

fun relChanges(dinoRelMap: Map<String,Int>, keyString: String):Int {
    return when (dinoRelMap.containsKey(keyString)) {
        true -> dinoRelMap.getValue(keyString)
        false -> 0
    }
}
fun dinoBodyOkay(bodyValues: Triple<String, Int, Int>, currDino: Dino): Boolean {
    return  (bodyValues.second == 1 && currDino.front == dino.FrontLimb.WING) ||
            (bodyValues.second == 2 &&
                (currDino.back == dino.BackLimb.SUCTIONCUP || currDino.back == dino.BackLimb.SUCTIONCUP_AND_CLAWS)) ||
            (bodyValues.second == 3 &&
                (currDino.back == dino.BackLimb.CLAW || currDino.back == dino.BackLimb.SUCTIONCUP_AND_CLAWS)) ||
            (bodyValues.second == 4 && currDino.tail == dino.Tail.WHIP)
}

/**
 * Methode zur Berechnung eines Faktors der dann für die Berechnung der Länge verwendet wird
 *
 * @param currentTask die Aufgabe für die die Berechnung durchgeführt werden soll
 * @return Faktor für die Berechnung
 */
fun timeCalcDeveloperDependent(currentTask: Task): Double {
    val skillValueSet: MutableSet<Int> = mutableSetOf()
    val propertyList1 = ArrayList(currentTask.dinoRel.keys)
    val propertyList2 = ArrayList(currentTask.dinoAbs.keys)
    propertyList1.addAll(propertyList2)
    propertyList1.toSet().forEach {
        if (it ==("sf")){
            skillValueSet.add(maxOf(currentTask.developerOne?.getSocial() ?: 0, currentTask.developerTwo?.getSocial()?: 0 ))
        }
        if (it == ("l")){
            skillValueSet.add(maxOf(currentTask.developerOne?.getLogic() ?: 0, currentTask.developerTwo?.getLogic()?: 0 ))
        }
        if (it == ("m")){
            skillValueSet.add(maxOf(currentTask.developerOne?.getCourage() ?: 0, currentTask.developerTwo?.getCourage()?: 0 ))
        }
        if (it == ("rz")){
            skillValueSet.add(maxOf(currentTask.developerOne?.getReaction() ?: 0, currentTask.developerTwo?.getReaction()?: 0 ))
        }
        if (it == ("b")){
            skillValueSet.add(maxOf(currentTask.developerOne?.getAgility() ?: 0, currentTask.developerTwo?.getAgility()?: 0 ))
        }
        if (it == ("bg")){
            skillValueSet.add(maxOf(currentTask.developerOne?.getDexterity() ?: 0, currentTask.developerTwo?.getDexterity()?: 0 ))
        }
        if (it == ("st")){
            skillValueSet.add(maxOf(currentTask.developerOne?.getStrength() ?: 0, currentTask.developerTwo?.getStrength()?: 0 ))
        }
        if (it == ("gw")){
            skillValueSet.add(maxOf(currentTask.developerOne?.getWeight() ?: 0, currentTask.developerTwo?.getWeight()?: 0 ))
        }
        if (it == ("gr")){
            skillValueSet.add(maxOf(currentTask.developerOne?.getSize() ?: 0, currentTask.developerTwo?.getSize()?: 0 ))
        }
        if (it == ("extr")){
            skillValueSet.add(maxOf(currentTask.developerOne?.getBody() ?: 0, currentTask.developerTwo?.getBody()?: 0 ))
        }
    }
    val averageSkillValue: Double = skillValueSet.sum().toDouble() / skillValueSet.size
    return when (averageSkillValue >= 9.0){
        true -> 0.5
        false -> when (averageSkillValue >= 7.0){
            true -> 0.75
            false -> when (averageSkillValue >=4.0){
                true -> 1.0
                false -> 2.0
            }
        }
    }
}


/**
 * Methode die in Abhängigkeit einer Aufgabe die Fähigkeiten von einem oder zwei Entwicklern verbessert
 *
 * @param currentTask die Aufgabe die der/die Entwickler bearbeitet haben
 */
fun developerImprovement(currentTask: Task){
    /*
    if (currentTask.developerTwo != null){
        var improvement = abs(currentTask.developerOne!!.getSocial()  - currentTask.developerTwo!!.getSocial()) / 2
        when (currentTask.developerOne!!.getSocial() <= currentTask.developerTwo!!.getSocial()){
            true -> currentTask.developerOne!!.incSocial(improvement)
            false -> currentTask.developerTwo!!.incSocial(improvement)
        }
        improvement = abs(currentTask.developerOne!!.getLogic()  - currentTask.developerTwo!!.getLogic()) / 2
        when (currentTask.developerOne!!.getLogic() <= currentTask.developerTwo!!.getLogic()){
            true -> currentTask.developerOne!!.incLogic(improvement)
            false -> currentTask.developerTwo!!.incLogic(improvement)
        }
        improvement = abs(currentTask.developerOne!!.getCourage()  - currentTask.developerTwo!!.getCourage()) / 2
        when (currentTask.developerOne!!.getCourage() <= currentTask.developerTwo!!.getCourage()){
            true -> currentTask.developerOne!!.incCourage(improvement)
            false -> currentTask.developerTwo!!.incCourage(improvement)
        }
        improvement = abs(currentTask.developerOne!!.getReaction()  - currentTask.developerTwo!!.getReaction()) / 2
        when (currentTask.developerOne!!.getReaction() <= currentTask.developerTwo!!.getReaction()){
            true -> currentTask.developerOne!!.incReaction(improvement)
            false -> currentTask.developerTwo!!.incReaction(improvement)
        }
        improvement = abs(currentTask.developerOne!!.getAgility()  - currentTask.developerTwo!!.getAgility()) / 2
        when (currentTask.developerOne!!.getAgility() <= currentTask.developerTwo!!.getAgility()){
            true -> currentTask.developerOne!!.incAgility(improvement)
            false -> currentTask.developerTwo!!.incAgility(improvement)
        }
        improvement = abs(currentTask.developerOne!!.getDexterity()  - currentTask.developerTwo!!.getDexterity()) / 2
        when (currentTask.developerOne!!.getDexterity() <= currentTask.developerTwo!!.getDexterity()){
            true -> currentTask.developerOne!!.incDexterity(improvement)
            false -> currentTask.developerTwo!!.incDexterity(improvement)
        }
        improvement = abs(currentTask.developerOne!!.getStrength()  - currentTask.developerTwo!!.getStrength()) / 2
        when (currentTask.developerOne!!.getStrength() <= currentTask.developerTwo!!.getStrength()){
            true -> currentTask.developerOne!!.incStrength(improvement)
            false -> currentTask.developerTwo!!.incStrength(improvement)
        }
        improvement = abs(currentTask.developerOne!!.getWeight()  - currentTask.developerTwo!!.getWeight()) / 2
        when (currentTask.developerOne!!.getWeight() <= currentTask.developerTwo!!.getWeight()){
            true -> currentTask.developerOne!!.incWeight(improvement)
            false -> currentTask.developerTwo!!.incWeight(improvement)
        }
        improvement = abs(currentTask.developerOne!!.getSize()  - currentTask.developerTwo!!.getSize()) / 2
        when (currentTask.developerOne!!.getSize() <= currentTask.developerTwo!!.getSize()){
            true -> currentTask.developerOne!!.incSize(improvement)
            false -> currentTask.developerTwo!!.incSize(improvement)
        }
        improvement = abs(currentTask.developerOne!!.getBody()  - currentTask.developerTwo!!.getBody()) / 2
        when (currentTask.developerOne!!.getBody() <= currentTask.developerTwo!!.getBody()){
            true -> currentTask.developerOne!!.incBody(improvement)
            false -> currentTask.developerTwo!!.incBody(improvement)
        }
    } else {
        currentTask.dinoAbs["sf"]?.let { currentTask.developerOne!!.incSocial(1) }
        currentTask.dinoAbs["l"]?.let { currentTask.developerOne!!.incLogic(1) }
        currentTask.dinoAbs["m"]?.let { currentTask.developerOne!!.incCourage(1) }
        currentTask.dinoAbs["rz"]?.let { currentTask.developerOne!!.incReaction(1) }
        currentTask.dinoAbs["b"]?.let { currentTask.developerOne!!.incAgility(1) }
        currentTask.dinoAbs["bg"]?.let { currentTask.developerOne!!.incDexterity(1) }
        currentTask.dinoAbs["st"]?.let { currentTask.developerOne!!.incStrength(1) }
        currentTask.dinoAbs["gw"]?.let { currentTask.developerOne!!.incWeight(1) }
        currentTask.dinoAbs["gr"]?.let { currentTask.developerOne!!.incSize(1) }
        currentTask.dinoAbs["extr"]?.let { currentTask.developerOne!!.incBody(1) }
    }
    */
    for (dev in currentTask.involvedDevelopers) {
        currentTask.dinoAbs["sf"]?.let { dev.incSocial(1) }
        currentTask.dinoAbs["l"]?.let { dev.incLogic(1) }
        currentTask.dinoAbs["m"]?.let { dev.incCourage(1) }
        currentTask.dinoAbs["rz"]?.let { dev.incReaction(1) }
        currentTask.dinoAbs["b"]?.let { dev.incAgility(1) }
        currentTask.dinoAbs["bg"]?.let { dev.incDexterity(1) }
        currentTask.dinoAbs["st"]?.let { dev.incStrength(1) }
        currentTask.dinoAbs["gw"]?.let { dev.incWeight(1) }
        currentTask.dinoAbs["gr"]?.let { dev.incSize(1) }
        currentTask.dinoAbs["extr"]?.let { dev.incBody(1) }
    }
}



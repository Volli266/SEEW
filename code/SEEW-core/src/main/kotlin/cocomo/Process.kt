package cocomo

import dino.Dino
import structure.Task

fun startTask(currentTask: Task, currentDino: Dino) {
    if (currentTask.contains != null) currentDino.shadowAbilities.add(currentTask.contains)
}

fun finishTask(currentTask: Task, currentDino: Dino) {
    if (currentTask.contains != null) currentDino.abilities.add(currentTask.contains)
    val propertySet: MutableSet<String> = mutableSetOf()
    for (item in currentTask.dinoRel.keys) propertySet.add(item)
    for (item in currentTask.dinoAbs.keys) propertySet.add(item)
    for (item in propertySet) {
        if (item=="sf") {
            if (currentTask.dinoRel.containsKey("sf")) {
                currentDino.social += currentTask.dinoRel["sf"] ?: 0
            }
            if (currentTask.dinoAbs.containsKey("sf")) {
                val op = currentTask.dinoAbs.getValue("sf").first
                val val1 = currentTask.dinoAbs.getValue("sf").second
                val val2 = currentTask.dinoAbs.getValue("sf").third
                if (op=="ge" && currentDino.social < val1 ||
                    op=="le" && currentDino.social > val1 || op=="eq") {
                    currentDino.social = val1
                }
                if (op=="bt" && currentDino.social < val1) currentDino.social = val1
                else if (op=="bt" && currentDino.social > val2) currentDino.social = val2
            }
        } else if (item=="l") {
            if (currentTask.dinoRel.containsKey("l")) {
                currentDino.logic += currentTask.dinoRel["l"] ?: 0
            }
            if (currentTask.dinoAbs.containsKey("l")) {
                val op = currentTask.dinoAbs.getValue("l").first
                val val1 = currentTask.dinoAbs.getValue("l").second
                val val2 = currentTask.dinoAbs.getValue("l").third
                if (op=="ge" && currentDino.logic < val1 ||
                    op=="le" && currentDino.logic > val1 || op=="eq") {
                    currentDino.logic = val1
                }
                if (op=="bt" && currentDino.logic < val1) currentDino.logic = val1
                else if (op=="bt" && currentDino.logic > val2) currentDino.logic = val2
            }
        } else if (item=="m") {
            if (currentTask.dinoRel.containsKey("m")) {
                currentDino.courage += currentTask.dinoRel["m"] ?: 0
            }
            if (currentTask.dinoAbs.containsKey("m")) {
                val op = currentTask.dinoAbs.getValue("m").first
                val val1 = currentTask.dinoAbs.getValue("m").second
                val val2 = currentTask.dinoAbs.getValue("m").third
                if (op=="ge" && currentDino.courage < val1 ||
                    op=="le" && currentDino.courage > val1 || op=="eq") {
                    currentDino.courage = val1
                }
                if (op=="bt" && currentDino.courage < val1) currentDino.courage = val1
                else if (op=="bt" && currentDino.courage > val2) currentDino.courage = val2
            }
        } else if (item=="rz") {
            if (currentTask.dinoRel.containsKey("rz")) {
                currentDino.reaction += currentTask.dinoRel["rz"] ?: 0
            }
            if (currentTask.dinoAbs.containsKey("rz")) {
                val op = currentTask.dinoAbs.getValue("rz").first
                val val1 = currentTask.dinoAbs.getValue("rz").second
                val val2 = currentTask.dinoAbs.getValue("rz").third
                if (op=="ge" && currentDino.reaction < val1 ||
                    op=="le" && currentDino.reaction > val1 || op=="eq") {
                    currentDino.reaction = val1
                }
                if (op=="bt" && currentDino.reaction < val1) currentDino.reaction = val1
                else if (op=="bt" && currentDino.reaction > val2) currentDino.reaction = val2
            }
        } else if (item=="b") {
            if (currentTask.dinoRel.containsKey("b")) {
                currentDino.agility += currentTask.dinoRel["b"] ?: 0
            }
            if (currentTask.dinoAbs.containsKey("b")) {
                val op = currentTask.dinoAbs.getValue("b").first
                val val1 = currentTask.dinoAbs.getValue("b").second
                val val2 = currentTask.dinoAbs.getValue("b").third
                if (op=="ge" && currentDino.agility < val1 ||
                    op=="le" && currentDino.agility > val1 || op=="eq") {
                    currentDino.agility = val1
                }
                if (op=="bt" && currentDino.agility < val1) currentDino.agility = val1
                else if (op=="bt" && currentDino.agility > val2) currentDino.agility = val2
            }
        } else if (item=="bg") {
            if (currentTask.dinoRel.containsKey("bg")) {
                currentDino.dexterity += currentTask.dinoRel["bg"] ?: 0
            }
            if (currentTask.dinoAbs.containsKey("bg")) {
                val op = currentTask.dinoAbs.getValue("bg").first
                val val1 = currentTask.dinoAbs.getValue("bg").second
                val val2 = currentTask.dinoAbs.getValue("bg").third
                if (op=="ge" && currentDino.dexterity < val1 ||
                    op=="le" && currentDino.dexterity > val1 || op=="eq") {
                    currentDino.dexterity = val1
                }
                if (op=="bt" && currentDino.dexterity < val1) currentDino.dexterity = val1
                else if (op=="bt" && currentDino.dexterity > val2) currentDino.dexterity = val2
            }
        } else if (item=="st") {
            if (currentTask.dinoRel.containsKey("st")) {
                currentDino.strength += currentTask.dinoRel["st"] ?: 0
            }
            if (currentTask.dinoAbs.containsKey("st")) {
                val op = currentTask.dinoAbs.getValue("st").first
                val val1 = currentTask.dinoAbs.getValue("st").second
                val val2 = currentTask.dinoAbs.getValue("st").third
                if (op=="ge" && currentDino.strength < val1 ||
                    op=="le" && currentDino.strength > val1 || op=="eq") {
                    currentDino.strength = val1
                }
                if (op=="bt" && currentDino.strength < val1) currentDino.strength = val1
                else if (op=="bt" && currentDino.strength > val2) currentDino.strength = val2
            }
        } else if (item=="gw") {
            if (currentTask.dinoRel.containsKey("gw")) {
                currentDino.weight += currentTask.dinoRel["gw"] ?: 0
            }
            if (currentTask.dinoAbs.containsKey("gw")) {
                val op = currentTask.dinoAbs.getValue("gw").first
                val val1 = currentTask.dinoAbs.getValue("gw").second
                val val2 = currentTask.dinoAbs.getValue("gw").third
                if (op=="ge" && currentDino.weight < val1 ||
                    op=="le" && currentDino.weight > val1 || op=="eq") {
                    currentDino.weight = val1
                }
                if (op=="bt" && currentDino.weight < val1) currentDino.weight = val1
                else if (op=="bt" && currentDino.weight > val2) currentDino.weight = val2
            }
        } else if (item=="gr") {
            if (currentTask.dinoRel.containsKey("gr")) {
                currentDino.size += currentTask.dinoRel["gr"] ?: 0
            }
            if (currentTask.dinoAbs.containsKey("gr")) {
                val op = currentTask.dinoAbs.getValue("gr").first
                val val1 = currentTask.dinoAbs.getValue("gr").second
                val val2 = currentTask.dinoAbs.getValue("gr").third
                if (op=="ge" && currentDino.size < val1 ||
                    op=="le" && currentDino.size > val1 || op=="eq") {
                    currentDino.size = val1
                }
                if (op=="bt" && currentDino.size < val1) currentDino.size = val1
                else if (op=="bt" && currentDino.size > val2) currentDino.size = val2
            }
        } else if (item=="extr") {
            val dinoRelValue: Int
            if (currentTask.dinoRel.containsKey("extr")) {
                dinoRelValue = currentTask.dinoRel["extr"] ?: -1
            } else dinoRelValue = -1
            val dinoAbsValue: Int
            if (currentTask.dinoAbs.containsKey("extr")) {
                dinoAbsValue = currentTask.dinoAbs.getValue("extr").second
            } else dinoAbsValue = -1
            if (dinoRelValue==1 || dinoAbsValue == 1) {
                currentDino.front = dino.FrontLimb.WING
            }
            if (dinoRelValue==2 || dinoAbsValue == 2) {
                if (currentDino.front == dino.FrontLimb.CLAW ||
                    currentDino.front == dino.FrontLimb.SUCTIONCUP_AND_CLAWS) {
                    currentDino.front = dino.FrontLimb.SUCTIONCUP_AND_CLAWS
                    currentDino.back = dino.BackLimb.SUCTIONCUP_AND_CLAWS
                } else {
                    currentDino.front = dino.FrontLimb.SUCTIONCUP
                    currentDino.back = dino.BackLimb.SUCTIONCUP
                }
            }
            if (dinoRelValue== 3 || dinoAbsValue == 3) {
                if (currentDino.front == dino.FrontLimb.SUCTIONCUP ||
                    currentDino.front == dino.FrontLimb.SUCTIONCUP_AND_CLAWS) {
                    currentDino.front = dino.FrontLimb.SUCTIONCUP_AND_CLAWS
                    currentDino.back = dino.BackLimb.SUCTIONCUP_AND_CLAWS
                } else {
                    currentDino.front = dino.FrontLimb.CLAW
                    currentDino.back = dino.BackLimb.CLAW
                }
            }
            if (dinoRelValue==4 || dinoAbsValue == 4) {
                currentDino.tail = dino.Tail.WHIP
            }
        }
    }
}

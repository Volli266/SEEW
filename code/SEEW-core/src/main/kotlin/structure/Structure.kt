package structure

/**
 * Repräsentiert die Struktur in resources/Dino.json
 */
class Structure(val epics: List<Epic>) {

    var epicOrder: List<Int> = mutableListOf(0, 1, 2)

    /**
     * Ändern der Reihenfolge in denen die Epics bearbeitet werden
     *
     * @param epicId Nummer des Epics dessen Priorität geändert werden soll
     * @param newPriority neue Priorität
     */
    fun changeEpicOrder(epicId: Int, newPriority: Int) {
        if (epicId in 0..2 && newPriority in 0..2) {
            val oldPriority = epicOrder.indexOf(epicId)
            when {
                (oldPriority == 0 && newPriority == 1) || (oldPriority == 1 && newPriority == 0) ->
                    epicOrder = mutableListOf(epicOrder[1], epicOrder[0], epicOrder[2])
                (oldPriority == 0 && newPriority == 2) || (oldPriority == 2 && newPriority == 0)->
                    epicOrder = mutableListOf(epicOrder[2], epicOrder[1], epicOrder[0])
                (oldPriority == 1 && newPriority == 2) || (oldPriority == 2 && newPriority == 1) ->
                    epicOrder = mutableListOf(epicOrder[0], epicOrder[2], epicOrder[1])
            }
        }
    }

    /**
     * Liefert Epic mit der angegebenen Nummer
     *
     * @param index Nummer des Epics
     * @return Epic mit Nummer index
     */
    fun getEpic(index: Int): Epic {
        return epics[epicOrder[index]]
    }

    /**
     * Tiefe Kopie für die Sruktur um jeder Gruppe eine eigene Version bereitzustellen
     *
     * @return tiefe Kopie
     */
    fun copy(): Structure {
        return Structure(epics.map { it.copy() })
    }
}

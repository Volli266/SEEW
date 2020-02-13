import org.json.JSONArray
import org.json.JSONObject

/**
 * Es wird über die Schlüssel eines JSON-Objekt iteriert
 * und dabei auf jedes Element die angegebene Methode angewendet
 *
 * @param json JSON-Objekt
 * @param fn Funktion mit der iteriert wird
 * @return Ergebnisliste
 */
fun <T> mapKeys(json: JSONObject, fn: (String) -> T): MutableList<T> {
    val list: MutableList<T> = mutableListOf()
    json.keys().forEach { key ->
        list.add(fn(key))
    }
    return list
}

/**
 * Es wird über ein JSON-Array iteriert und dabei auf jedes Element die
 * angegebene Methode angewendet
 *
 * @param json JSON-Array
 * @param fn Funktion mit der iteriert wird
 * @return Ergebnisliste
 */
fun <T> mapIndices(json: JSONArray, fn: (Int) -> T): MutableList<T> {
    val list: MutableList<T> = mutableListOf()
    for (i in 0 until json.length()) {
        list.add(fn(i))
    }
    return list
}

/**
 * Ein idealerweise mit Integern gefülltes JSON-Array wird
 * in eine List<Int> umgewandelt.
 *
 * @param json JSON-Array
 * @return Liste von Integern
 */
fun toIntList(json: JSONArray): List<Int> {
    val list: MutableList<Int> = mutableListOf()
    for (i in 0 until json.length()) {
        list.add(json.getInt(i))
    }
    return list
}


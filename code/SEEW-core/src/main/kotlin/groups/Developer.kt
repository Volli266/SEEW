package groups

import org.json.JSONObject
import structure.Task
import kotlin.random.Random

/**
 * Klasse Developer legt bei Deklaration die Merkmale zufällig fest. Das Geschlecht dient hierbei nur zur Auswahl
 * der Namen und ist danach nicht weiter relevant. Die Namen werden aus einer Liste und entsprechende
 * Attribute zufällig auf einer Skala von 1 bis 10 generiert.
 * Entsprechend sind Funktionen zum Abrufen sowie Inkrementierten der Variablen vorhanden.
 */
class Developer {

    companion object {
        private var counter: Int = 0
        private val forenames: List<String> = listOf("Julia", "Laura", "Lea", "Sarah", "Lina", "Mila", "Anna",
            "Emilia", "Lena", "Marie", "Sophie", "Maria", "Amelie", "Lara", "Leonie", "Emma", "Mia", "Lisa",
            "Luna", "Luisa", "Vanessa", "Nina", "Hannah", "Ella", "Alina", "Katharina", "Sandra",
            "Nora", "Carolin", "Selma", "Hilde", "Lorena", "Sigrun", "Hedwig", "Leni", "Rose",
            "Pauline", "Mathilda", "Rena", "Merle", "Pia", "Lone", "Melina", "Maya", "Karina",
            "Hermine", "Padma", "Ginny", "Lavender", "Joanne", "Kerstin", "Gudrun", "Minerva", "Dolores", "Elias",
            "Liam", "Alexander", "Jonas", "Linus", "Noah", "Samuel", "Milan", "David",
            "Julian", "Lukas", "Daniel", "Simon", "Emil", "Finn", "Jan", "Arvid", "Mats",
            "Levin", "Karl", "Sebastian", "Ansgar", "Malte", "Dirk", "Elmar", "Tjark", "Karsten",
            "Norwin", "Raik", "Herbert", "Frank", "Siegfried", "Lennard", "Hagen", "Nantwin", "Hugo",
            "Henri", "Lennart", "Adrian", "Dean", "Jonas", "Len", "Horst", "Tyr", "Luis",
            "Harry", "Ron", "Neville", "Ernie", "Justin", "Pascal", "Kevin", "Tom", "Gandulf", "Alonzo", "Haskell",
            "Linus", "Thoralf", "Edsger", "Alan", "John", "Axel", "Heinrich", "Jens", "Hans-Jürgen", "Johannes",
            "Mario", "Jean-Alexander", "Klaus", "Oliver", "Thomas", "Robert", "Uwe", "Schlomo", "Moishe", "Ehud",
            "Avigad", "Golda", "Ariel", "Werner", "Molly", "Ada", "FORTRAN", "ssd", "EJ", "Jakob", "Bastian", "Mariia",
            "Laura", "Lars", "Karl", "Helen", "Tillman", "Alex", "Max", "")
        private val surnames: List<String> = listOf("Meyer", "Oltmanns", "Müller", "Kruse", "Becker", "Eilers",
            "Cordes", "Gerdes", "Janßen", "Frerichs", "Schröder", "Menke", "Bruns",
            "Schwarting", "Ahlers", "Schütte", "Behrens", "Lange",
            "Harms", "Deters", "Schmidt", "Gramberg", "Albers", "Meiners", "Kramer", "Olberding", "Macke",
            "Bergmann", "Ullrich", "Töpfer", "Maler", "Decker", "Potter", "Riddle", "Wichmann", "Hinrichs",
            "Lüschen", "Kröger", "Stöver", "Fortmann", "Wessels", "Borchers", "Diekmann", "Rogge", "Willenborg",
            "Büsing", "Ahrens", "Ostendorf", "Brunken", "Böckmann", "Peters", "Klostermann", "Hollmann", "Stolle",
            "Wilken", "Naber", "Scheper", "Lamping", "Renken", "Hobbie", "Jüchter", "Suhr", "Seibert",
            "Köhler", "Schumacher", "Logemann", "Schneider", "Moormann", "Koch", "Ehler", "Lübben", "Schulte",
            "Drees", "Weber", "Middendorf", "Poppe", "Niemann", "Enneking", "Hausfeld", "Haverkamp", "Neuhaus",
            "Siefken", "Dierke", "Wilke", "Speckmann", "Hespe", "Loseke", "Lampe", "Backhaus", "Sommer",
            "Onken", "Taphorn", "Dreher", "Harbers", "Henkel", "Zunft", "Läufer", "Werner", "Hölzer",
            "Heinke", "Huster", "Spier", "Jahn", "Klein", "Groß", "Fuchs", "Hase", "Krebs", "Insel", "Sischarp",
            "Siplasplas", "Integer", "Curry", "Wahrheit", "Church", "von Neumann", "Lovelace")
    }

    val id = counter++
    var task: Task? = null

    var values: Int = 0

    private var forename = forenames.random()
    private var surname = surnames.random()

    private var properties = IntArray(10) { 0 }

    /**
     * Initialisierung der Eigenschaften
     */
    fun initialize() {
        var i = 0
        var randomize = 0
        while (values >= 0) {
            randomize = Random.nextInt(0, 5)
            if((properties[i] + randomize) < 10) {
                properties[i] += randomize
                values -= randomize
            }
            if (i == 9) {
                i = -1
            }
            i++
        }
    }

    /**
     * @return JSON-Repräsentation des Entwicklers
     */
    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put(JSON.DEVELOPER_ID, id)
        json.put(JSON.DEVELOPER_FORENAME, forename)
        json.put(JSON.DEVELOPER_SURNAME, surname)
        json.put(JSON.DEVELOPER_SOCIAL, properties[0])
        json.put(JSON.DEVELOPER_LOGIC, properties[1])
        json.put(JSON.DEVELOPER_COURAGE, properties[2])
        json.put(JSON.DEVELOPER_REACTION, properties[3])
        json.put(JSON.DEVELOPER_AGILITY, properties[4])
        json.put(JSON.DEVELOPER_DEXTERITY, properties[5])
        json.put(JSON.DEVELOPER_STRENGTH, properties[6])
        json.put(JSON.DEVELOPER_WEIGHT, properties[7])
        json.put(JSON.DEVELOPER_SIZE, properties[8])
        json.put(JSON.DEVELOPER_BODY, properties[9])

        return json
    }

    /**
     *  Getter-Methoden für Eigenschaften
     */
    fun getSocial(): Int {
        return properties[0]
    }
    fun getLogic(): Int {
        return properties[1]
    }
    fun getCourage(): Int {
        return properties[2]
    }
    fun getReaction(): Int {
        return properties[3]
    }
    fun getAgility(): Int {
        return properties[4]
    }
    fun getDexterity(): Int {
        return properties[5]
    }
    fun getStrength(): Int {
        return properties[6]
    }
    fun getWeight(): Int {
        return properties[7]
    }
    fun getSize(): Int {
        return properties[8]
    }
    fun getBody(): Int {
        return properties[9]
    }

    /**
     * Increase-Methoden für Eigenschaften
     */
    fun incSafe(prop: Int, value: Int) {
        if (properties[prop] + value <= 10) {
            properties[prop] += value
        }
    }
    fun incSocial(value: Int) {
        incSafe(0, value)
    }
    fun incLogic(value: Int) {
        incSafe(1, value)
    }
    fun incCourage(value: Int) {
        incSafe(2, value)
    }
    fun incReaction(value: Int) {
        incSafe(3, value)
    }
    fun incAgility(value: Int) {
        incSafe(4, value)
    }
    fun incDexterity(value: Int) {
        incSafe(5, value)
    }
    fun incStrength(value: Int) {
        incSafe(6, value)
    }
    fun incWeight(value: Int) {
        incSafe(7, value)
    }
    fun incSize(value: Int) {
        incSafe(8, value)
    }
    fun incBody(value: Int) {
        incSafe(9, value)
    }
}

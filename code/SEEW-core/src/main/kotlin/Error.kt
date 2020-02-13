import org.json.JSONObject

/**
 * Klasse, die mögliche Fehler mit entsprechendem Fehlertext zur Bildschirmausgabe hält
 */
enum class Error(private val message: String) {

    GROUP_NAME_TAKEN("Gruppenname ist schon vergeben!"),
    GROUP_COLOR_TAKEN("Dinofarbe ist schon vergeben!"),
    GROUP_COMPLETE("Gruppe ist bereits voll! - maximal 4 Mitglieder"),
    GROUP_INCOMPLETE("Gruppe braucht mindestens 3 Mitglieder"),
    GROUP_STARTED("Gruppe befindet sich bereits in der Simulation"),
    USER_NAME_TAKEN("Benutzername ist bereits vergeben!"),
    LOGIN_FAILED("Login fehlgeschlagen!"),
    LOGIN_EMPTY("Username oder Passwort fehlt!"),
    NOT_IN_GROUP("Du bist in keiner Gruppe"),
    NOT_READY("Andere Mitglieder deiner Gruppe sind noch nicht bereit!"),
    NOT_ALL_CONFIRMED("Alle Mitglieder deiner Gruppe müssen diese Phase abschließen"),
    ESTIMATION_REJECTION("Bitte noch mal schätzen!! Die Werte liegen zu weit auseinander!!"),
    STORIES_NOT_CHOSEN("Wähle aus jedem Pool eine Story!"),
    NOT_ESTIMATED("Bitte die Stories schätzen!"),
    NOT_REVIEWED("Bitte erst Review durchführen!"),
    NOT_REVIEW_CONFIRMED("Alle Mitglieder deiner Gruppe müssen Review durchführen drücken."),
    NO_DEVELOPER("Der Aufgabe muss ein Entwickler zugewiesen werden!"),
    DAILY_NOT_CONFIRMED("Alle Mitglieder deiner Gruppe müssen dem Daily zustimmen"),
    NO_ERROR("");

    /**
     * Methode, die Fehlermeldung in JSON-Objekt umwandelt
     */
    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put(JSON.ERROR, this.message)
        return json
    }
}

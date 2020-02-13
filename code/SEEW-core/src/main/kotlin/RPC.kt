import connection.SocketConnection
import connection.SocketMessage
import groups.Group
import groups.User
import org.json.JSONObject

/**
 * Objekt zur Verwaltung von RPC-Funktionen (remote procedure calls). Die Funktionen
 * werden in eine Map eingetragen. Mögliche Strings als Schlüssel sind in JSON.kt
 * hinterlegt. Die RPC-Funktionen sind alle vom Typ
 * <code>suspend (User, JSONObject) -> Unit</code>
 */
object RPC {

    // Map von aufrufbaren Funktionen
    private val functions: MutableMap<String, suspend (User, JSONObject) -> Unit> = mutableMapOf()

    private val users: MutableList<User> = mutableListOf()

    /**
     * Methode, um eine Funktion in die functions-Map einzutragen.
     *
     * @param   fnName  String als Bezeichnung der Funktion, mögliche Werte
     *                  sind in JSON.kt hinterlegt.
     * @param   fn      Funktion vom Typ <code>suspend (User, JSONObject) -> Unit</code>
     *                  die eingetragen wird, um per RPC ausgeführt zu werden
     */
    fun register(fnName: String, fn: suspend (User, JSONObject) -> Unit) {
        functions[fnName] = fn
    }

    /**
     * Methode, um aus einen functions-Eintrag (eine Funktion) ein JSON-Objekt
     * zu bilden und dieses (für die <code>connection.send(msg: String)</code>-Methode
     * in einen String umzuwandeln.
     *
     * @param   fnName  Name der Funktion, mögliche Werte in <code>JSON.kt</code>
     * @param   params  Parameter der Funktion als JSON-Objekt
     * @return  das in einen String umgewandelte, zu sendende JSON-Objekt
     */
    fun makeMessage(fnName: String, params: JSONObject): String {
        val msg = JSONObject()
        msg.put(JSON.FUNCTION, fnName)
        msg.put(JSON.PARAMS, params)
        return msg.toString()
    }

    /**
     * Methode, um RP-Call bei einem User zu starten
     *
     * @param   fnName  Name der Funktion, mögliche Werte in <code>JSON.kt</code>
     * @param   params  Parameter der Funktion als JSON-Objekt
     * @param   user    User, bei dem der RP-Call erfolgen soll
     */
    suspend fun callUser(fnName: String, params: JSONObject, user: User) {
        user.send(makeMessage(fnName, params))
    }
    /**
     * Methode, um RP-Call bei einer Gruppe (Group) zu starten
     *
     * @param   fnName  Name der Funktion, mögliche Werte in <code>JSON.kt</code>
     * @param   params  Parameter der Funktion als JSON-Objekt
     * @param   group   Gruppe, bei deren Mitgliedern der RP-Call erfolgen soll
     */
    suspend fun callGroup(fnName: String, params: JSONObject, group: Group) {
        group.send(makeMessage(fnName, params))
    }
    /**
     * Methode, um RP-Call bei allen Usern zu starten
     *
     * @param   fnName  Name der Funktion, mögliche Werte in <code>JSON.kt</code>
     * @param   params  Parameter der Funktion als JSON-Objekt
     * @param   users   Liste mit allen im Spiel registrierten User
     */
    suspend fun callAllUsers(fnName: String, params: JSONObject) {
        users.forEach { it.send(makeMessage(fnName, params)) }
    }

    /**
     * Es wird geprüft ob ein Benutzer mit passendem Benutzernamen und Passwort existiert.
     * Falls ja wird dieser Benutzer mit der WebSocket-Verbindung assoziiert.
     *
     * @param username Bei Login angegebener Nutzername
     * @param password Bei Login angegebenes Passwort
     * @param connection WebSocket-Verbindung mit der username und password geschickt wurden
     */
    private suspend fun login(username: String, password: String, connection: SocketConnection) {
        val user = users.find { it.username == username && it.password == password }
        if (username.isEmpty() || password.isEmpty()) {
            val msg = makeMessage(JSON.NOTIFY_LOGIN, Error.LOGIN_EMPTY.toJSON())
            connection.sendMessage(SocketMessage.Text(msg, connection))
        } else if (user != null) {
            user.connection = connection
            if (user.group?.getStartedSimulation() == true ) {
                callUser(JSON.ALREADY_STARTED, JSONObject(), user)
            } else {
                callUser(JSON.CONFIRM_LOGIN, JSONObject(), user)
            }
        } else {
            val msg = makeMessage(JSON.NOTIFY_LOGIN, Error.LOGIN_FAILED.toJSON())
            connection.sendMessage(SocketMessage.Text(msg, connection))
        }
    }

    /**
     * Es wird ein neuer Benutzer mit angegebenenm Benutzernamen und Passwort und der
     * zugehöreigen Websocket-Verbindung angelegt.
     *
     * @param username Benutzername
     * @param passwort Passwort
     * @param connection Websocket-Verbindung
     */
    private suspend fun registerUser(username: String, password: String, connection: SocketConnection) {
        if (username.isEmpty() || password.isEmpty()) {
            val msg = makeMessage(JSON.NOTIFY_LOGIN, Error.LOGIN_EMPTY.toJSON())
            connection.sendMessage(SocketMessage.Text(msg, connection))
        } else if (users.all { it.username != username }) {
            val user = User(username, password, connection)
            users.add(user)
            callUser(JSON.CONFIRM_LOGIN, JSONObject(), user)
        } else {
            val msg = makeMessage(JSON.NOTIFY_LOGIN, Error.USER_NAME_TAKEN.toJSON())
            connection.sendMessage(SocketMessage.Text(msg, connection))
        }
    }

    /**
     * Prüft ob die eingehende Nachricht einem Nutzer zugeordnet werden kann. Falls
     * ja wird die zugehörige Funktion aufgerufen. Falls es sich um eine Einlogg- oder
     * Registriernachricht handelt wird der User eingeloggt oder registriert. Andernfalls
     * wird eine Nachricht zurückgeschickt die den Client zur Login Seite zurückkehren lässt.
     *
     * @param msg eingehende Nachricht
     */
    suspend fun receive(msg: SocketMessage.Text) {
        val json = JSONObject(msg.content)
        if (json.has(JSON.FUNCTION) && json.has(JSON.PARAMS) && json.has(JSON.USERNAME) && json.has(JSON.PASSWORD)) {
            val fnName = json.getString(JSON.FUNCTION)
            val username = if (json.isNull(JSON.USERNAME)) "" else json.getString(JSON.USERNAME)
            val password = if (json.isNull(JSON.PASSWORD)) "" else json.getString(JSON.PASSWORD)
            if (fnName == JSON.LOGIN) {
                login(username, password, msg.connection)
            } else if (fnName == JSON.REGISTER) {
                registerUser(username, password, msg.connection)
            } else {
                val user = users.find { it.username == username && it.password == password }
                if (user != null) {
                    user.connection = msg.connection
                    val params = if (json.isNull(JSON.PARAMS)) JSONObject() else json.getJSONObject(JSON.PARAMS)
                    functions[fnName]?.invoke(user, params)
                } else {
                    val answer = makeMessage(JSON.RETURN_TO_LOGIN, JSONObject())
                    msg.connection.sendMessage(SocketMessage.Text(answer, msg.connection))
                }
            }
        }
    }
}

package groups

import connection.SocketConnection
import connection.SocketMessage
import org.json.JSONObject
import SeewService

/**
 * "User" ist die Klasse, die die Daten eines Spielers hält. Der Konstruktor
 *  wird aufgerufen von SEEWService.newUser(params: JSONObject, connection: SocketConnection).
 *  Die Gruppenzugehörigkeit wird erst durch Beitritt zu einer Gruppe gesetzt.
 *  @see SeewService#newUser(params: JSONObject, connection: SocketConnection)
 */

class User(
    val username: String,
    val password: String,
    var connection: SocketConnection) {

    var group: Group? = null

    var developerOne = Developer()
    var developerTwo = Developer()

    var readyForIntro: Boolean = false
    var readyForSimulation: Boolean = false

    var estimationValueStoryOne: Int = 0
    var estimationValueStoryTwo: Int = 0
    var hasEstimatedStoryOne: Boolean = false
    var hasEstimatedStoryTwo: Boolean = false

    var hasConfirmedPhase: Boolean = false
    var hasConfirmedReview: Boolean = false
    var hasConfirmedDaily: Boolean = false

    /**
     *  Methode um einem User per Socket-Connection einen String zu übermitteln
     *
     *  @param msg zu übermittelnder Text
     */
    suspend fun send(msg: String) {
        connection.sendMessage(SocketMessage.Text(msg, connection))
    }

    /**
     *  Methode, um den Nutzer in ein JSON-Objekt umzuwandeln
     */
    fun toJSON(): JSONObject {
        val json = JSONObject()
        json.put(JSON.USERNAME, username)
        json.put(JSON.DEVELOPER_ONE, developerOne.toJSON())
        json.put(JSON.DEVELOPER_TWO, developerTwo.toJSON())
        return json
    }

}

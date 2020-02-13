package connection

interface MessageService {

    suspend fun sendMessage(socketMessage: SocketMessage)

}
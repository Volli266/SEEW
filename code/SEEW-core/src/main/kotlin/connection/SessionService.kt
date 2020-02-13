package connection

interface SessionService {
    fun sendMessage(socketMessage: SocketMessage)
}
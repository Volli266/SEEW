package connection

interface ServerListener {
    suspend fun onClientConnected(connection: SocketConnection)
    suspend fun onMessageReceived(message: SocketMessage)
}
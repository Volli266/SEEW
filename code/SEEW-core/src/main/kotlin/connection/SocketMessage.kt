package connection

sealed class SocketMessage(
    val connection: SocketConnection) {

    val timestamp = System.currentTimeMillis()

    class Binary(val bytes: ByteArray, connection: SocketConnection): SocketMessage(connection)

    class Text(val content: String, connection: SocketConnection): SocketMessage(connection)
}

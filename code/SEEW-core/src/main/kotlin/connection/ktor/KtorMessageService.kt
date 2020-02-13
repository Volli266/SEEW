package connection.ktor

import connection.MessageService
import connection.SocketMessage
import io.ktor.http.cio.websocket.Frame
import io.ktor.websocket.WebSocketServerSession
import kotlinx.coroutines.isActive
import java.nio.ByteBuffer

class KtorMessageService(private val session: WebSocketServerSession) : MessageService {

    override suspend fun sendMessage(socketMessage: SocketMessage) {
        if (session.isActive) {
            when (socketMessage) {
                is SocketMessage.Binary -> session.outgoing.send(
                    Frame.Binary(
                        true,
                        ByteBuffer.wrap(socketMessage.bytes)
                    )
                )
                is SocketMessage.Text -> session.outgoing.send((Frame.Text(socketMessage.content)))
            }
        }
    }

}
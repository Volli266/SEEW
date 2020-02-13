package connection.ktor

import connection.SeewServer
import connection.ServerListener
import connection.SocketConnection
import connection.SocketMessage
import connection.config.ServerConfig
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readBytes
import io.ktor.http.cio.websocket.readText
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.response.respondFile
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import java.io.File
import java.time.Duration

class KtorServer: SeewServer {

    companion object {
        const val DEFAULT_PING_PERIOD_MINUTES = 1L
    }

    override fun start(config: ServerConfig, serverListener: ServerListener) {
        embeddedServer(Netty, port = config.port, module = {
            install(WebSockets) {
                pingPeriod = Duration.ofMinutes(DEFAULT_PING_PERIOD_MINUTES)
            }

            routing {

                /**
                 * Bei Websocket Anfragen auf Pfad "/ws" wird eine Websocketverbindug hergestellt und ein User
                 * hinzugefügt. Sobald der User eine Nachricht schickt die Text enthält wird receive aufgerufen
                 */
                webSocket(config.wsPath) {
                    val session = SocketConnection(KtorMessageService(this))
                    serverListener.onClientConnected(session)
                    while (true) {
                        when (val frame = incoming.receive()) {
                            is Frame.Text -> serverListener.onMessageReceived(SocketMessage.Text(frame.readText(), session))
                            is Frame.Binary -> serverListener.onMessageReceived(SocketMessage.Binary(frame.readBytes(), session))
                        }
                    }
                }
            }
        }).start(wait = true)
    }
}
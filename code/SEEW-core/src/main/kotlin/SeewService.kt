import connection.ServerListener
import connection.SocketConnection
import connection.SocketMessage
import connection.config.ServerConfig
import groups.GroupService
import java.lang.RuntimeException

/**
 * Kotlin-Objekt, das Implementationen zum Start des Servers enthält
 */
object SeewService: ServerListener {

    private lateinit var seewServiceConfig: SeewServiceConfig

    fun initialize(seewServiceConfig: SeewServiceConfig) {
        this.seewServiceConfig = seewServiceConfig
        GroupService.initialize()
    }

    fun start(config: ServerConfig) {
        checkInitialized()
        seewServiceConfig.seewServer.start(config, this)
    }

    private fun checkInitialized() {
        if (::seewServiceConfig.isInitialized.not()) {
            throw NotInitializedException()
        }
    }

    override suspend fun onClientConnected(connection: SocketConnection) {}

    override suspend fun onMessageReceived(message: SocketMessage) {
        if (message is SocketMessage.Text) {
            RPC.receive(message)
        }
    }

    class NotInitializedException: RuntimeException("SeewService ist nicht initialisiert. Vor dem Aufruf von Methode muss SeewService.initialize(...) aufgerufen werden")
}

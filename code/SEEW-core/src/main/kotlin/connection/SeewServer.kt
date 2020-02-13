package connection

import connection.config.ServerConfig

interface SeewServer {
    fun start(config: ServerConfig, serverListener: ServerListener)
}
package connection.config

interface ServerConfigProvider {
    fun config(): ServerConfig?
}
package connection.config

data class ServerConfig(val port: Int = DEFAULT_PORT, val wsPath: String = DEFAULT_WS_PATH) {
    companion object {
        const val DEFAULT_WS_PATH = "/ws"
        const val CONFIG_PORT_NAME = "port"
        const val DEFAULT_PORT = 8080
    }
}
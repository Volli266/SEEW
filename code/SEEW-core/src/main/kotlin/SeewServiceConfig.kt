import connection.SeewServer

class SeewServiceConfig(val seewServer: SeewServer) {

    class Builder(private val seewServer: SeewServer) {

        fun build(): SeewServiceConfig {
            return SeewServiceConfig(seewServer)
        }

    }
}
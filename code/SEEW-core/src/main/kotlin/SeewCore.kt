import cli.DefaultCliParser
import connection.config.*
import connection.ktor.KtorServer

/**
 * Startet den SeewService mit einer ServerConfig. Wenn keine Argumente übergeben wurden, so wird die Config aus dem
 * Config-File geladen. Falls Config-Argumente enthalten sind, so wird stattdessen eine ServerConfig aus den vorhandenen
 * Argumenten erstellt.
 *
 * @see DefaultCliParser
 */
fun main(args: Array<String>) {
    val serverConfig = createServerConfig(args)
    serverConfig?.let {config ->
        SeewService.apply {
            initialize(SeewServiceConfig.Builder(KtorServer())
                .build())
            start(config)
        }
        SeewService.start(serverConfig)
    }?:exitNoConfig()
}

private fun createServerConfig(args: Array<String>): ServerConfig? {
    return CliPortRelatedServerConfigProvider(DefaultCliParser(), args).config()
}

private fun exitNoConfig() {
    // todo: log failure
    System.exit(1)
}




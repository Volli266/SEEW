package connection.config

import cli.CliParser
import org.apache.commons.cli.CommandLine

/**
 * Proxy-ServerConfig die eine ServerConfig erstellt, sobald der Parameter -p enthalten ist. Ist keiner enthalten, so
 * wird stattdessen eine ServerConfig aus der Config-Datei geladen.
 */
class CliPortRelatedServerConfigProvider(
    private val cliParser: CliParser,
    private val programArgs: Array<String>) : ServerConfigProvider {

    override fun config(): ServerConfig? {
        val commandLine = cliParser.createCommandLine(programArgs)
        val serverConfigProvider: ServerConfigProvider = if (cliParser.cliServerConfigAvailable(commandLine)) {
            CliServerConfigProvider(commandLine)
        } else {
            FileServerConfigProvider()
        }
        return serverConfigProvider.config()
    }
}
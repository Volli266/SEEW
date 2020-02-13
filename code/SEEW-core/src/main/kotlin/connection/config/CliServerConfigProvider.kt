package connection.config

import cli.CliParser
import org.apache.commons.cli.CommandLine

/**
 * Lädt die Config aus den Parametern die über die CLI beim Start des Servers angegeben wurden. Falls ein Fehler beim
 * Parse der Config auftritt wird stattdessen die ServerConfig im default zurückgegeben.
 */
class CliServerConfigProvider(val commandLine: CommandLine): ServerConfigProvider {

    override fun config(): ServerConfig? {
        return if (containsPortOption()) {
            serverConfigFromCli()
        } else {
            null
        }
    }

    private fun serverConfigFromCli(): ServerConfig {
        return ServerConfig(portFromCliOption())
    }

    private fun portFromCliOption(): Int {
        return commandLine.getOptionValue(CliParser.SHORT_OPTION_PORT).toInt()
    }

    fun containsPortOption(): Boolean {
        return if (commandLine.hasOption(CliParser.SHORT_OPTION_PORT)) {
            return try {
                portFromCliOption()
                true
            } catch (nfe: NumberFormatException) {
                false
            }
        } else {
            false
        }
    }
}
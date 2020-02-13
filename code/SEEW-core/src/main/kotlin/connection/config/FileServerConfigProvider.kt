package connection.config

import java.io.File
import java.lang.NumberFormatException

/**
 * Lädt die Config aus der Datei aus dem angegebenen Pfad. Falls ein Fehler beim Laden der Config auftritt wird stattdess
 * die ServerConfig im default zurückgegeben.
 *
 * Dateien in einer Config werden als einfach key-values Paare erwartet. Ungültige Einträge werden nicht ausgewertet.
 */
class FileServerConfigProvider(val configPath: String = "$DEFAULT_CONFIG_PATH${File.separator}$DEFAULT_CONFIG_FILE_NAME"):
    ServerConfigProvider {

    companion object {
        const val ENTRY_SEPERATOR = "="
        const val CONFIG_ENTRY_PATTERN = "[a-zA-Z0-9]*$ENTRY_SEPERATOR[a-zA-Z0-9]*"

        const val USER_DIR_PROPERTY = "user.dir"
        const val DEFAULT_CONFIG_FILE_NAME = "server.conf"
        val DEFAULT_CONFIG_PATH = System.getProperty(USER_DIR_PROPERTY)

    }

    override fun config(): ServerConfig? {
        val configFile = File(configPath)
        return if (isConfigFileAvailable(configFile)) {
            serverConfigFromFile(configFile)
        } else {
            null
        }
    }

    private fun isConfigFileAvailable(configFile: File): Boolean {
        return configFile.exists() && configFile.isDirectory.not() && configFile.length() > 0
    }

    private fun serverConfigFromFile(configFile: File): ServerConfig? {
        val configEntries = parseConfigLines(configFile.readLines())
        val portConfig = configEntries.firstOrNull { it.key == ServerConfig.CONFIG_PORT_NAME }
        return portConfig?.let {
            return try {
                ServerConfig(it.value.toInt())
            } catch (nfe: NumberFormatException) {
                null
            }
        }
    }

    private fun parseConfigLines(lines: List<String>): List<ConfigEntry> {
        return lines.filter { isValidConfigEntry(it) }.map { entryFromLine(it) }
    }

    private fun isValidConfigEntry(line: String): Boolean {
        return line.matches(CONFIG_ENTRY_PATTERN.toRegex())
    }

    private fun entryFromLine(line: String): ConfigEntry {
        val splitedLine = line.split(ENTRY_SEPERATOR)
        return ConfigEntry(splitedLine[0], splitedLine[1])
    }

    inner class ConfigEntry(val key: String, val value: String)
}
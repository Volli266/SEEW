package cli

import org.apache.commons.cli.CommandLine

interface CliParser {

    companion object {
        val SHORT_OPTION_PORT = "p"
    }

    fun cliServerConfigAvailable(commandLine: CommandLine): Boolean

    fun createCommandLine(args: Array<String>): CommandLine
}
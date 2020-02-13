package cli

import cli.CliParser
import connection.config.CliServerConfigProvider
import org.apache.commons.cli.*

class DefaultCliParser: CliParser {

    override fun cliServerConfigAvailable(commandLine: CommandLine): Boolean {
        return CliServerConfigProvider(commandLine).containsPortOption()
    }

    override fun createCommandLine(args: Array<String>): CommandLine {
        val options = Options()
        options.addOption(Option.builder(CliParser.SHORT_OPTION_PORT)
            .hasArg()
            .build())
        val parser = DefaultParser()
        return try {
            parser.parse(options, args)
        } catch (pe: ParseException) {
            parser.parse(Options(), args)
        }
    }

}
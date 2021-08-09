package org.cascadebot.slashcommandstest

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import org.cascadebot.slashcommandstest.commandmeta.CommandManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.system.exitProcess

object SlashCommandsTest {

    val LOG: Logger = LoggerFactory.getLogger("SlashCommandsTest");
    private val commandManager = CommandManager()

    fun run() {
        val configFile = File("config.yml")
        if (!configFile.exists()) {
            LOG.error("No config file found!")
            exitProcess(1)
        }

        Config.init(configFile)
    }

    fun pushCommands() {
        for (command in commandManager.commands) {
            val data = command.commandData
            for (subCommand in command.subCommands) {// TODO account for sub command groups
                data.subcommands
            }
        }
    }

}

fun main(args: Array<String>) {
    val parser = ArgParser("example")

    val updateCommands by parser.option(
        ArgType.Boolean,
        fullName = "update-commands",
        shortName = "u",
        description = "Attempts to update slash commands with Discord then exits"
    )

    parser.parse(args)

    if (updateCommands == true) {
        SlashCommandsTest.pushCommands()
    } else {
        SlashCommandsTest.run()
    }
}
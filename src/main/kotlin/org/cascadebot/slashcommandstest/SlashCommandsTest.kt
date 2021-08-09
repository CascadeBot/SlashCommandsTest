package org.cascadebot.slashcommandstest

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData
import org.cascadebot.slashcommandstest.commandmeta.CommandManager
import org.cascadebot.slashcommandstest.commandmeta.ExecutableRootCommand
import org.cascadebot.slashcommandstest.commandmeta.ParentCommand
import org.cascadebot.slashcommandstest.commandmeta.SubCommand
import org.cascadebot.slashcommandstest.commandmeta.SubCommandGroup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.function.Consumer
import kotlin.system.exitProcess

object SlashCommandsTest {

    val LOG: Logger = LoggerFactory.getLogger("SlashCommandsTest");
    private val commandManager = CommandManager()

    fun run() {

    }

    fun pushCommands() {
        initConfig()
        val jda: JDA = JDABuilder.createLight(Config.INS?.botToken).build()

        val subCommands: MutableMap<ParentCommand, MutableList<SubCommand>> = mutableMapOf()
        val subCommandGroups: MutableMap<ParentCommand, MutableList<SubCommandGroup>> = mutableMapOf()
        val subCommandsOfGroup: MutableMap<SubCommandGroup, MutableList<SubCommand>> = mutableMapOf()

        for (command in commandManager.commands) {
            when (command) {
                is SubCommand -> {
                    if (command.group != null) {
                        if (!subCommandGroups.contains(command.parent)) {
                            subCommandGroups[command.parent] = mutableListOf(command.group)
                        } else {
                            if (!subCommandGroups[command.parent]?.contains(command.group)!!) {
                                subCommandGroups[command.parent]?.add(command.group)
                            }
                        }
                        if (!subCommandsOfGroup.contains(command.group)) {
                            subCommandsOfGroup[command.group] = mutableListOf(command)
                        } else {
                            if (!subCommandsOfGroup[command.group]?.contains(command)!!) {
                                subCommandsOfGroup[command.group]?.add(command)
                            }
                        }
                    } else {
                        if (!subCommands.contains(command.parent)) {
                            subCommands[command.parent] = mutableListOf(command)
                        } else {
                            subCommands[command.parent]?.add(command)
                        }
                    }
                }
                is ExecutableRootCommand -> {
                    LOG.info("Upserting command " + command.command)
                    jda.upsertCommand(command.commandData).queue { LOG.info("Command " + it.name + " upserted with id " + it.id) } // TODO store these ids somewhere?
                }
            }
        }

        for (parentCommand in ParentCommand.values()) {
            LOG.info("Upserting command " + parentCommand.command)
            val data = CommandData(parentCommand.command, parentCommand.description)
            if (subCommands.contains(parentCommand)) {
                for (subCommand in subCommands[parentCommand]!!) {
                    LOG.info("\tWith sub command " + subCommand.command)
                    data.addSubcommands(subCommand.commandData)
                }
            }
            if (subCommandGroups.contains(parentCommand)) {
                for (subCommandGroup in subCommandGroups[parentCommand]!!) {
                    LOG.info("\tWith sub command group " + subCommandGroup.groupName)
                    val groupData = SubcommandGroupData(subCommandGroup.groupName, subCommandGroup.description)
                    for (subCommand in subCommandsOfGroup[subCommandGroup]!!) {
                        LOG.info("\t\tWith sub command " + subCommand.command)
                        groupData.addSubcommands(subCommand.commandData)
                    }
                    data.addSubcommandGroups(groupData)
                }
            }
            jda.upsertCommand(data).queue { LOG.info("Command " + it.name + " upserted with id " + it.id) } // TODO store these ids somewhere?
        }

        LOG.info("All commands queued for upsert")
        // TODO exit when jda is done
    }

    fun initConfig() {
        val configFile = File("config.yml")
        if (!configFile.exists()) {
            LOG.error("No config file found!")
            exitProcess(1)
        }

        Config.init(configFile)
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
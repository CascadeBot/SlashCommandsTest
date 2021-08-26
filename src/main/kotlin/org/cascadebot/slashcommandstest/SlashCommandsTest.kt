package org.cascadebot.slashcommandstest

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import org.cascadebot.slashcommandstest.commandmeta.CommandManager
import org.cascadebot.slashcommandstest.commandmeta.ExecutableRootCommand
import org.cascadebot.slashcommandstest.commandmeta.ParentCommand
import org.cascadebot.slashcommandstest.commandmeta.SubCommand
import org.cascadebot.slashcommandstest.commandmeta.SubCommandGroup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.system.exitProcess

object SlashCommandsTest {

    val LOG: Logger = LoggerFactory.getLogger("SlashCommandsTest");
    val commandManager = CommandManager()

    fun run() {
        initConfig()
        val defaultShardManagerBuilder = DefaultShardManagerBuilder.create(GatewayIntent.getIntents(GatewayIntent.DEFAULT))
            .addEventListeners(EventListener())
            .setToken(Config.INS!!.botToken)
            .setShardsTotal(-1)
            .setActivityProvider { Activity.playing("Testing slash command") }
            .setBulkDeleteSplittingEnabled(false)

        defaultShardManagerBuilder.build()
    }

    private fun buildSubCommands(command: SubCommand, subCommands: MutableMap<ParentCommand, MutableList<SubCommand>>, subCommandGroups: MutableMap<ParentCommand, MutableList<SubCommandGroup>>, subCommandsOfGroup: MutableMap<SubCommandGroup, MutableList<SubCommand>>) {
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

    private fun upsertSubCommands(jda: JDA, parentCommand: ParentCommand, subCommands: MutableMap<ParentCommand, MutableList<SubCommand>>, subCommandGroups: MutableMap<ParentCommand, MutableList<SubCommandGroup>>, subCommandsOfGroup: MutableMap<SubCommandGroup, MutableList<SubCommand>>) {
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

    fun updateCommands() {
        initConfig()
        val jda: JDA = JDABuilder.createLight(Config.INS?.botToken).build()

        val subCommands: MutableMap<ParentCommand, MutableList<SubCommand>> = mutableMapOf()
        val subCommandGroups: MutableMap<ParentCommand, MutableList<SubCommandGroup>> = mutableMapOf()
        val subCommandsOfGroup: MutableMap<SubCommandGroup, MutableList<SubCommand>> = mutableMapOf()

        for (command in commandManager.commands) {
            when (command) {
                is SubCommand -> {
                    buildSubCommands(command, subCommands, subCommandGroups, subCommandsOfGroup)
                }
                is ExecutableRootCommand -> {
                    LOG.info("Upserting command " + command.command)
                    jda.upsertCommand(command.commandData).queue { LOG.info("Command " + it.name + " upserted with id " + it.id) } // TODO store these ids somewhere?
                }
            }
        }

        for (parentCommand in ParentCommand.values()) {
            upsertSubCommands(jda, parentCommand, subCommands, subCommandGroups, subCommandsOfGroup)
        }

        LOG.info("All commands queued for upsert")
        // TODO exit when jda is done, maybe don't use jda?
    }

    fun pushCommands() {
        initConfig()
        val jda: JDA = JDABuilder.createLight(Config.INS?.botToken).build()

        val subCommands: MutableMap<ParentCommand, MutableList<SubCommand>> = mutableMapOf()
        val subCommandGroups: MutableMap<ParentCommand, MutableList<SubCommandGroup>> = mutableMapOf()
        val subCommandsOfGroup: MutableMap<SubCommandGroup, MutableList<SubCommand>> = mutableMapOf()

        jda.retrieveCommands().queue {
            for (command in commandManager.commands) {
                when(command) {
                    is SubCommand -> {
                        buildSubCommands(command, subCommands, subCommandGroups, subCommandsOfGroup)
                    }
                    is ExecutableRootCommand -> {
                        if (!it.map { com -> com.name }.contains(command.command)) {
                            LOG.info("Upserting command " + command.command)
                            jda.upsertCommand(command.commandData).queue { LOG.info("Command " + it.name + " upserted with id " + it.id) } // TODO store these ids somewhere?
                        }
                    }
                }
            }

            for (parentCommand in ParentCommand.values()) {
                if (!it.map { com -> com.name }.contains(parentCommand.command)) {
                    upsertSubCommands(jda, parentCommand, subCommands, subCommandGroups, subCommandsOfGroup)
                }
            }

            LOG.info("All commands queued for upsert")
            // TODO exit when jda is done, maybe don't use jda?
        }
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

    val pushCommands by parser.option(
        ArgType.Boolean,
        fullName = "push-commands",
        shortName = "p",
        description = "Attempts to push new slash commands to Discord then exits"
    )

    parser.parse(args)

    var exit = false
    if (updateCommands == true) {
        exit = true;
        SlashCommandsTest.updateCommands()
    }

    if (pushCommands == true) {
        exit = true;
        SlashCommandsTest.pushCommands()
    }

    if (!exit) {
        SlashCommandsTest.run()
    }

}
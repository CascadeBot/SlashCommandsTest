package org.cascadebot.slashcommandstest

import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import org.cascadebot.slashcommandstest.commandmeta.CommandArgs
import org.cascadebot.slashcommandstest.commandmeta.CommandContext
import org.cascadebot.slashcommandstest.commandmeta.CommandPath
import org.cascadebot.slashcommandstest.commandmeta.ExecutableCommand
import org.cascadebot.slashcommandstest.commandmeta.SubCommand

class EventListener : ListenerAdapter() {

    override fun onReady(event: ReadyEvent) {
        SlashCommandsTest.getClient()!!.retrieveCommands().queue{
            for (command in SlashCommandsTest.commandManager.commands.map { it.first }) {
                for (discordCom in it) {
                    if (discordCom.subcommandGroups.size > 0) {
                        for (subGroup in discordCom.subcommandGroups) {
                            for (subComm in subGroup.subcommands) {
                                val path = listOf(discordCom.name, subGroup.name, subComm.name)
                                if (command == CommandPath(0, path)) {
                                    command.rootId = discordCom.idLong
                                }
                            }
                        }
                    } else if (discordCom.subcommands.size > 0) {
                        for (subComm in discordCom.subcommands) {
                            val path = listOf(discordCom.name, subComm.name)
                            if (command == CommandPath(0, path)) {
                                command.rootId = discordCom.idLong
                            }
                        }
                    } else {
                        val path = listOf(discordCom.name)
                        if (command == CommandPath(0, path)) {
                            command.rootId = discordCom.idLong
                        }
                    }
                }
            }
        }
    }

    override fun onSlashCommand(event: SlashCommandEvent) {
        val args = CommandArgs(event.options.groupBy { it.name })

        val command = SlashCommandsTest.commandManager.getCommand(event.commandIdLong, event.commandPath) ?: return // TODO reply with some sort of message saying the command doesn't exist

        event.deferReply()
        command.onCommand(CommandContext(event), args, Object())
    }

}
package org.cascadebot.slashcommandstest

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import org.cascadebot.slashcommandstest.commandmeta.CommandArgs
import org.cascadebot.slashcommandstest.commandmeta.CommandContext
import org.cascadebot.slashcommandstest.commandmeta.ExecutableCommand
import org.cascadebot.slashcommandstest.commandmeta.SubCommand

class EventListener : ListenerAdapter() {

    override fun onSlashCommand(event: SlashCommandEvent) {
        val args = CommandArgs(event.options.groupBy { it.name })

        val command = SlashCommandsTest.commandManager.getCommand(event.commandIdLong, event.commandPath) ?: return // TODO reply with some sort of message saying the command doesn't exist

        event.deferReply()
        command.onCommand(CommandContext(event), args, Object())
    }

}
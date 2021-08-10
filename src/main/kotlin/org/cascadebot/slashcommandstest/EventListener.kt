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

        val path = event.commandPath
        val command = getCommand(path) ?: return

        event.deferReply()
        command.onCommand(CommandContext(event), args, Object())
    }

    fun getCommand(path: String): ExecutableCommand? {
        val parts = path.split("/")
        var command: ExecutableCommand? = null;
        if (parts.size == 1) {
            command = SlashCommandsTest.commandManager.commands.first { it.command == parts[0] }
        } else if (parts.size == 2) {
            command = SlashCommandsTest.commandManager.commands.first { it is SubCommand && it.command == parts[1] && it.parent.command == parts[0] }
        } else if (parts.size == 3) {
            command = SlashCommandsTest.commandManager.commands.first { it is SubCommand && it.command == parts[2] && it.group != null && it.group.groupName == parts[1] && it.parent.command == parts[0] }
        }
        return command
    }

}
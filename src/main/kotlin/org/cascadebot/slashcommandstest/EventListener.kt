package org.cascadebot.slashcommandstest

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping

class EventListener : ListenerAdapter() {

    override fun onSlashCommand(event: SlashCommandEvent) {
        val args : Map<String, List<OptionMapping>> = event.options.groupBy { it.name }

        val commandId = event.commandIdLong;
    }

}
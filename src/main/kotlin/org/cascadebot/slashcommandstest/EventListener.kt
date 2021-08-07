package org.cascadebot.slashcommandstest

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EventListener : ListenerAdapter() {

    override fun onSlashCommand(event: SlashCommandEvent) {
        event.nam
    }

}
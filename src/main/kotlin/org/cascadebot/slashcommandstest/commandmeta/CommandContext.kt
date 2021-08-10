package org.cascadebot.slashcommandstest.commandmeta

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class CommandContext(private val event: SlashCommandEvent) {

    fun reply(message: String) {
        event.reply(message).queue()
    }

}
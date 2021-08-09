package org.cascadebot.slashcommandstest.commands

import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import org.cascadebot.slashcommandstest.commandmeta.CommandArgs
import org.cascadebot.slashcommandstest.commandmeta.CommandContext
import org.cascadebot.slashcommandstest.commandmeta.ParentCommand
import org.cascadebot.slashcommandstest.commandmeta.SubCommand

class PlayYoutube: SubCommand("youtube", "youtube", "Plays a song from youtube", ParentCommand.PLAY) {
    override val commandData: SubcommandData
        get() = SubcommandData(command, description).addOption(OptionType.STRING, "search", "The link or search term for the song you want to play")

    override fun onCommand(context: CommandContext, args: CommandArgs, data: Any) {
        TODO("Not yet implemented")
    }

}
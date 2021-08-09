package org.cascadebot.slashcommandstest.commands

import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import org.cascadebot.slashcommandstest.commandmeta.CommandArgs
import org.cascadebot.slashcommandstest.commandmeta.CommandContext
import org.cascadebot.slashcommandstest.commandmeta.ParentCommand
import org.cascadebot.slashcommandstest.commandmeta.SubCommand

class PlaySoundcloud: SubCommand("soundcloud", "soundcloud", "Plays a song from soundcloud", ParentCommand.PLAY) {
    override val commandData: SubcommandData
        get() = SubcommandData(command, description).addOption(OptionType.STRING, "link", "The link for the song you want to play")

    override fun onCommand(context: CommandContext, args: CommandArgs, data: Any) {
        TODO("Not yet implemented")
    }

}
package org.cascadebot.slashcommandstest.commands

import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import org.cascadebot.slashcommandstest.commandmeta.CommandArgs
import org.cascadebot.slashcommandstest.commandmeta.CommandContext
import org.cascadebot.slashcommandstest.commandmeta.ExecutableRootCommand
import org.cascadebot.slashcommandstest.commandmeta.Module

class BanCommand : ExecutableRootCommand("ban", Module.MODERATION, "Bans a user", "") {

    override val commandData: CommandData
        get() = CommandData(command, description)
            .addOption(OptionType.USER, "user", "The user to be banned", true)

    override fun onCommand(context: CommandContext, args: CommandArgs, data: Any) {

    }

}
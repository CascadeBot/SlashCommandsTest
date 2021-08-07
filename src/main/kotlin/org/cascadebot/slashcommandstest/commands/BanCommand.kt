package org.cascadebot.slashcommandstest.commands

import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import org.cascadebot.slashcommandstest.commandmeta.Command
import org.cascadebot.slashcommandstest.commandmeta.CommandArgs
import org.cascadebot.slashcommandstest.commandmeta.CommandContext
import org.cascadebot.slashcommandstest.commandmeta.Module
import java.lang.reflect.Member

class BanCommand : Command("ban", Module.MODERATION, "") {

    override val commandData: CommandData
        get() = CommandData("ban", "Bans a user from the guild!")
            .addOption(OptionType.USER, "user", "The user to be banned", true)

    override fun onCommand(data: Any, args: CommandArgs, context: CommandContext) {

    }

}
package org.cascadebot.slashcommandstest.commands

import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import org.cascadebot.slashcommandstest.commandmeta.CommandArgs
import org.cascadebot.slashcommandstest.commandmeta.CommandContext
import org.cascadebot.slashcommandstest.commandmeta.ParentCommand
import org.cascadebot.slashcommandstest.commandmeta.SubCommand
import org.cascadebot.slashcommandstest.commandmeta.SubCommandGroup

class PermissionsUserRemoveCommand: SubCommand("remove", "remove", "Removes a permission from a user", ParentCommand.PERMISSION, SubCommandGroup.PERMISSION_USER) {

    override val commandData: SubcommandData
        get() = SubcommandData(command, description)
            .addOption(OptionType.USER, "target", "The user to target")
            .addOption(OptionType.STRING, "permission", "The permission to remove")

    override fun onCommand(context: CommandContext, args: CommandArgs, data: Any) {

    }

}
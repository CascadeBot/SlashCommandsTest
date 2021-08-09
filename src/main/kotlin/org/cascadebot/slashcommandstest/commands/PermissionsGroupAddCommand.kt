package org.cascadebot.slashcommandstest.commands

import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import org.cascadebot.slashcommandstest.commandmeta.CommandArgs
import org.cascadebot.slashcommandstest.commandmeta.CommandContext
import org.cascadebot.slashcommandstest.commandmeta.ParentCommand
import org.cascadebot.slashcommandstest.commandmeta.SubCommand
import org.cascadebot.slashcommandstest.commandmeta.SubCommandGroup

class PermissionsGroupAddCommand: SubCommand("add", "add", "Adds a permission to a group", ParentCommand.PERMISSION, SubCommandGroup.PERMISSION_GROUP) {

    override val commandData: SubcommandData
        get() = SubcommandData(command, description)
            .addOption(OptionType.STRING, "group", "The group to target")
            .addOption(OptionType.STRING, "permission", "The permission to add")

    override fun onCommand(context: CommandContext, args: CommandArgs, data: Any) {

    }

}
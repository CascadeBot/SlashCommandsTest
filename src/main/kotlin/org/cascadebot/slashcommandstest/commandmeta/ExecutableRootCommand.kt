package org.cascadebot.slashcommandstest.commandmeta

import net.dv8tion.jda.api.interactions.commands.build.CommandData

// TODO this is a terrible name, change it
abstract class ExecutableRootCommand(command: String, module: Module, val description: String, permission: String) : ExecutableCommand(
    command, module,
    permission
) {

    abstract val commandData: CommandData // TODO maybe have our own custom command data? Currently this can cause unexpected issues if you specify sub commands/groups via this

}
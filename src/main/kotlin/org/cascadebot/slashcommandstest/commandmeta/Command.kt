package org.cascadebot.slashcommandstest.commandmeta

abstract class Command(
    command: String,
    module: Module,
    permission: String,
    deleteMessages: Boolean = true
) : ExecutableCommand(command, module, permission, deleteMessages) {

    open val subCommands: Set<SubCommand> = setOf()

}
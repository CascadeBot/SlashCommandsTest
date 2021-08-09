package org.cascadebot.slashcommandstest.commandmeta

abstract class ExecutableCommand(
    val command: String,
    val module: Module,
    val permission: String
) {

    abstract fun onCommand(context: CommandContext, args: CommandArgs, data: Any/* , something else? */)

}
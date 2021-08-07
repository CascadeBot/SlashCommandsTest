package org.cascadebot.slashcommandstest.commandmeta

import net.dv8tion.jda.api.interactions.commands.build.CommandData
import java.lang.reflect.Member

abstract class ExecutableCommand(
    val command: String,
    val module: Module,
    val permission: String,
    val deleteMessages: Boolean = true
) {

    abstract val commandData: CommandData

    abstract fun onCommand(sender: Member/* , something else? */)

}
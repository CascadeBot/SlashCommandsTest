package org.cascadebot.slashcommandstest.commandmeta

import com.google.common.reflect.ClassPath
import org.apache.commons.lang3.reflect.ConstructorUtils
import org.reflections.ReflectionUtils
import org.slf4j.LoggerFactory
import java.util.stream.Collectors
import kotlin.system.exitProcess

class CommandManager {

    private val _commands: MutableList<Command> = mutableListOf()
    val commands
        get() = _commands.toList()

    fun getCommand(command: String?): Command? {
        for (cmd in _commands) {
            if (cmd.command == command) return cmd
        }
        return null
    }

    fun getCommandsByModule(type: Module): List<Command> {
        return _commands
            .filter { command: Command -> command.module == type }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(CommandManager::class.java)
    }

    init {
        val start = System.currentTimeMillis()
        try {
            val classInfos: List<ClassPath.ClassInfo> = ClassPath.from(
                CommandManager::class.java.classLoader
            ).getTopLevelClassesRecursive("org.cascadebot.slashcommandstest.commands").asList()
            val classes = classInfos.stream().map { it.load() }.collect(Collectors.toList())
            for (c in classes) {
                if (Command::class.java.isAssignableFrom(c)) {
                    val command: Command = ConstructorUtils.invokeConstructor(c) as Command
                    _commands.add(command)
                }
            }
            LOGGER.info(
                "Loaded {} commands in {}ms.", _commands.size,
                System.currentTimeMillis() - start
            )
        } catch (e: Exception) {
            LOGGER.error("Could not load commands!", e)
            exitProcess(1)
        }
    }
}
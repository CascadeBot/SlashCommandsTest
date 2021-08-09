package org.cascadebot.slashcommandstest.commandmeta

import com.google.common.reflect.ClassPath
import org.apache.commons.lang3.reflect.ConstructorUtils
import org.cascadebot.slashcommandstest.SlashCommandsTest
import org.reflections.ReflectionUtils
import org.slf4j.LoggerFactory
import java.util.stream.Collectors
import kotlin.system.exitProcess

class CommandManager {

    private val _commands: MutableList<ExecutableCommand> = mutableListOf()
    val commands
        get() = _commands.toList()

    fun getCommand(command: String?): ExecutableCommand? {
        for (cmd in _commands) {
            if (cmd.command == command) return cmd
        }
        return null
    }

    fun getCommandsByModule(type: Module): List<ExecutableCommand> {
        return _commands
            .filter { command: ExecutableCommand -> command.module == type }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(CommandManager::class.java)
    }

    init {
        val start = System.currentTimeMillis()
        try {
            val classInfos: Set<ClassPath.ClassInfo> = ClassPath.from(
                SlashCommandsTest::class.java.classLoader
            ).getTopLevelClassesRecursive("org.cascadebot.slashcommandstest.commands")
            val classes = classInfos.map { it.load() }
            for (c in classes) {
                if (ExecutableCommand::class.java.isAssignableFrom(c)) {
                    val command: ExecutableCommand = ConstructorUtils.invokeConstructor(c) as ExecutableCommand
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
package org.cascadebot.slashcommandstest.commandmeta

import com.google.common.reflect.ClassPath
import org.apache.commons.lang3.reflect.ConstructorUtils
import org.cascadebot.slashcommandstest.SlashCommandsTest
import org.reflections.ReflectionUtils
import org.slf4j.LoggerFactory
import java.util.stream.Collectors
import kotlin.system.exitProcess

class CommandManager {
    // TODO figure out how to handle guild level commands
    private val _commands: MutableMap<CommandPath, ExecutableCommand> = mutableMapOf()
    val commands
        get() = _commands.toList()

    fun getCommand(rootId: Long?, path: String): ExecutableCommand {
        val parts = path.split("/")
        // If rootId is null then assume it's a global command
        return if (rootId == null) {
            _commands.filter { entry ->
                !parts.indices.any { parts.getOrNull(it) != entry.key.path.getOrNull(it) }
            }.map { it.value }.first()
        } else {
            _commands.filter { it.key == CommandPath(rootId, parts) }.map { it.value }.first()
            // TODO check for guild command?
        }
    }

    fun getCommandsByModule(type: Module): List<ExecutableCommand> {
        return _commands.values
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
                    when (command) {
                        is ExecutableRootCommand -> {
                            _commands[CommandPath(0, listOf(command.command))] = command;
                        }
                        is SubCommand -> {
                            if (command.group != null) {
                                _commands[CommandPath(0, listOf(command.parent.command, command.group.groupName, command.command))] = command;
                            } else {
                                _commands[CommandPath(0, listOf(command.parent.command, command.command))] = command;
                            }
                        }
                    }
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
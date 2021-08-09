package org.cascadebot.slashcommandstest.commandmeta

import java.util.Arrays
import java.util.EnumSet
import java.util.stream.Collectors

enum class Module {
    /**
     * A module that contains the bot's meta commands. These cannot be disabled!
     */
    CORE(ModuleFlag.REQUIRED, ModuleFlag.DEFAULT),

    /**
     * This module contains commands that are used to manage the bot settings for the guild.
     */
    MANAGEMENT(ModuleFlag.REQUIRED, ModuleFlag.DEFAULT),

    /**
     * This is music, what do you think this is?
     */
    MUSIC(ModuleFlag.DEFAULT),

    /**
     * The commands in this module display various pieces of information about discord entities.
     */
    INFORMATIONAL(ModuleFlag.DEFAULT),

    /**
     * This module speaks for itself, it contains commands that allow the admins of a guild to moderate said guild
     */
    MODERATION(ModuleFlag.DEFAULT),

    /**
     * This is a bit of a random module containing smaller commands.
     */
    FUN(),

    /**
     * This is a module for containing useful stuff that doesn't really fit into any other module.
     */
    USEFUL(),

    /**
     * All our special commands :D
     */
    DEVELOPER(ModuleFlag.PRIVATE);

    private var flags: EnumSet<ModuleFlag>

    constructor() {
        flags = EnumSet.noneOf(ModuleFlag::class.java) // Public module that is not required
    }

    constructor(vararg flags: ModuleFlag) {
        this.flags = EnumSet.noneOf(ModuleFlag::class.java)
        this.flags.addAll(flags.toList())
    }

    private fun isFlagEnabled(flag: ModuleFlag): Boolean {
        return flags.contains(flag)
    }

    val isPrivate: Boolean
        get() = isFlagEnabled(ModuleFlag.PRIVATE)
    val isRequired: Boolean
        get() = isFlagEnabled(ModuleFlag.REQUIRED)
    val isDefault: Boolean
        get() = isFlagEnabled(ModuleFlag.DEFAULT)

    companion object {

        fun getModules(vararg flags: ModuleFlag?): Set<Module> {
            return Arrays.stream(values())
                .filter { module: Module ->
                    module.flags.containsAll(flags.toList())
                }.collect(Collectors.toSet())
        }
    }
}

enum class ModuleFlag {
    /**
     * Indicates that this module must always be enabled for the proper function
     * of the bot.
     */
    REQUIRED,

    /**
     * Indicates that this module cannot be used by general users of the bot and
     * so is unable to be disabled
     */
    PRIVATE,

    /**
     * Indicates that the module will be enabled by default
     */
    DEFAULT
}
package org.cascadebot.slashcommandstest

import org.simpleyaml.configuration.file.FileConfiguration
import org.simpleyaml.configuration.file.YamlConfiguration
import org.simpleyaml.exceptions.InvalidConfigurationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.system.exitProcess

data class Config(
    val botToken: String,
    val botId: Long
) {
    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(Config::class.java)

        var INS: Config? = null;

        fun init(file: File) {
            LOG.info("Starting loading config!")

            val config: FileConfiguration = YamlConfiguration()

            try {
                config.load(file)
            } catch (e: InvalidConfigurationException) {
                LOG.error("Invalid yaml configuration", e)
                exitProcess(1)
            }

            val botId = config.getLong("bot.id", -1)
            if (botId == -1L) {
                LOG.error("No bot ID provided in config! Please provide the bot ID to start the bot.")
                exitProcess(1)
            }

            val botToken = config.getString("bot.token", "")
            if (botToken.isEmpty()) {
                LOG.error("No bot token provided in config! Please provide a token to start the bot.")
                exitProcess(1)
            }

            INS = Config(botToken, botId)

            LOG.info("Successfully loaded config!")
        }
    }
}
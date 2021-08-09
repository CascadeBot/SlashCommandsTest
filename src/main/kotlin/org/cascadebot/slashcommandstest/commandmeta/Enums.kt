package org.cascadebot.slashcommandstest.commandmeta

enum class ParentCommand(
    val command: String,
    val module: Module,
    val permission: String,
    val description: String
) {
    PERMISSION("permission", Module.MODERATION, "permission", "Modifies permissions")
}

enum class SubCommandGroup(
    val groupName: String,
    val permission: String,
    val description: String
) {
    PERMISSION_GROUP("group", "group", "Modifies the permissions of a group"),
    PERMISSION_USER("user", "user", "Modifies the permissions of a user")
}
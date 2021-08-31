package org.cascadebot.slashcommandstest.commandmeta

data class CommandPath(val rootId: Long) {

    var path: List<String> = listOf()
        private set

    constructor(rootId: Long, path: List<String>) : this(rootId) {
        this.path = path;
    }

    override fun equals(other: Any?): Boolean {
        return when(other) {
            is CommandPath -> {
                var matches = this.rootId == other.rootId
                for (i in this.path.indices) {
                    if (this.path[i] != other.path[i]) {
                        matches = false
                    }
                }
                matches
            }
            else -> {
                false;
            }
        }
    }
}
package defalt.logger

/**
 * Level of a log message.
 *
 * @property priority order of the levels: a logger writes the messages of its level and above.
 */
enum class LogLevel(
    val priority: Int,
) {
    TRACE(0),
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4),
}

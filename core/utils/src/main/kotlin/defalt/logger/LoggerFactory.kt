package defalt.logger

interface LoggerFactory {
    fun getLogger(type: Class<*>): Logger
}

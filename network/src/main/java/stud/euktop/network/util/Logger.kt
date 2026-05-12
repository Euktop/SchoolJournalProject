package stud.euktop.network.util

interface Logger {
    fun i(tag: String, action: String, data: String? = null)
    fun d(tag: String, action: String, data: String? = null)
    fun e(tag: String, action: String, throwable: Throwable? = null, data: String? = null)
}

var logger: Logger? = object : Logger {
    override fun i(tag: String, action: String, data: String?) {
        log("INFO", tag, action, data)
    }

    override fun d(tag: String, action: String, data: String?) {
        log("DEBUG", tag, action, data)
    }

    override fun e(
        tag: String,
        action: String,
        throwable: Throwable?,
        data: String?
    ) {
        log("ERROR", tag, action, data, throwable)
    }

    private fun log(
        logType: String,
        tag: String,
        event: String,
        message: String?,
        th: Throwable? = null
    ) {
        val message = "$event -- $message"
        print("$logType:[$tag]: $event -- $message $")
    }
}

fun <T : Any> T.toSimpleTag() = this::class.java.simpleName ?: this::class.java.canonicalName!!
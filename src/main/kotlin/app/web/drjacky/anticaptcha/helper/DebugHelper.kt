package app.web.drjacky.anticaptcha.helper

import org.json.JSONObject

object DebugHelper {
    private const val ANSI_RED = "\u001B[31m"
    private const val ANSI_GREEN = "\u001B[32m"
    private const val ANSI_YELLOW = "\u001B[33m"
    private const val ANSI_RESET = "\u001B[0m"

    fun setVerboseMode(verboseMode: Boolean) {
        DebugHelper.verboseMode = verboseMode
    }

    private var verboseMode = false

    @JvmStatic
    fun jsonFieldParseError(field: String, submitResult: JSONObject?) {
        val error = field + " could not be parsed. Raw response: " + JsonHelper.asString(submitResult)
        out(error, Type.ERROR)
    }

    fun out(message: String?, type: Type) {
        if (!verboseMode) {
            return
        }
        when (type) {
            Type.ERROR -> {
                println(ANSI_RED + message)
            }

            Type.INFO -> {
                println(ANSI_YELLOW + message)
            }

            Type.SUCCESS -> {
                println(ANSI_GREEN + message)
            }
        }
        print(ANSI_RESET)
    }

    enum class Type {
        ERROR, INFO, SUCCESS
    }
}
package app.web.drjacky.anticaptcha.helper

import org.apache.commons.codec.binary.Base64
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

object StringHelper {
    fun toCamelCase(s: String): String {
        val parts = s.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var camelCaseString = ""
        for (part in parts) {
            camelCaseString += part.substring(0, 1).uppercase(Locale.getDefault()) + part.substring(1)
                .lowercase(Locale.getDefault())
        }
        return camelCaseString.substring(0, 1).lowercase(Locale.getDefault()) + camelCaseString.substring(1)
    }

    fun imageFileToBase64String(path: String?): String? {
        return try {
            String(Base64.encodeBase64(Files.readAllBytes(Paths.get(path))))
        } catch (e: Exception) {
            null
        }
    }
}
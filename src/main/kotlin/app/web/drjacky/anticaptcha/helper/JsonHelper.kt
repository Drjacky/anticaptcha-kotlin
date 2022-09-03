package app.web.drjacky.anticaptcha.helper

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.text.ParseException
import java.util.*

object JsonHelper {
    fun extractStr(json: JSONObject, fieldName: String): String? {
        return extractStr(json, fieldName, null, false)
    }

    fun extractStr(json: JSONObject, fieldName: String, silent: Boolean?): String? {
        return extractStr(json, fieldName, null, silent)
    }

    fun extractStr(json: JSONObject, firstLevel: String, secondLevel: String?): String? {
        return extractStr(json, firstLevel, secondLevel, false)
    }

    fun asString(json: JSONObject?): String? {
        return try {
            json?.toString(4)
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
    }

    fun extractStr(json: JSONObject, firstLevel: String, secondLevel: String?, silent: Boolean?): String? {
        return try {
            if (secondLevel == null) json.get(firstLevel).toString() else json.getJSONObject(firstLevel)
                .get(secondLevel).toString()
        } catch (e: JSONException) {
            if (!silent!!) {
                val path = firstLevel + if (secondLevel == null) "" else "=>$secondLevel"
                DebugHelper.jsonFieldParseError(path, json)
            }
            null
        }
    }

    fun extractInt(json: JSONObject, firstLevel: String, secondLevel: String?, silent: Boolean?): Int? {
        return try {
            if (secondLevel == null) json.getInt(firstLevel) else json.getJSONObject(firstLevel).getInt(secondLevel)
        } catch (e: JSONException) {
            if (!silent!!) {
                val path = firstLevel + if (secondLevel == null) "" else "=>$secondLevel"
                DebugHelper.jsonFieldParseError(path, json)
            }
            null
        }
    }

    fun extractInt(json: JSONObject, fieldName: String): Int? {
        return extractInt(json, fieldName, false)
    }

    fun extractInt(json: JSONObject, fieldName: String, silent: Boolean): Int? {
        return try {
            json.getInt(fieldName)
        } catch (e1: JSONException) {
            val str = extractStr(json, fieldName, silent)
            if (str == null) {
                if (!silent) {
                    DebugHelper.jsonFieldParseError(fieldName, json)
                }
                return null
            }
            try {
                str.toInt()
            } catch (e2: NumberFormatException) {
                DebugHelper.jsonFieldParseError(fieldName, json)
                null
            }
        }
    }

    fun extractDouble(json: JSONObject, fieldName: String): Double? {
        return try {
            json.getDouble(fieldName)
        } catch (e1: JSONException) {
            var str = extractStr(json, fieldName)
            if (str == null) {
                DebugHelper.jsonFieldParseError(fieldName, json)
                return null
            }
            str = str.replace(',', '.')
            val format: NumberFormat = NumberFormat.getInstance(Locale.US)
            try {
                format.parse(str).toDouble()
            } catch (e2: ParseException) {
                DebugHelper.jsonFieldParseError(fieldName, json)
                null
            }
        }
    }

    fun extractJSONArray(json: JSONObject, firstLevel: String?, fieldName: String?): JSONArray? {
        return try {
            json.getJSONObject(firstLevel).getJSONArray(fieldName)
        } catch (e: JSONException) {
            null
        }
    }

    fun extractJSONObject(json: JSONObject, firstLevel: String?, fieldName: String?): JSONObject? {
        return try {
            json.getJSONObject(firstLevel).getJSONObject(fieldName)
        } catch (e: JSONException) {
            null
        }
    }
}
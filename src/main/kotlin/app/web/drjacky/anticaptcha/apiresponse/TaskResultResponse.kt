package app.web.drjacky.anticaptcha.apiresponse

import app.web.drjacky.anticaptcha.helper.DebugHelper
import app.web.drjacky.anticaptcha.helper.JsonHelper
import org.json.JSONObject
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*


class TaskResultResponse(json: JSONObject) {
    val errorId: Int?
    var errorCode: String? = null
    private var errorDescription: String? = null
    var status: StatusType? = null
    var cost: Double? = null
    var ip: String? = null

    /**
     * ﻿Task create time in UTC
     */
    var createTime: ZonedDateTime? = null

    /**
     * ﻿Task end time in UTC
     */
    var endTime: ZonedDateTime? = null
    var solveCount: Int? = null
    var solution: SolutionData = SolutionData()

    init {
        errorId = JsonHelper.extractInt(json, "errorId")
        if (errorId != null) {
            if (errorId == 0) {
                status = parseStatus(JsonHelper.extractStr(json, "status"))
                if (status == StatusType.READY) {
                    cost = JsonHelper.extractDouble(json, "cost")
                    ip = JsonHelper.extractStr(json, "ip", true)
                    solveCount = JsonHelper.extractInt(json, "solveCount", true)
                    createTime = unixTimeStampToDateTime(JsonHelper.extractDouble(json, "createTime"))
                    endTime = unixTimeStampToDateTime(JsonHelper.extractDouble(json, "endTime"))
                    solution.gRecaptchaResponse = JsonHelper.extractStr(json, "solution", "gRecaptchaResponse", true)
                    solution.gRecaptchaResponseMd5 =
                        JsonHelper.extractStr(json, "solution", "gRecaptchaResponseMd5", true)
                    solution.text = JsonHelper.extractStr(json, "solution", "text", true)
                    solution.url = JsonHelper.extractStr(json, "solution", "url", true)
                    solution.token = JsonHelper.extractStr(json, "solution", "token", true)
                    solution.challenge = JsonHelper.extractStr(json, "solution", "challenge", true)
                    solution.seccode = JsonHelper.extractStr(json, "solution", "seccode", true)
                    solution.validate = JsonHelper.extractStr(json, "solution", "validate", true)
                    solution.cookies = JsonHelper.extractJSONObject(json, "solution", "cookies")
                    solution.localStorage = JsonHelper.extractJSONObject(json, "solution", "localStorage")
                    solution.fingerprint = JsonHelper.extractJSONObject(json, "solution", "fingerprint")
                    solution.domain = JsonHelper.extractStr(json, "solution", "domain", true)
                    solution.captchaId = JsonHelper.extractStr(json, "solution", "captcha_id", true)
                    solution.lotNumber = JsonHelper.extractStr(json, "solution", "lot_number", true)
                    solution.passToken = JsonHelper.extractStr(json, "solution", "pass_token", true)
                    solution.genTime = JsonHelper.extractInt(json, "solution", "gen_time", true)
                    solution.captchaOutput = JsonHelper.extractStr(json, "solution", "captcha_output", true)
                    if (solution.gRecaptchaResponse == null && solution.text == null && solution.token == null && solution.challenge == null && solution.seccode == null && solution.validate == null && solution.cookies == null && solution.captchaOutput == null) {
                        DebugHelper.out("2 Got no 'solution' field from API", DebugHelper.Type.ERROR)
                        DebugHelper.out(json.toString(), DebugHelper.Type.ERROR)
                    }
                }
            } else {
                errorCode = JsonHelper.extractStr(json, "errorCode")
                errorDescription = JsonHelper.extractStr(json, "errorDescription")
                DebugHelper.out(errorDescription, DebugHelper.Type.ERROR)
            }
        } else {
            DebugHelper.out("Unknown error", DebugHelper.Type.ERROR)
        }
    }

    fun getErrorDescription(): String {
        return errorDescription ?: "(no error description)"
    }

    private fun parseStatus(status: String?): StatusType? {
        return if (status == null || status.length == 0) {
            null
        } else try {
            app.web.drjacky.anticaptcha.apiresponse.TaskResultResponse.StatusType.valueOf(status.uppercase(Locale.getDefault()))
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    enum class StatusType {
        PROCESSING, READY
    }

    inner class SolutionData {
        var gRecaptchaResponse // Will be available for Recaptcha tasks only!
                : String? = null
        var gRecaptchaResponseMd5 // for Recaptcha with isExtended=true property
                : String? = null
        var text // Will be available for ImageToText tasks only!
                : String? = null
        var url // Will be available for ImageToText and AntiGate tasks only!
                : String? = null
        var token // Will be available for FunCaptcha tasks only
                : String? = null
        var challenge // Will be available for GeeTest tasks only
                : String? = null
        var seccode // Will be available for GeeTest tasks only
                : String? = null
        var validate // Will be available for GeeTest tasks only
                : String? = null
        var captchaId // Will be available for GeeTest v4 tasks only
                : String? = null
        var lotNumber // Will be available for GeeTest v4 tasks only
                : String? = null
        var passToken // Will be available for GeeTest v4 tasks only
                : String? = null
        var genTime // Will be available for GeeTest v4 tasks only
                : Int? = null
        var captchaOutput // Will be available for GeeTest v4 tasks only
                : String? = null
        var cookies: JSONObject? = null
        var localStorage: JSONObject? = null
        var fingerprint: JSONObject? = null
        var domain: String? = null
    }

    companion object {
        private fun unixTimeStampToDateTime(unixTimeStamp: Double?): ZonedDateTime? {
            if (unixTimeStamp == null) {
                return null
            }
            val epochStart = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"))
            return epochStart.plusSeconds(unixTimeStamp.toLong())
        }
    }
}

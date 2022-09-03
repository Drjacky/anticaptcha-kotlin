package app.web.drjacky.anticaptcha.apiresponse

import app.web.drjacky.anticaptcha.helper.DebugHelper
import app.web.drjacky.anticaptcha.helper.JsonHelper
import org.json.JSONObject


class BalanceResponse(json: JSONObject) {
    val errorId: Int?
    var errorCode: String? = null
    private var errorDescription: String? = null
    var balance: Double? = null

    init {
        errorId = JsonHelper.extractInt(json, "errorId")
        if (errorId != null) {
            if (errorId == 0) {
                balance = JsonHelper.extractDouble(json, "balance")
            } else {
                errorCode = JsonHelper.extractStr(json, "errorCode")
                errorDescription = JsonHelper.extractStr(json, "errorDescription")
            }
        } else {
            DebugHelper.out("Unknown error", DebugHelper.Type.ERROR)
        }
    }

    fun getErrorDescription(): String {
        return errorDescription ?: "(no error description)"
    }
}

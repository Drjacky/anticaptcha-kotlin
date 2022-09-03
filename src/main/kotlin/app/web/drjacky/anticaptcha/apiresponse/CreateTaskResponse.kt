package app.web.drjacky.anticaptcha.apiresponse

import app.web.drjacky.anticaptcha.helper.DebugHelper
import app.web.drjacky.anticaptcha.helper.JsonHelper
import org.json.JSONObject


class CreateTaskResponse(json: JSONObject) {
    val errorId: Int?
    var errorCode: String? = null
    private var errorDescription: String? = null
    var taskId: Int? = null

    init {
        errorId = JsonHelper.extractInt(json, "errorId")
        if (errorId != null) {
            if (errorId == 0) {
                taskId = JsonHelper.extractInt(json, "taskId")
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

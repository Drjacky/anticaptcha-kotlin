package app.web.drjacky.anticaptcha

import app.web.drjacky.anticaptcha.apiresponse.BalanceResponse
import app.web.drjacky.anticaptcha.apiresponse.CreateTaskResponse
import app.web.drjacky.anticaptcha.apiresponse.TaskResultResponse
import app.web.drjacky.anticaptcha.helper.DebugHelper
import app.web.drjacky.anticaptcha.helper.HttpHelper
import app.web.drjacky.anticaptcha.helper.JsonHelper
import app.web.drjacky.anticaptcha.helper.StringHelper
import app.web.drjacky.anticaptcha.http.HttpRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

abstract class AnticaptchaBase {
    protected var taskInfo: TaskResultResponse? = null
    private val host = "api.anti-captcha.com"
    private val scheme = SchemeType.HTTPS
    private var errorMessage: String? = null
    private var taskId: Int? = null
    private var clientKey: String? = null
    /**
     * Specify softId to earn 10% commission with your app.
     * Get your softId here:
     * [https://anti-captcha.com/clients/tools/devcenter](https://anti-captcha.com/clients/tools/devcenter)
     */
    /**
     * Specify softId to earn 10% commission with your app.
     * Get your softId here:
     * [https://anti-captcha.com/clients/tools/devcenter](https://anti-captcha.com/clients/tools/devcenter)
     */
    var softId: Int? = null

    enum class ProxyTypeOption {
        HTTP, SOCKS4, SOCKS5
    }

    private fun jsonPostRequest(methodName: ApiMethod, jsonPostData: JSONObject): JSONObject? {
        val url = scheme.toString() + "://" + host + "/" + StringHelper.toCamelCase(methodName.toString())
        val request = HttpRequest(url)
        request.rawPost = JsonHelper.asString(jsonPostData)
        request.addHeader("Content-Type", "application/json; charset=utf-8")
        request.addHeader("Accept", "application/json")
        request.timeout = 30000
        val rawJson: String
        try {
            rawJson = HttpHelper.download(request).body!!
        } catch (e: Exception) {
            errorMessage = e.message
            DebugHelper.out("HTTP problem: " + e.message, DebugHelper.Type.ERROR)
            return null
        }
        return try {
            JSONObject(rawJson)
        } catch (e: Exception) {
            errorMessage = e.message
            DebugHelper.out("JSON parse problem: " + e.message, DebugHelper.Type.ERROR)
            null
        }
    }

    abstract fun getPostData(): JSONObject?

    fun createTask(): Boolean {
        val taskJson: JSONObject? = getPostData()
        if (taskJson == null) {
            DebugHelper.out("JSON error", DebugHelper.Type.ERROR)
            return false
        }
        val jsonPostData = JSONObject()
        try {
            jsonPostData.put("clientKey", clientKey)
            jsonPostData.put("softId", softId)
            jsonPostData.put("task", taskJson)
        } catch (e: JSONException) {
            errorMessage = e.message
            DebugHelper.out("JSON compilation error: " + e.message, DebugHelper.Type.ERROR)
            return false
        }
        DebugHelper.out("Connecting to $host", DebugHelper.Type.INFO)
        val postResult: JSONObject? = jsonPostRequest(ApiMethod.CREATE_TASK, jsonPostData)
        if (postResult == null) {
            DebugHelper.out("API error", DebugHelper.Type.ERROR)
            return false
        }
        val response = CreateTaskResponse(postResult)
        if (response.errorId == null || !response.errorId.equals(0)) {
            errorMessage = response.getErrorDescription()
            val errorId = if (response.errorId == null) "" else response.errorId.toString()
            DebugHelper.out(
                "API error " + errorId + ": " + response.getErrorDescription(),
                DebugHelper.Type.ERROR
            )
            return false
        }
        if (response.taskId == null) {
            DebugHelper.jsonFieldParseError("taskId", postResult)
            return false
        }
        taskId = response.taskId
        DebugHelper.out("Task ID: $taskId", DebugHelper.Type.SUCCESS)
        return true
    }

    val balance: Double?
        get() {
            val jsonPostData = JSONObject()
            try {
                jsonPostData.put("clientKey", clientKey)
            } catch (e: JSONException) {
                errorMessage = e.message
                DebugHelper.out("JSON compilation error: " + e.message, DebugHelper.Type.ERROR)
                return null
            }
            val postResult: JSONObject? = jsonPostRequest(ApiMethod.GET_BALANCE, jsonPostData)
            if (postResult == null) {
                DebugHelper.out("API error", DebugHelper.Type.ERROR)
                return null
            }
            val balanceResponse = BalanceResponse(postResult)
            if (balanceResponse.errorId == null || !balanceResponse.errorId.equals(0)) {
                errorMessage = balanceResponse.getErrorDescription()
                val errorId = if (balanceResponse.errorId == null) "" else balanceResponse.errorId.toString()
                DebugHelper.out(
                    "API error " + errorId + ": " + balanceResponse.getErrorDescription(),
                    DebugHelper.Type.ERROR
                )
                return null
            }
            return balanceResponse.balance
        }

    @JvmOverloads
    @Throws(InterruptedException::class)
    fun waitForResult(maxSeconds: Int = 120, currentSecond: Int = 0): Boolean {
        if (currentSecond >= maxSeconds) {
            DebugHelper.out("Time's out.", DebugHelper.Type.ERROR)
            return false
        }
        if (currentSecond == 0) {
            DebugHelper.out("Waiting for 3 seconds...", DebugHelper.Type.INFO)
            TimeUnit.SECONDS.sleep(3)
        } else {
            TimeUnit.SECONDS.sleep(1)
        }
        DebugHelper.out("Requesting the task status", DebugHelper.Type.INFO)
        val jsonPostData = JSONObject()
        try {
            jsonPostData.put("clientKey", clientKey)
            jsonPostData.put("taskId", taskId)
        } catch (e: JSONException) {
            errorMessage = e.message
            DebugHelper.out("JSON compilation error: " + e.message, DebugHelper.Type.ERROR)
            return false
        }
        val postResult: JSONObject? = jsonPostRequest(ApiMethod.GET_TASK_RESULT, jsonPostData)
        if (postResult == null) {
            DebugHelper.out("API error", DebugHelper.Type.ERROR)
            return false
        }
        taskInfo = TaskResultResponse(postResult)
        if (taskInfo?.errorId == null || taskInfo?.errorId?.equals(0)!!.not()) {
            errorMessage = taskInfo?.getErrorDescription()
            val errorId = if (taskInfo?.errorId == null) "" else taskInfo?.errorId.toString()
            DebugHelper.out(
                "API error $errorId: $errorMessage",
                DebugHelper.Type.ERROR
            )
            return false
        }
        val status: TaskResultResponse.StatusType? = taskInfo?.status
        val solution: TaskResultResponse.SolutionData? = taskInfo?.solution
        if (status != null && status == TaskResultResponse.StatusType.PROCESSING) {
            DebugHelper.out("The task is still processing...", DebugHelper.Type.INFO)
            return waitForResult(maxSeconds, currentSecond + 1)
        } else if (status != null && status == TaskResultResponse.StatusType.READY) {
            if (solution?.gRecaptchaResponse == null && solution?.text == null && solution?.token == null && solution?.challenge == null && solution?.seccode == null && solution?.validate == null && solution?.cookies == null) {
                DebugHelper.out("Got no 'solution' field from API", DebugHelper.Type.ERROR)
                return false
            }
            DebugHelper.out("The task is complete!", DebugHelper.Type.SUCCESS)
            return true
        }
        errorMessage = "An unknown API status, please update your software"
        DebugHelper.out(errorMessage, DebugHelper.Type.ERROR)
        return false
    }

    fun setClientKey(clientKey_: String?) {
        clientKey = clientKey_
    }

    fun getErrorMessage(): String {
        return if (errorMessage == null) "no error message" else errorMessage!!
    }

    private enum class SchemeType {
        HTTP, HTTPS
    }

    private enum class ApiMethod {
        CREATE_TASK, GET_TASK_RESULT, GET_BALANCE
    }
}
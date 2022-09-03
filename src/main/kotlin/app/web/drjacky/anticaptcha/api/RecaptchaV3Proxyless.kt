package app.web.drjacky.anticaptcha.api

import app.web.drjacky.anticaptcha.AnticaptchaBase
import app.web.drjacky.anticaptcha.IAnticaptchaTaskProtocol
import app.web.drjacky.anticaptcha.apiresponse.TaskResultResponse
import app.web.drjacky.anticaptcha.helper.DebugHelper
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

open class RecaptchaV3Proxyless : AnticaptchaBase(), IAnticaptchaTaskProtocol {
    var websiteUrl: URL? = null
    var websiteKey: String? = null
    var pageAction: String? = null
    private var minScore = 0.3

    fun getMinScore(): Double {
        return minScore
    }

    fun setMinScore(minScore: Double) {
        if (minScore != 0.3 && minScore != 0.7 && minScore != 0.9) {
            DebugHelper.out(
                "minScore must be one of these: 0.3, 0.7, 0.9; you passed $minScore; 0.3 will be set",
                DebugHelper.Type.ERROR
            )
        } else {
            this.minScore = minScore
        }
    }

    override fun getPostData(): JSONObject? {
        val postData = JSONObject()
        try {
            postData.put("type", "RecaptchaV3TaskProxyless")
            postData.put("websiteURL", websiteUrl.toString())
            postData.put("websiteKey", websiteKey)
            postData.put("pageAction", pageAction)
            postData.put("minScore", minScore)
        } catch (e: JSONException) {
            DebugHelper.out("JSON compilation error: " + e.message, DebugHelper.Type.ERROR)
            return null
        }
        return postData
    }

    override fun getTaskSolution(): TaskResultResponse.SolutionData? = taskInfo?.solution
}
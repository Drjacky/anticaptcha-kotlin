package app.web.drjacky.anticaptcha.api

import app.web.drjacky.anticaptcha.AnticaptchaBase
import app.web.drjacky.anticaptcha.IAnticaptchaTaskProtocol
import app.web.drjacky.anticaptcha.apiresponse.TaskResultResponse
import app.web.drjacky.anticaptcha.helper.DebugHelper
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

open class RecaptchaV2EnterpriseProxyless : AnticaptchaBase(), IAnticaptchaTaskProtocol {
    var websiteUrl: URL? = null
    var websiteKey: String? = null
    var enterprisePayload: JSONObject? = null

    override fun getPostData(): JSONObject? {
        val postData = JSONObject()
        try {
            postData.put("type", "RecaptchaV2EnterpriseTaskProxyless")
            postData.put("websiteURL", websiteUrl.toString())
            postData.put("websiteKey", websiteKey)
            if (enterprisePayload != null) postData.put("enterprisePayload", enterprisePayload)
        } catch (e: JSONException) {
            DebugHelper.out("JSON compilation error: " + e.message, DebugHelper.Type.ERROR)
            return null
        }
        return postData
    }

    override fun getTaskSolution(): TaskResultResponse.SolutionData? = taskInfo?.solution
}
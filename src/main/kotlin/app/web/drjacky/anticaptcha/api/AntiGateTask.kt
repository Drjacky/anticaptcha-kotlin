package app.web.drjacky.anticaptcha.api

import app.web.drjacky.anticaptcha.AnticaptchaBase
import app.web.drjacky.anticaptcha.IAnticaptchaTaskProtocol
import app.web.drjacky.anticaptcha.apiresponse.TaskResultResponse
import app.web.drjacky.anticaptcha.helper.DebugHelper
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URL


class AntiGateTask : AnticaptchaBase(), IAnticaptchaTaskProtocol {
    var websiteUrl: URL? = null
    var templateName: String? = null
    var variables: JSONObject? = null
    var domainsOfInterest: JSONArray? = null
    var proxyAddress: String? = null
    var proxyPort: Int? = null
    var proxyLogin: String? = null
    var proxyPassword: String? = null

    override fun getPostData(): JSONObject? {
        val postData = JSONObject()
        try {
            postData.put("type", "AntiGateTask")
            postData.put("websiteURL", websiteUrl.toString())
            postData.put("templateName", templateName)
            if (proxyAddress != null && proxyPort != null) {
                postData.put("proxyAddress", proxyAddress)
                postData.put("proxyPort", proxyPort)
            }
            if (proxyLogin != null && proxyPassword != null) {
                postData.put("proxyLogin", proxyLogin)
                postData.put("proxyPassword", proxyPassword)
            }
            if (variables != null) {
                postData.put("variables", variables)
            }
            if (domainsOfInterest != null) {
                postData.put("domainsOfInterest", domainsOfInterest)
            }
        } catch (e: JSONException) {
            DebugHelper.out("JSON compilation error: " + e.message, DebugHelper.Type.ERROR)
            return null
        }
        return postData
    }

    override fun getTaskSolution(): TaskResultResponse.SolutionData? = taskInfo?.solution
}

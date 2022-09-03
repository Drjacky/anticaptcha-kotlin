package app.web.drjacky.anticaptcha.api

import app.web.drjacky.anticaptcha.AnticaptchaBase
import app.web.drjacky.anticaptcha.IAnticaptchaTaskProtocol
import app.web.drjacky.anticaptcha.apiresponse.TaskResultResponse
import app.web.drjacky.anticaptcha.helper.DebugHelper
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

open class GeeTestV4Proxyless : AnticaptchaBase(), IAnticaptchaTaskProtocol {
    var websiteUrl: URL? = null
    var websiteKey: String? = null
    var geetestApiServerSubdomain: String? = null
    private var initParameters: JSONObject? = null

    fun setInitParameters(value: JSONObject?) {
        initParameters = value
    }

    override fun getPostData(): JSONObject? {
        val postData = JSONObject()
        try {
            postData.put("type", "GeeTestTaskProxyless")
            postData.put("websiteURL", websiteUrl.toString())
            postData.put("gt", websiteKey)
            postData.put("version", 4)
            if (initParameters != null) postData.put("initParameters", initParameters)
            if (geetestApiServerSubdomain != null && geetestApiServerSubdomain.isNullOrEmpty().not()) {
                postData.put("geetestApiServerSubdomain", geetestApiServerSubdomain)
            }
        } catch (e: JSONException) {
            DebugHelper.out("JSON compilation error: " + e.message, DebugHelper.Type.ERROR)
            return null
        }
        return postData
    }

    override fun getTaskSolution(): TaskResultResponse.SolutionData? = taskInfo?.solution
}
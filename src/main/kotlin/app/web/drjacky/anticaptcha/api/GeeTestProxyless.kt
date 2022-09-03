package app.web.drjacky.anticaptcha.api

import app.web.drjacky.anticaptcha.AnticaptchaBase
import app.web.drjacky.anticaptcha.IAnticaptchaTaskProtocol
import app.web.drjacky.anticaptcha.apiresponse.TaskResultResponse
import app.web.drjacky.anticaptcha.helper.DebugHelper
import org.json.JSONException
import org.json.JSONObject
import java.net.URL

open class GeeTestProxyless : AnticaptchaBase(), IAnticaptchaTaskProtocol {
    var websiteUrl: URL? = null
    var websiteKey: String? = null
    var websiteChallenge: String? = null
    var geetestApiServerSubdomain: String? = null
    private var geetestLib: String? = null

    fun setGeetestLib(geetestLib: String?) {
        this.geetestLib = geetestLib
    }

    override fun getPostData(): JSONObject? {
        val postData = JSONObject()
        try {
            postData.put("type", "GeeTestTaskProxyless")
            postData.put("websiteURL", websiteUrl.toString())
            postData.put("gt", websiteKey)
            postData.put("challenge", websiteChallenge)
            if (geetestApiServerSubdomain != null && geetestApiServerSubdomain!!.length > 0) {
                postData.put("geetestApiServerSubdomain", geetestApiServerSubdomain)
            }
            if (geetestLib != null) {
                postData.put("geetestGetLib", geetestLib)
            }
        } catch (e: JSONException) {
            DebugHelper.out("JSON compilation error: " + e.message, DebugHelper.Type.ERROR)
            return null
        }
        return postData
    }

    override fun getTaskSolution(): TaskResultResponse.SolutionData? = taskInfo?.solution
}
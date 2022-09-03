package app.web.drjacky.anticaptcha.api

import app.web.drjacky.anticaptcha.AnticaptchaBase
import app.web.drjacky.anticaptcha.IAnticaptchaTaskProtocol
import app.web.drjacky.anticaptcha.apiresponse.TaskResultResponse
import app.web.drjacky.anticaptcha.helper.DebugHelper
import org.json.JSONException
import org.json.JSONObject
import java.net.URL


open class FunCaptchaProxyless : AnticaptchaBase(), IAnticaptchaTaskProtocol {
    var websiteUrl: URL? = null
    var websitePublicKey: String? = null
    var apiSubdomain: String? = null
    var dataBlob: String? = null

    override fun getPostData(): JSONObject? {
        val postData = JSONObject()
        try {
            postData.put("type", "FunCaptchaTaskProxyless")
            postData.put("websiteURL", websiteUrl)
            postData.put("websitePublicKey", websitePublicKey)
            if (apiSubdomain != null) {
                postData.put("funcaptchaApiJSSubdomain", apiSubdomain)
            }
            if (dataBlob != null) {
                postData.put("data", dataBlob)
            }
        } catch (e: JSONException) {
            DebugHelper.out("JSON compilation error: " + e.message, DebugHelper.Type.ERROR)
            return null
        }
        return postData
    }

    override fun getTaskSolution(): TaskResultResponse.SolutionData? = taskInfo?.solution

}

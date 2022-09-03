package app.web.drjacky.anticaptcha.api

import app.web.drjacky.anticaptcha.apiresponse.TaskResultResponse
import app.web.drjacky.anticaptcha.helper.DebugHelper
import org.json.JSONException
import org.json.JSONObject

class RecaptchaV3EnterpriseProxyless : RecaptchaV3Proxyless() {

    override fun getPostData(): JSONObject? {
        val postData: JSONObject? = super.getPostData()
        try {
            postData?.put("isEnteprise", true)
        } catch (e: JSONException) {
            DebugHelper.out("JSON compilation error: " + e.message, DebugHelper.Type.ERROR)
            return null
        }
        return postData
    }

    override fun getTaskSolution(): TaskResultResponse.SolutionData? = taskInfo?.solution
}
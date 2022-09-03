package app.web.drjacky.anticaptcha.api

import app.web.drjacky.anticaptcha.AnticaptchaBase
import app.web.drjacky.anticaptcha.IAnticaptchaTaskProtocol
import app.web.drjacky.anticaptcha.apiresponse.TaskResultResponse
import app.web.drjacky.anticaptcha.helper.DebugHelper
import org.json.JSONException
import org.json.JSONObject

class HCaptcha : HCaptchaProxyless(), IAnticaptchaTaskProtocol {
    private var proxyType: ProxyTypeOption = AnticaptchaBase.ProxyTypeOption.HTTP
    private var proxyAddress: String? = null
    private var proxyPort: Int? = null
    private var proxyLogin: String? = null
    private var proxyPassword: String? = null
    private var cookies: String? = null

    override fun getPostData(): JSONObject? {
        val postData = JSONObject()
        try {
            postData.put("type", "HCaptchaTask")
            postData.put("websiteURL", websiteUrl.toString())
            postData.put("websiteKey", websiteKey)
            postData.put("proxyType", proxyType.toString().toLowerCase())
            postData.put("proxyAddress", proxyAddress)
            postData.put("proxyPort", proxyPort)
            postData.put("proxyLogin", proxyLogin)
            postData.put("proxyPassword", proxyPassword)
            postData.put("userAgent", userAgent)
            postData.put("cookies", cookies)
            if (enterprisePayload != null) {
                postData.put("enterprisePayload", enterprisePayload)
            }
        } catch (e: JSONException) {
            DebugHelper.out("JSON compilation error: " + e.message, DebugHelper.Type.ERROR)
            return null
        }
        return postData
    }

    fun setProxyType(proxyType: ProxyTypeOption) {
        this.proxyType = proxyType
    }

    fun setProxyAddress(proxyAddress: String?) {
        this.proxyAddress = proxyAddress
    }

    fun setProxyPort(proxyPort: Int?) {
        this.proxyPort = proxyPort
    }

    fun setProxyLogin(proxyLogin: String?) {
        this.proxyLogin = proxyLogin
    }

    fun setProxyPassword(proxyPassword: String?) {
        this.proxyPassword = proxyPassword
    }

    fun setCookies(cookies: String?) {
        this.cookies = cookies
    }

    override fun getTaskSolution(): TaskResultResponse.SolutionData? = taskInfo?.solution
}
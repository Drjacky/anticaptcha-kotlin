package app.web.drjacky.anticaptcha.api

import app.web.drjacky.anticaptcha.apiresponse.TaskResultResponse
import app.web.drjacky.anticaptcha.helper.DebugHelper
import org.json.JSONException
import org.json.JSONObject

class GeeTestV4 : GeeTestV4Proxyless() {
    private var proxyLogin: String? = null
    private var proxyPassword: String? = null
    private var proxyPort: Int? = null
    private var proxyType: ProxyTypeOption? = null
    private var userAgent: String? = null
    private var proxyAddress: String? = null

    fun setProxyLogin(proxyLogin: String?) {
        this.proxyLogin = proxyLogin
    }

    fun setProxyPassword(proxyPassword: String?) {
        this.proxyPassword = proxyPassword
    }

    fun setProxyPort(proxyPort: Int?) {
        this.proxyPort = proxyPort
    }

    fun setProxyType(proxyType: ProxyTypeOption?) {
        this.proxyType = proxyType
    }

    fun setUserAgent(userAgent: String?) {
        this.userAgent = userAgent
    }

    fun setProxyAddress(proxyAddress: String?) {
        this.proxyAddress = proxyAddress
    }

    override fun getPostData(): JSONObject? {
        val postData: JSONObject? = super.getPostData()
        if (proxyType == null || proxyPort == null || proxyPort!! < 1 || proxyPort!! > 65535 || proxyAddress == null || proxyAddress!!.length == 0) {
            DebugHelper.out("Proxy data is incorrect!", DebugHelper.Type.ERROR)
            return null
        }
        try {
            postData?.put("type", "GeeTestTask")
            postData?.put("proxyType", proxyType.toString().toLowerCase())
            postData?.put("proxyAddress", proxyAddress)
            postData?.put("proxyPort", proxyPort)
            postData?.put("proxyLogin", proxyLogin)
            postData?.put("proxyPassword", proxyPassword)
            postData?.put("userAgent", userAgent)
        } catch (e: JSONException) {
            DebugHelper.out("JSON compilation error: " + e.message, DebugHelper.Type.ERROR)
            return null
        }
        return postData
    }

    override fun getTaskSolution(): TaskResultResponse.SolutionData? = taskInfo?.solution
}
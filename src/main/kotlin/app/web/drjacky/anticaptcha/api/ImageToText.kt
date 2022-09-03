package app.web.drjacky.anticaptcha.api

import app.web.drjacky.anticaptcha.AnticaptchaBase
import app.web.drjacky.anticaptcha.IAnticaptchaTaskProtocol
import app.web.drjacky.anticaptcha.apiresponse.TaskResultResponse
import app.web.drjacky.anticaptcha.helper.DebugHelper
import app.web.drjacky.anticaptcha.helper.StringHelper
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class ImageToText : AnticaptchaBase(), IAnticaptchaTaskProtocol {
    var phrase: Boolean? = null
    var case_: Boolean? = null
    var numeric = NumericOption.NO_REQUIREMENTS
    var math: Int? = null
    var minLength: Int? = null
    var maxLength: Int? = null
    private var bodyBase64: String? = null

    enum class NumericOption {
        NO_REQUIREMENTS, NUMBERS_ONLY, ANY_LETTERS_EXCEPT_NUMBERS
    }

    fun setFilePath(filePath: String) {
        val f = File(filePath)
        if (!f.exists() || f.isDirectory) {
            DebugHelper.out("File $filePath not found", DebugHelper.Type.ERROR)
        } else {
            bodyBase64 = StringHelper.imageFileToBase64String(filePath)
            if (bodyBase64 == null) {
                DebugHelper.out(
                    "Could not convert the file \" + value + \" to base64. Is this an image file?",
                    DebugHelper.Type.ERROR
                )
            }
        }
    }

    override fun getPostData(): JSONObject? {
        if (bodyBase64 == null || bodyBase64!!.isEmpty()) {
            return null
        }
        val postData = JSONObject()
        try {
            postData.put("type", "ImageToTextTask")
            postData.put("body", bodyBase64!!.replace("\r", "").replace("\n", ""))
            postData.put("phrase", phrase)
            postData.put("case", case_)
            postData.put(
                "numeric",
                if (numeric == NumericOption.NO_REQUIREMENTS) 0 else if (numeric == NumericOption.NUMBERS_ONLY) 1 else 2
            )
            postData.put("math", math)
            postData.put("minLength", minLength)
            postData.put("maxLength", maxLength)
        } catch (e: JSONException) {
            DebugHelper.out("JSON compilation error: " + e.message, DebugHelper.Type.ERROR)
            return null
        }
        return postData
    }

    override fun getTaskSolution(): TaskResultResponse.SolutionData? = taskInfo?.solution

    fun setBodyBase64(bodyBase64: String?) {
        this.bodyBase64 = bodyBase64
    }
}
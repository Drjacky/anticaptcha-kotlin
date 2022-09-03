package app.web.drjacky.anticaptcha

import app.web.drjacky.anticaptcha.apiresponse.TaskResultResponse
import org.json.JSONObject

interface IAnticaptchaTaskProtocol {
    fun getPostData(): JSONObject?
    fun getTaskSolution(): TaskResultResponse.SolutionData?
}
package app.web.drjacky.anticaptcha

import app.web.drjacky.anticaptcha.AnticaptchaBase.ProxyTypeOption
import app.web.drjacky.anticaptcha.api.*
import app.web.drjacky.anticaptcha.helper.DebugHelper
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.MalformedURLException
import java.net.URL

object Main {
    @Throws(InterruptedException::class, MalformedURLException::class, JSONException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        exampleGetBalance()
        exampleImageToText()
        exampleRecaptchaV2()
        exampleRecaptchaV2Proxyless()
        exampleRecaptchaV3Proxyless()
        exampleRecaptchaV2Enterprise()
        exampleRecaptchaV2EnterpriseProxyless()
        exampleRecaptchaV3EnterpriseProxyless()
        exampleFuncaptcha()
        exampleFuncaptchaProxyless()
        exampleGeeTest()
        exampleGeeTestProxyless()
        exampleHCaptchaProxyless()
        exampleAntiGateTask()
    }

    @Throws(MalformedURLException::class, InterruptedException::class)
    private fun exampleGeeTest() {
        DebugHelper.setVerboseMode(true)
        val api = GeeTest()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.websiteKey = URL("https://auth.geetest.com/").toString()
        api.websiteKey = "b6e21f90a91a3c2d4a31fe84e10d0442"
        // you need to get a new "challenge" each time
        api.websiteChallenge = "cd0b3b5c33fb951ab364d9e13ccd7bf8"

        //optional parameters, read the documentation regarding this
        api.geetestApiServerSubdomain = "optional.subdomain.api.geetest.com"
        api.setGeetestLib("{\"customlibs\":\"url-to-lib.js\"}")


        // proxy access parameters
        api.setProxyType(ProxyTypeOption.HTTP)
        api.setProxyAddress("xx.xxx.xx.xx")
        api.setProxyPort(8282)
        api.setProxyLogin("login")
        api.setProxyPassword("password")
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result CHALLENGE: " + api.getTaskSolution()?.challenge, DebugHelper.Type.SUCCESS)
            DebugHelper.out("Result SECCODE: " + api.getTaskSolution()?.seccode, DebugHelper.Type.SUCCESS)
            DebugHelper.out("Result VALIDATE: " + api.getTaskSolution()?.validate, DebugHelper.Type.SUCCESS)
        }
    }

    @Throws(MalformedURLException::class, InterruptedException::class)
    private fun exampleGeeTestProxyless() {
        DebugHelper.setVerboseMode(true)
        val api = GeeTestProxyless()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.websiteUrl = (URL("https://auth.geetest.com/"))
        api.websiteKey = ("b6e21f90a91a3c2d4a31fe84e10d0442")

        // you need to get a new "challenge" each time
        api.websiteChallenge = ("cd0b3b5c33fb951ab364d9e13ccd7bf8")

        //optional parameters, read the documentation regarding this
        api.geetestApiServerSubdomain = ("optional.subdomain.api.geetest.com")
        api.setGeetestLib("{\"customlibs\":\"url-to-lib.js\"}")
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result CHALLENGE: " + api.getTaskSolution()?.challenge, DebugHelper.Type.SUCCESS)
            DebugHelper.out("Result SECCODE: " + api.getTaskSolution()?.seccode, DebugHelper.Type.SUCCESS)
            DebugHelper.out("Result VALIDATE: " + api.getTaskSolution()?.validate, DebugHelper.Type.SUCCESS)
        }
    }

    @Throws(MalformedURLException::class, InterruptedException::class)
    private fun exampleGeeTestV4Proxyless() {
        DebugHelper.setVerboseMode(true)
        val api = GeeTestV4Proxyless()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.websiteUrl = (URL("https://auth.geetest.com/"))
        api.websiteKey = ("b6e21f90a91a3c2d4a31fe84e10d0442")

        // optional initial parameters
        val additionalInitParameters = JSONObject()
        try {
            additionalInitParameters.put("riskType", "ai")
        } catch (e: Exception) {
            DebugHelper.out("JSON error: " + e.message, DebugHelper.Type.ERROR)
            return
        }
        api.setInitParameters(additionalInitParameters)

        //optional parameters, read the documentation regarding this
        api.geetestApiServerSubdomain = ("optional.subdomain.api.geetest.com")
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result captcha_id: " + api.getTaskSolution()?.captchaId, DebugHelper.Type.SUCCESS)
            DebugHelper.out("Result lot_number: " + api.getTaskSolution()?.lotNumber, DebugHelper.Type.SUCCESS)
            DebugHelper.out("Result pass_token: " + api.getTaskSolution()?.passToken, DebugHelper.Type.SUCCESS)
            DebugHelper.out("Result gen_time: " + api.getTaskSolution()?.genTime, DebugHelper.Type.SUCCESS)
            DebugHelper.out(
                "Result captcha_output: " + api.getTaskSolution()?.captchaOutput,
                DebugHelper.Type.SUCCESS
            )
        }
    }

    @Throws(MalformedURLException::class, InterruptedException::class)
    private fun exampleGeeTestV4() {
        DebugHelper.setVerboseMode(true)
        val api = GeeTestV4()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.websiteUrl = (URL("https://auth.geetest.com/"))
        api.websiteKey = ("b6e21f90a91a3c2d4a31fe84e10d0442")

        // optional initial parameters
        val additionalInitParameters = JSONObject()
        try {
            additionalInitParameters.put("riskType", "ai")
        } catch (e: Exception) {
            DebugHelper.out("JSON error: " + e.message, DebugHelper.Type.ERROR)
            return
        }
        api.setInitParameters(additionalInitParameters)

        //optional parameters, read the documentation regarding this
        api.geetestApiServerSubdomain = ("optional.subdomain.api.geetest.com")


        // proxy access parameters
        api.setProxyType(ProxyTypeOption.HTTP)
        api.setProxyAddress("xx.xxx.xx.xx")
        api.setProxyPort(8282)
        api.setProxyLogin("login")
        api.setProxyPassword("password")
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result captcha_id: " + api.getTaskSolution()?.captchaId, DebugHelper.Type.SUCCESS)
            DebugHelper.out("Result lot_number: " + api.getTaskSolution()?.lotNumber, DebugHelper.Type.SUCCESS)
            DebugHelper.out("Result pass_token: " + api.getTaskSolution()?.passToken, DebugHelper.Type.SUCCESS)
            DebugHelper.out("Result gen_time: " + api.getTaskSolution()?.genTime, DebugHelper.Type.SUCCESS)
            DebugHelper.out(
                "Result captcha_output: " + api.getTaskSolution()?.captchaOutput,
                DebugHelper.Type.SUCCESS
            )
        }
    }

    @Throws(InterruptedException::class)
    private fun exampleImageToText() {
        DebugHelper.setVerboseMode(true)
        val api = ImageToText()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.setFilePath("captcha.jpg")
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution()?.text, DebugHelper.Type.SUCCESS)
        }
    }

    @Throws(MalformedURLException::class, InterruptedException::class)
    private fun exampleRecaptchaV2Proxyless() {
        DebugHelper.setVerboseMode(true)
        val api = RecaptchaV2Proxyless()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.websiteUrl = (URL("http://http.myjino.ru/recaptcha/test-get.php"))
        api.websiteKey = ("6Lc_aCMTAAAAABx7u2W0WPXnVbI_v6ZdbM6rYf16")
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution()?.gRecaptchaResponse, DebugHelper.Type.SUCCESS)
        }
    }

    @Throws(MalformedURLException::class, InterruptedException::class)
    private fun exampleHCaptchaProxyless() {
        DebugHelper.setVerboseMode(true)
        val api = HCaptchaProxyless()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.websiteUrl = (URL("http://democaptcha.com/"))
        api.websiteKey = ("51829642-2cda-4b09-896c-594f89d700cc")
        api.userAgent = (
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/52.0.2743.116"
                )

        // uncomment to tell API that HCaptcha is in invisible mode
//        api.setIsInvisible(true);

        // uncomment and use for HCaptcha Enterprise version if you need to set parameters like rqdata, sentry, apiEndpoint, endpoint, reportapi, assethost, imghost
//        JSONObject enterprisePayload = new JSONObject();
//        try {
//            enterprisePayload.put("rqdata", "rqdata from the target website");
//            enterprisePayload.put("sentry", Boolean.TRUE);
//            enterprisePayload.put("apiEndpoint", "https://...");
//        } catch (Exception e) {
//            DebugHelper.out("JSON error: "+e.message, DebugHelper.Type.ERROR);
//            return;
//        }
//        api.setEnterprisePayload(enterprisePayload);
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution()?.gRecaptchaResponse, DebugHelper.Type.SUCCESS)
        }
    }

    @Throws(MalformedURLException::class, InterruptedException::class)
    private fun exampleRecaptchaV3Proxyless() {
        DebugHelper.setVerboseMode(true)
        val api = RecaptchaV3Proxyless()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.websiteUrl = (URL("http://www.supremenewyork.com"))
        api.websiteKey = ("6Leva6oUAAAAAMFYqdLAI8kJ5tw7BtkHYpK10RcD")
        api.pageAction = ("testPageAction")
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution()?.gRecaptchaResponse, DebugHelper.Type.SUCCESS)
        }
    }

    @Throws(MalformedURLException::class, InterruptedException::class)
    private fun exampleRecaptchaV2() {
        DebugHelper.setVerboseMode(true)
        val api = RecaptchaV2()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.websiteUrl = (URL("http://http.myjino.ru/recaptcha/test-get.php"))
        api.websiteKey = ("6Lc_aCMTAAAAABx7u2W0WPXnVbI_v6ZdbM6rYf16")
        api.setUserAgent(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/52.0.2743.116"
        )

        // proxy access parameters
        api.setProxyType(ProxyTypeOption.HTTP)
        api.setProxyAddress("xx.xxx.xx.xx")
        api.setProxyPort(8282)
        api.setProxyLogin("login")
        api.setProxyPassword("password")
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution()?.gRecaptchaResponse, DebugHelper.Type.SUCCESS)
        }
    }

    @Throws(MalformedURLException::class, InterruptedException::class)
    private fun exampleRecaptchaV2Enterprise() {
        DebugHelper.setVerboseMode(true)
        val api = RecaptchaV2Enterprise()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.websiteUrl = (URL("http://http.myjino.ru/recaptcha/test-get.php"))
        api.websiteKey = ("6Lc_aCMTAAAAABx7u2W0WPXnVbI_v6ZdbM6rYf16")
        api.setUserAgent(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/52.0.2743.116"
        )

        // proxy access parameters
        api.setProxyType(ProxyTypeOption.HTTP)
        api.setProxyAddress("xx.xxx.xx.xx")
        api.setProxyPort(8282)
        api.setProxyLogin("login")
        api.setProxyPassword("password")
        val enterprisePayload = JSONObject()
        try {
            enterprisePayload.put("s", "SOME_UNDOCUMENTED_TOKEN_VALUE")
        } catch (e: Exception) {
            DebugHelper.out("JSON error: " + e.message, DebugHelper.Type.ERROR)
            return
        }
        api.enterprisePayload = (enterprisePayload)
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution()?.gRecaptchaResponse, DebugHelper.Type.SUCCESS)
        }
    }

    @Throws(MalformedURLException::class, InterruptedException::class)
    private fun exampleRecaptchaV2EnterpriseProxyless() {
        DebugHelper.setVerboseMode(true)
        val api = RecaptchaV2EnterpriseProxyless()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.websiteUrl = (URL("http://http.myjino.ru/recaptcha/test-get.php"))
        api.websiteKey = ("6Lc_aCMTAAAAABx7u2W0WPXnVbI_v6ZdbM6rYf16")
        val enterprisePayload = JSONObject()
        try {
            enterprisePayload.put("s", "SOME_UNDOCUMENTED_TOKEN_VALUE")
        } catch (e: Exception) {
            DebugHelper.out("JSON error: " + e.message, DebugHelper.Type.ERROR)
            return
        }
        api.enterprisePayload = (enterprisePayload)
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution()?.gRecaptchaResponse, DebugHelper.Type.SUCCESS)
        }
    }

    @Throws(MalformedURLException::class, InterruptedException::class)
    private fun exampleRecaptchaV3EnterpriseProxyless() {
        DebugHelper.setVerboseMode(true)
        val api = RecaptchaV3EnterpriseProxyless()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.websiteUrl = (URL("http://www.supremenewyork.com"))
        api.websiteKey = ("6Leva6oUAAAAAMFYqdLAI8kJ5tw7BtkHYpK10RcD")
        api.pageAction = ("testPageAction")
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution()?.gRecaptchaResponse, DebugHelper.Type.SUCCESS)
        }
    }

    private fun exampleGetBalance() {
        DebugHelper.setVerboseMode(true)
        val api = ImageToText()
        api.setClientKey("1234567890123456789012")
        val balance: Double? = api.balance
        if (balance == null) {
            DebugHelper.out("GetBalance() failed. " + api.getErrorMessage(), DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Balance: $balance", DebugHelper.Type.SUCCESS)
        }
    }

    @Throws(MalformedURLException::class, InterruptedException::class)
    private fun exampleFuncaptcha() {
        DebugHelper.setVerboseMode(true)
        val api = FunCaptcha()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.websiteUrl = (URL("http://http.myjino.ru/funcaptcha_test/"))
        api.websitePublicKey = ("DE0B0BB7-1EE4-4D70-1853-31B835D4506B")
        api.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116")

        //optional parameters, be careful!
        api.apiSubdomain = ("custom-domain-api.arkoselabs.com")
        api.dataBlob = ("{\"blob\":\"DATA_BLOB_VALUE_HERE\"}")

        // proxy access parameters
        api.setProxyType(ProxyTypeOption.HTTP)
        api.setProxyAddress("xx.xxx.xx.xx")
        api.setProxyPort(8282)
        api.setProxyLogin("login")
        api.setProxyPassword("password")
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution()?.token, DebugHelper.Type.SUCCESS)
        }
    }

    @Throws(MalformedURLException::class, InterruptedException::class)
    private fun exampleFuncaptchaProxyless() {
        DebugHelper.setVerboseMode(true)
        val api = FunCaptchaProxyless()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.websiteUrl = (URL("http://http.myjino.ru/funcaptcha_test/"))
        api.websitePublicKey = ("DE0B0BB7-1EE4-4D70-1853-31B835D4506B")

        //optional parameters, be careful!
        api.apiSubdomain = ("custom-domain-api.arkoselabs.com")
        api.dataBlob = ("{\"blob\":\"DATA_BLOB_VALUE_HERE\"}")
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result: " + api.getTaskSolution()?.token, DebugHelper.Type.SUCCESS)
        }
    }

    @Throws(MalformedURLException::class, InterruptedException::class)
    private fun exampleAntiGateTask() {
        DebugHelper.setVerboseMode(true)
        val api = AntiGateTask()
        api.setClientKey("1234567890123456789012")

        /*
         *  Specify softId to earn 10% commission with your app.
         *  Get your softId here:
         *  https://anti-captcha.com/clients/tools/devcenter
         */api.softId = 0
        api.websiteUrl = (URL("http://antigate.com/logintest.php"))
        api.templateName = ("Sign-in and wait for control text")
        val variables = JSONObject()
        try {
            variables.put("login_input_css", "#login")
            variables.put("login_input_value", "the login")
            variables.put("password_input_css", "#password")
            variables.put("password_input_value", "the password")
            variables.put("control_text", "You have been logged successfully")
        } catch (e: Exception) {
            DebugHelper.out("JSON exception: " + e.message, DebugHelper.Type.ERROR)
            return
        }
        api.variables = (variables)

        // use this to collect data from other domains
        val domainsOfInterest = JSONArray()
        domainsOfInterest.put("domain1.com")
        domainsOfInterest.put("domain2.com")
        api.domainsOfInterest = (domainsOfInterest)

//        api.setProxyAddress("xx.xxx.xx.xx");
//        api.setProxyPort(8282);
//        api.setProxyLogin("login");
//        api.setProxyPassword("password");
        if (!api.createTask()) {
            DebugHelper.out(
                "API v2 send failed. " + api.getErrorMessage(),
                DebugHelper.Type.ERROR
            )
        } else if (!api.waitForResult()) {
            DebugHelper.out("Could not solve the captcha.", DebugHelper.Type.ERROR)
        } else {
            DebugHelper.out("Result: ", DebugHelper.Type.SUCCESS)
            DebugHelper.out("Cookies: ", DebugHelper.Type.SUCCESS)
            DebugHelper.out(api.getTaskSolution()?.cookies.toString(), DebugHelper.Type.SUCCESS)
            DebugHelper.out("localStorage: ", DebugHelper.Type.SUCCESS)
            DebugHelper.out(api.getTaskSolution()?.localStorage.toString(), DebugHelper.Type.SUCCESS)
            DebugHelper.out("fingerprint: ", DebugHelper.Type.SUCCESS)
            DebugHelper.out(api.getTaskSolution()?.fingerprint.toString(), DebugHelper.Type.SUCCESS)
            DebugHelper.out("url: ", DebugHelper.Type.SUCCESS)
            DebugHelper.out(api.getTaskSolution()?.url, DebugHelper.Type.SUCCESS)
        }
    }
}
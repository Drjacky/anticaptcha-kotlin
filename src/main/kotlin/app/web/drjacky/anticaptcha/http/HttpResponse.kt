package app.web.drjacky.anticaptcha.http

import org.apache.http.client.protocol.HttpClientContext

class HttpResponse {
    var body: String? = null
        private set
    private var headers: MutableMap<String, String?> = HashMap()
    private var cookies: MutableMap<String, String> = HashMap()
    var charset: String? = null
        private set
    var contentType: String? = null
        private set
    var httpCode: Int? = null
        private set
    var httpMessage: String? = null
        private set

    constructor(
        body: String?,
        headers: MutableMap<String, String?>,
        cookies: MutableMap<String, String>,
        charset: String?,
        contentType: String?,
        httpCode: Int?,
        statusMessage: String?
    ) {
        this.body = body
        this.headers = headers
        this.cookies = cookies
        this.charset = charset
        this.contentType = contentType
        this.httpCode = httpCode
        httpMessage = statusMessage
    }

    constructor(body: String?, apacheHttpResponse: org.apache.http.HttpResponse, apacheHttpContext: HttpClientContext) {
        this.body = body

        // HEADERS
        for (header in apacheHttpResponse.allHeaders) {
            headers[header.name] = header.value
        }

        // COOKIES
        for (cookie in apacheHttpContext.cookieStore.cookies) {
            cookies[cookie.name] = cookie.value
        }

        // Content-Type and charset:
        if (headers["Content-Type"] != null) {
            val contentTypeHeaderSplitted =
                headers["Content-Type"]!!.split("; charset=".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            contentType = contentTypeHeaderSplitted[0]
            if (contentTypeHeaderSplitted.size > 1) {
                charset = contentTypeHeaderSplitted[1]
            }
        }

        // STATUS CODE & MESSAGE
        httpCode = apacheHttpResponse.statusLine.statusCode
        httpMessage = apacheHttpResponse.statusLine.reasonPhrase
    }

    fun getHeaders(): Map<String, String?> {
        return headers
    }

    private val headersWithoutDots: Map<String, String?>
        private get() {
            val newHeaders: MutableMap<String, String?> = HashMap()
            for ((key, value) in headers) {
                newHeaders[key.replace(".", "\\uff0E")] = value
            }
            return newHeaders
        }

    fun getCookies(): Map<String, String> {
        return cookies
    }

    private val cookiesWithoutDots: Map<String, String>
        private get() {
            val newCookies: MutableMap<String, String> = HashMap()
            for ((key, value) in cookies) {
                newCookies[key.replace(".", "\\uff0E")] = value
            }
            return newCookies
        }
}
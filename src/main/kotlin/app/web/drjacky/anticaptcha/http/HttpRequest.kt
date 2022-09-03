package app.web.drjacky.anticaptcha.http

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

class HttpRequest(val url: String) {
    var rawPost: String? = null
    var timeout = 60000 // milliseconds
    var maxBodySize = 0 // 0 = unlimited, in bytes
    var isFollowRedirects = true // does not work now due to moving from JSOUP to ApacheHttpClient
    var isValidateTLSCertificates = false
    private var proxy: MutableMap<String, String>? =
        null //new HashMap<String, String>() {{put("host", "192.168.0.168"); put("port", "8888");}};
    private var cookies: MutableMap<String, String> = HashMap()
    private val headers: MutableMap<String, String> = HashMap<String, String>().apply {
        put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
        put("Accept-Encoding", "gzip, deflate, sdch")
        put("Accept-Language", "ru-RU,en;q=0.8,ru;q=0.6")
    }
    var isNoCache = false
    private val acceptedHttpCodes: MutableSet<Int> = HashSet<Int>().apply { add(200) }
    private val urlCuttedForHash: String? = null
    private val urlChangingParts = arrayOf(
        "session_id",
        "sessionid",
        "timestamp"
    )

    fun getProxy(): Map<String, String>? {
        return proxy
    }

    fun setProxy(proxyHost: String?, proxyPort: Int) {
        proxy = HashMap()
        proxy?.let {
            it["host"] = proxyHost!!
            it["port"] = proxyPort.toString()
        }
    }

    fun getReferer(): String? {
        return if (headers["Referer"] != null) {
            headers["Referer"]
        } else null
    }

    fun setReferer(referer: String) {
        headers["Referer"] = referer
    }

    fun getHeaders(): Map<String, String?> {
        return headers
    }

    fun getCookies(): Map<String, String> {
        return cookies
    }

    fun getAcceptedHttpCodes(): Set<Int> {
        return acceptedHttpCodes
    }

    @Throws(Exception::class)
    fun getUrlWithoutChangingParts(url: String): String {
        var url = url
        url = url.lowercase(Locale.getDefault())
        var newUrl = url
        for (partToRemove in urlChangingParts) {
            val splitted = newUrl.split(partToRemove.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (splitted.size == 1) {
                continue
            }
            val firstPiece = splitted[0]
            var secondPiece = splitted[1]
            if (splitted.size > 2) {
                val splitted2 = arrayOfNulls<String>(splitted.size - 1)
                System.arraycopy(splitted, 1, splitted2, 0, splitted2.size)
                secondPiece = java.lang.String.join(partToRemove, *splitted2)
            }
            var breakpointPos = secondPiece.length
            if (secondPiece.contains("?")) {
                breakpointPos = secondPiece.indexOf("?")
            } else if (secondPiece.contains("&")) {
                breakpointPos = secondPiece.indexOf("&")
            }
            newUrl = firstPiece + secondPiece.substring(breakpointPos)
        }
        return if (newUrl == url) {
            newUrl
        } else {
            getUrlWithoutChangingParts(newUrl)
        }
    }

    @Throws(UnsupportedEncodingException::class)
    fun addToPost(key: String?, value: String?) {
        if (rawPost == null) {
            rawPost = ""
        } else {
            rawPost += "&"
        }
        rawPost += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8")
        addHeader("Content-Type", "application/x-www-form-urlencoded")
    }

    fun setCookies(cookies: MutableMap<String, String>) {
        this.cookies = cookies
    }

    fun addCookie(key: String, value: String) {
        cookies[key] = value
    }

    fun addAcceptedHttpCode(httpCode: Int) {
        acceptedHttpCodes.add(httpCode)
    }

    fun addHeader(key: String, value: String) {
        headers[key] = value
    }
}
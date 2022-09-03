package app.web.drjacky.anticaptcha.helper

import app.web.drjacky.anticaptcha.http.HttpRequest
import app.web.drjacky.anticaptcha.http.HttpResponse
import org.apache.http.HttpHost
import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.DefaultProxyRoutePlanner
import org.apache.http.impl.cookie.BasicClientCookie
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.StringWriter
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object HttpHelper {
    @Throws(Exception::class)
    fun download(request: HttpRequest): HttpResponse {
        val cookieStore = BasicCookieStore()
        if (request.getCookies() != null) {
            for ((key, value) in request.getCookies().entries) {
                val cookie = BasicClientCookie(key, value)
                cookie.domain = getCookieDomain(request.url)
                cookieStore.addCookie(cookie)
            }
        }
        val httpClientBuilder: HttpClientBuilder

        // if "https:" and don't need to check certificates
        if (!request.isValidateTLSCertificates && request.url.toLowerCase()[4] == 's') {
            httpClientBuilder = HttpsClientBuilderGiver.INSTANCE.httpsClientBuilder
        } else {
            httpClientBuilder = HttpClientBuilder.create()
        }
        if (request.getCookies() != null) {
            httpClientBuilder.setDefaultCookieStore(cookieStore)
        }
        if (request.getProxy() != null) {
            httpClientBuilder.setRoutePlanner(
                DefaultProxyRoutePlanner(
                    HttpHost(
                        request.getProxy()?.get("host"), request.getProxy()?.get("port")!!.toInt()
                    )
                )
            )
        }
        val httpClient: HttpClient = if (request.isFollowRedirects) {
            httpClientBuilder.build()
        } else {
            httpClientBuilder.disableRedirectHandling().build()
        }
        val response: org.apache.http.HttpResponse
        val apacheHttpRequest: HttpRequestBase
        if (request.rawPost == null) {
            apacheHttpRequest = HttpGet(request.url)
        } else {
            apacheHttpRequest = HttpPost(request.url)
            (apacheHttpRequest as HttpPost).entity = StringEntity(request.rawPost, "UTF-8")
        }
        val context: HttpClientContext = HttpClientContext.create()
        apacheHttpRequest.config = RequestConfig.custom()
            .setConnectionRequestTimeout(request.timeout)
            .setConnectTimeout(request.timeout)
            .setSocketTimeout(request.timeout)
            .build()
        for ((key, value) in request.getHeaders().entries) {
            apacheHttpRequest.addHeader(key, value)
        }
        response = httpClient.execute(apacheHttpRequest, context)
        var charset = "utf8"
        if (response.getHeaders("Content-Type").size !== 0) {
            val charsetSplitted: List<String> =
                response.getHeaders("Content-Type")[0].value.split("; charset=")
            if (charsetSplitted.size == 2) {
                charset = charsetSplitted[1]
            }
        }
        return HttpResponse(
            InputOutput.INSTANCE.toString(response.entity.content, charset, request.maxBodySize),
            response,
            context
        )
    }

    private fun getCookieDomain(url: String): String {
        return "." + url.split("://".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].split("/".toRegex())
            .dropLastWhile { it.isEmpty() }.toTypedArray()[0]
    }

    // Костыль для получения HttpClient'а для HTTPS
    private enum class HttpsClientBuilderGiver {
        INSTANCE;// SSL and TLS - both work
        //            SSLContext sslcontext = SSLContextexts.custom().useSSL().build(); // works, too
        //            sslcontext.init(null, new X509TrustManager[]{new HttpsTrustManager()}, new SecureRandom()); // works, too
        /**
         * Apache HttpClient which will work well with any (even invalid and expired) HTTPS
         * certificate.
         */
        @get:Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
        val httpsClientBuilder: HttpClientBuilder
            get() {
                val sslcontext = SSLContext.getInstance("TLS") // SSL and TLS - both work
                //            SSLContext sslcontext = SSLContextexts.custom().useSSL().build(); // works, too
                sslcontext.init(arrayOfNulls(0), arrayOf<TrustManager>(HttpsTrustManager()), SecureRandom())
                //            sslcontext.init(null, new X509TrustManager[]{new HttpsTrustManager()}, new SecureRandom()); // works, too
                SSLContext.setDefault(sslcontext)
                return HttpClients.custom()
                    .setSSLSocketFactory(SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier()))
            }

        private inner class HttpsTrustManager : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(arg0: Array<X509Certificate>, arg1: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(arg0: Array<X509Certificate>, arg1: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
    }

    private enum class InputOutput {
        INSTANCE;

        /**
         * The default buffer size to use.
         */
        private val DEFAULT_BUFFER_SIZE = 1024 * 4

        /**
         * Get the contents of an `InputStream` as a String using the specified character
         * encoding.
         *
         * Character encoding names can be found at [IANA](http://www.iana.org/assignments/character-sets).
         *
         *  This method buffers the input internally, so there is no need to use a
         * `BufferedInputStream`.
         *
         * @param input    the `InputStream` to read from
         * @param encoding the encoding to use, null means platform default
         * @param bytesMax the amount of bytes you want to get, when exceeded, download will stop
         * @return the requested String
         * @throws NullPointerException if the input is null
         * @throws IOException          if an I/O jsonFieldParseError occurs
         */
        @Throws(IOException::class)
        fun toString(input: InputStream?, encoding: String?, bytesMax: Int): String {
            val output = StringWriter()
            val `in` = InputStreamReader(input, encoding)
            val buffer = CharArray(DEFAULT_BUFFER_SIZE)
            var count: Long = 0
            var n = 0
            while (-1 != `in`.read(buffer).also { n = it }) {
                output.write(buffer, 0, n)
                count += n.toLong()
                if (bytesMax > 0 && count >= bytesMax) {
                    break
                }
            }
            return output.toString()
        }
    }
}
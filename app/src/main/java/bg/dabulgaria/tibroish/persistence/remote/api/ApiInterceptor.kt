package bg.dabulgaria.tibroish.persistence.remote.api

import bg.dabulgaria.tibroish.persistence.remote.ApiException
import com.android.volley.toolbox.HttpHeaderParser
import okhttp3.Interceptor
import okhttp3.Response
import java.io.UnsupportedEncodingException

class ApiInterceptor (private val userAgent: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()

        // Customize the request
        val request = original.newBuilder()
                //.header(ApiHeader.AcceptEncoding, "gzip")
                //.header(ApiHeader.ContentType, "application/json")
                //.header(ApiHeader.UserAgent, userAgent)
                .build()

        val response = chain.proceed(request)
        if (!response.isSuccessful)
            throwError(response)

        return response;
    }

    // region parse API error
    private fun throwError(response: Response) {

        val networkResponse = response.networkResponse
        val networkResponseBody = response.body
        var responseData: String =""
        if (networkResponse != null && networkResponseBody != null) {

            try {

                val headersMapMap = mutableMapOf<String, String>()
                networkResponse.headers.names().map { name ->
                    headersMapMap.put(name, networkResponse.headers.get(name)
                            ?: "")
                }
                responseData = String(networkResponseBody.bytes(),
                        charset(HttpHeaderParser.parseCharset(headersMapMap, Charsets.UTF_8.name())))

            } catch (e: UnsupportedEncodingException) {

                e.printStackTrace()
            }
        }

        throw ApiException(response, responseData)
    }
}
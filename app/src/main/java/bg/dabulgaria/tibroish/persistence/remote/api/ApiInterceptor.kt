package bg.dabulgaria.tibroish.persistence.remote.api

import bg.dabulgaria.tibroish.persistence.remote.ApiConflictException
import bg.dabulgaria.tibroish.persistence.remote.ApiException
import bg.dabulgaria.tibroish.persistence.remote.AuthException
import com.android.volley.toolbox.HttpHeaderParser
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class ApiInterceptor (private val userAgent: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()

        // Customize the request
        val request = original.newBuilder()
                .header(ApiHeader.AcceptEncoding, "gzip")
                .header(ApiHeader.ContentType, "application/json")
                .header(ApiHeader.UserAgent, userAgent)
                .build()

        val response = chain.proceed(request)
        var success =response.isSuccessful
        if (!success)
            throwError(response)

        return response
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

        val code =networkResponse?.code ?: 500
        val message: String? = getMessageOrNull(responseData)

        val exception = when (code) {
            UNAUTHORIZED -> AuthException(response, responseData, message)
            CONFLICT -> ApiConflictException(response, responseData, message)
            else -> ApiException(response, responseData, message)
        }

        throw exception
    }

    private fun getMessageOrNull(responseData: String): String? {
        var message: String? = null
        try {
            val jsonObject = JSONObject(responseData)
            message = getMessageFromJsonArray(jsonObject)
            if (message == null) {
                message = jsonObject.getString("message")
            }
        } catch (e: JSONException) {
        }
        return message
    }

    private fun getMessageFromJsonArray(jsonObject: JSONObject): String? {
        val jsonArray: JSONArray?
        try {
            jsonArray = jsonObject.getJSONArray("message")
        } catch (e: JSONException) {
            return null
        }
        if (jsonArray.length() == 0) {
            return null
        }
        val sb = StringBuilder()
        for (i in 0 until jsonArray.length()) {
            sb.append(removeQuotesFromStringStartAndEnd(jsonArray.getString(i)))
            if (i != jsonArray.length()-1) {
                sb.append("\n")
            }
        }
        return sb.toString()
    }

    private fun removeQuotesFromStringStartAndEnd(string: String): String {
        return string.replace(Regex("^\"+|\"+$"), "")
    }

    companion object{

        const val UNAUTHORIZED = 401
        const val CONFLICT = 409
    }
}

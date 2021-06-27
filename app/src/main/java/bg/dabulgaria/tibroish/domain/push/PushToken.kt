package bg.dabulgaria.tibroish.domain.push

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SendTokenRequest(
        @SerializedName("token") private val token: String) : Serializable

class SendTokenResponse(
        @SerializedName("data") val data: String) : Serializable
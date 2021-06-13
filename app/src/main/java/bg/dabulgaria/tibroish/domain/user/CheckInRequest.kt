package bg.dabulgaria.tibroish.domain.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SendCheckInRequest constructor(  @SerializedName("section") val section: String/*section.id*/):Serializable

class SendCheckInResponse constructor(  @SerializedName("section") val section: String/*section.id*/):Serializable

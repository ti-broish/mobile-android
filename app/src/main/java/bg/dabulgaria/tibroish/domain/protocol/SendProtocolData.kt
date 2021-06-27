package bg.dabulgaria.tibroish.domain.protocol

import bg.dabulgaria.tibroish.domain.locations.SectionRemote
import bg.dabulgaria.tibroish.domain.image.PictureDto
import com.google.gson.JsonDeserializer
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

enum class ProtocolStatusRemote constructor(val stringValue:String){

    @SerializedName("received")
    Received("received"),
    @SerializedName("rejected")
    Rejected("rejected"),
    @SerializedName("replaced")
    Replaced("replaced"),
    @SerializedName("ready")
    Ready("ready"),
    @SerializedName("approved")
    Approved("approved"),
    @SerializedName("publishing")
    Publishing("publishing"),
    @SerializedName("published")
    Published("published"),
    @SerializedName("processed")
    Processed("processed"),
    Undefined("undefined");

    companion object {

        private val lookup = HashMap<String, ProtocolStatusRemote>()

        init {

            for (status in EnumSet.allOf(ProtocolStatusRemote::class.java))
                lookup.put(status.stringValue, status)
        }

        operator fun get(strValue: String): ProtocolStatusRemote {
            return lookup[strValue] ?: Undefined
        }

        val deserializer: JsonDeserializer<ProtocolStatusRemote?>
            get() = JsonDeserializer { json, _, _ ->
                val enumValue = json?.asString ?: ""
                val value = ProtocolStatusRemote[enumValue]
                return@JsonDeserializer value
            }
    }
}

class SendProtocolRequest(
        @SerializedName("section")
        val section: String,//section.id
        @SerializedName("pictures")//picture server id's
        val pictures:List<String>):Serializable

class ProtocolRemote(
        val id: String,
        val section: SectionRemote,
        val pictures: List<PictureDto>,
        val status: ProtocolStatusRemote,
        val statusLocalized: String,
        var errorMessage: String?) : Serializable
package bg.dabulgaria.tibroish.domain.violation

import androidx.room.TypeConverter
import bg.dabulgaria.tibroish.domain.protocol.ProtocolStatusRemote
import com.google.gson.JsonDeserializer
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.HashMap

enum class ViolationRemoteStatus(val stringValue:String){

    @SerializedName("received") Received("received"),
    @SerializedName("processing") Processing("processing"),
    @SerializedName("processed") Processed("processed"),
    @SerializedName("approved") Approved("approved"),
    @SerializedName("rejected") Rejected("rejected"),
    Undefined("undefined");

    companion object {

        private val lookup = HashMap<String, ViolationRemoteStatus>()

        init {

            for (status in EnumSet.allOf(ViolationRemoteStatus::class.java))
                lookup.put(status.stringValue, status)
        }

        operator fun get(strValue: String): ViolationRemoteStatus? {
            return lookup[strValue] ?: Undefined
        }

        val deserializer: JsonDeserializer<ViolationRemoteStatus?>
            get() = JsonDeserializer { json, _, _ ->
                val enumValue = json?.asString ?: ""
                val value = ViolationRemoteStatus[enumValue]
                return@JsonDeserializer value
            }
    }
}

class ViolationRemoteStatusTypeConverter {

    @TypeConverter
    fun toViolationRemoteStatus(value: String?): ViolationRemoteStatus? = if(value==null) null else ViolationRemoteStatus[value]

    @TypeConverter
    fun fromViolationRemoteStatus(value: ViolationRemoteStatus?) = value?.stringValue
}
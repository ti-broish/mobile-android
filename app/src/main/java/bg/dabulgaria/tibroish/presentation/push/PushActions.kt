package bg.dabulgaria.tibroish.presentation.push

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


enum class PushActionType constructor(val stringValue: String) : Serializable {

    @SerializedName("ShowScreen")
    ShowScreen("ShowScreen");

    companion object {

        private val lookup = hashMapOf<String, PushActionType>()

        init {

            for (action in EnumSet.allOf(PushActionType::class.java))
                lookup.put(action.stringValue, action)
        }

        operator fun get(stringValue: String): PushActionType? = lookup[stringValue]
    }
}

enum class PushActionValuesShowScreen constructor(val stringValue: String) : Serializable {

    @SerializedName("Protocols")
    Protocols("Protocols"),

    @SerializedName("ProtocolDetails")
    ProtocolDetails("ProtocolDetails"),

    @SerializedName("Violations")
    Violations("Violations"),

    @SerializedName("ViolationDetails")
    ViolationDetails("ViolationDetails");

    companion object {

        private val lookup = hashMapOf<String, PushActionValuesShowScreen>()

        init {

            for (action in EnumSet.allOf(PushActionValuesShowScreen::class.java))
                lookup.put(action.stringValue, action)
        }

        operator fun get(stringValue: String): PushActionValuesShowScreen? = lookup[stringValue]
    }
}
package bg.dabulgaria.tibroish.domain.send

import androidx.room.TypeConverter
import java.util.*

enum class SendStatus constructor(val code:Int){
    New(0),
    Send(1),
    Sending(2),
    SendError(3),
    SendErrorInvalidSection(4);

    companion object {

        private val lookup = hashMapOf<Int, SendStatus>()

        init {

            for (status in EnumSet.allOf(SendStatus::class.java))
                lookup.put(status.code, status)
        }

        operator fun get(code: Int): SendStatus? = lookup[code]
    }
}

class SendStatusTypeConverter {
    @TypeConverter
    fun toProtocolStatus(value: Int):SendStatus? = SendStatus[value]

    @TypeConverter
    fun fromProtocolStatus(value: SendStatus) = value.code
}
package bg.dabulgaria.tibroish.domain.send

import androidx.room.TypeConverter
import java.util.*

enum class ImageSendStatus(val code:Int){
    NotProcessed(0), Copied(1), Uploaded(2), Replace(3);

    companion object {

        private val lookup = hashMapOf<Int, ImageSendStatus>()

        init {

            for (status in EnumSet.allOf(ImageSendStatus::class.java))
                lookup.put(status.code, status)
        }

        operator fun get(code: Int): ImageSendStatus? = lookup[code]
    }
}

class ImageSendStatusTypeConverter {
    @TypeConverter
    fun toUploadStatus(value: Int): ImageSendStatus? = ImageSendStatus.get( value )

    @TypeConverter
    fun fromUploadStatus(value: ImageSendStatus) = value.code
}
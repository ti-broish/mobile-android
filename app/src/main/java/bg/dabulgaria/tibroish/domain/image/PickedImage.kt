package bg.dabulgaria.tibroish.domain.image

import androidx.room.TypeConverter
import java.io.Serializable
import java.util.*

enum class PickedImageSource (val code:Int){
    None(0),
    Gallery(1),
    Camera(2);

    companion object {

        private val lookup = hashMapOf<Int, PickedImageSource>()

        init {

            for (source in EnumSet.allOf(PickedImageSource::class.java))
                lookup.put(source.code, source)
        }

        operator fun get(code: Int): PickedImageSource? = lookup[code]
    }
}

class PickedImageSourceTypeConverter {
    @TypeConverter
    fun toPickedImageSource(value: Int): PickedImageSource? = PickedImageSource.get( value )

    @TypeConverter
    fun fromPickedImageSource(value: PickedImageSource) = value.code
}

data class PickedImage constructor(val id:String,
                                   val source:PickedImageSource,
                                   val imageFilePath:String,
                                   val width:Int,
                                   val height:Int,
                                   val dateTaken:Date) :Serializable
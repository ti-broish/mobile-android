package bg.dabulgaria.tibroish.domain.image

import java.io.Serializable
import java.util.*

enum class PickedImageSource (val type:Int){
    None(0),
    Gallery(1),
    Camera(2)
}

data class PickedImage constructor(val id:String,
                                   val source:PickedImageSource,
                                   val imageFilePath:String,
                                   val width:Int,
                                   val height:Int,
                                   val dateTaken:Date) :Serializable {
}
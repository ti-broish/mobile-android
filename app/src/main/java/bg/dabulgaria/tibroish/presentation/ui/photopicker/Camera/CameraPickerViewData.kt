package bg.dabulgaria.tibroish.presentation.ui.photopicker.Camera//package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import java.io.Serializable
import java.util.*

data class PhotoItem(val uuid:String,
                     val providerId:String,
                     val uri:String,
                     val width:Int,
                     val height:Int,
                     val dateTaken: Date?,
                     val folderName: String,
                     val isSelected: Boolean): Serializable

data class CameraPickerViewData(val protocolId:Long): Serializable {

    val photoItems = mutableListOf<PhotoItem>()
}

class CameraPickerConstants{
    companion object {
        val VIEW_DATA_KEY = "CameraPickerConstants.AddProtocolViewData"
    }
}

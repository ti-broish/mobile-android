package bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery//package bg.dabulgaria.tibroish.presentation.ui.protocol.list

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

data class PhotoPickerViewData(val protocolId:Long): Serializable {

    val photoItems = mutableListOf<PhotoItem>()
}

class PhotoPickerConstants{
    companion object {
        val VIEW_DATA_KEY = "PhotoPickerConstants.AddProtocolViewData"
    }
}

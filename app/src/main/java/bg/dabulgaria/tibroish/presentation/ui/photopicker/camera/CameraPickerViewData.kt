package bg.dabulgaria.tibroish.presentation.ui.photopicker.camera//package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import bg.dabulgaria.tibroish.domain.image.PickedImage
import java.io.Serializable
import java.util.*

data class PhotoItem(val isSelected: Boolean): Serializable

data class CameraPickerViewData(val protocolId:Long): Serializable {

    val photoItems = mutableListOf<PickedImage>()
}

class CameraPickerConstants{
    companion object {
        val VIEW_DATA_KEY = "CameraPickerConstants.AddProtocolViewData"
    }
}

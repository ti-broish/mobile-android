package bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery//package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import bg.dabulgaria.tibroish.domain.image.PickedImageSource
import java.io.Serializable
import java.util.*

open class PhotoId(val id: String,
                   val source: PickedImageSource) : Serializable

class PhotoItem(id: String,
                source: PickedImageSource,
                val imageFilePath: String,
                val width: Int,
                val height: Int,
                val dateTaken: Date,
                var isSelected: Boolean,
                var previouslySelected: Boolean,
                val displaySize: Int) : PhotoId(id, source), Serializable

data class PhotoPickerViewData(val prevSelectedPhotos: List<PhotoId>): Serializable {

    @Transient
    val photoItems = mutableListOf<PhotoItem>()

    var selectedPhotos =  mutableListOf<PhotoItem>()
    var photosPermissionRequested = false
}

class PhotoPickerConstants{
    companion object {
        val VIEW_DATA_KEY = "PhotoPickerConstants.AddProtocolViewData"

        val REQUEST_IMAGE_CAPTURE = 1001
    }
}

enum class ViewState{
    Loading, Loaded, Error, NoPermission
}

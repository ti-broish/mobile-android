package bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery

import bg.dabulgaria.tibroish.domain.image.PickedImage
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage
import javax.inject.Inject

interface IPhotoItemTypeAdapter{

    fun toPhotoItem(pickedImage: PickedImage, displaySize: Int) :PhotoItem

    fun toPhotoId(pickedImage: PickedImage, displaySize: Int) :PhotoId
}

class PhotoItemTypeAdapter @Inject constructor(): IPhotoItemTypeAdapter{

    override fun toPhotoItem(pickedImage: PickedImage, displaySize:Int): PhotoItem
            = PhotoItem( pickedImage.id,
            pickedImage.source,
            pickedImage.imageFilePath,
            pickedImage.width,
            pickedImage.height,
            pickedImage.dateTaken,
            isSelected = false,
            previouslySelected = false,
            displaySize = displaySize)

    override fun toPhotoId(pickedImage: PickedImage, displaySize: Int): PhotoId
            = PhotoId( pickedImage.id,
            pickedImage.source)

}

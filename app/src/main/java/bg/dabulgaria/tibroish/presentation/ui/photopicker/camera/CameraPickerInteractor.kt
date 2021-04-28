package bg.dabulgaria.tibroish.presentation.ui.photopicker.camera

import bg.dabulgaria.tibroish.domain.image.IGalleryImagesRepository
import bg.dabulgaria.tibroish.domain.image.PickedImage
import javax.inject.Inject


interface ICameraPickerInteractor{

    fun loadImages(protocolId:Long):List<PickedImage>
}

class CameraPickerInteractor @Inject constructor(private val galleryImagesRepository: IGalleryImagesRepository)
    : ICameraPickerInteractor {

    override fun loadImages(protocolId:Long): List<PickedImage> {

        return galleryImagesRepository.getImages()
    }
}
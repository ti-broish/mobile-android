package bg.dabulgaria.tibroish.presentation.providers

import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.PhotoItem
import javax.inject.Inject

interface IGallerySelectedImagesProvider{

    val selectedImages:MutableList<PhotoItem>
}

class GallerySelectedImagesProvider @Inject constructor() :IGallerySelectedImagesProvider {

    private val _selectedImages:MutableList<PhotoItem> = mutableListOf()

    override val selectedImages: MutableList<PhotoItem>
        get() = _selectedImages

}
package bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery

import android.content.Context
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.image.IGalleryImagesRepository
import bg.dabulgaria.tibroish.domain.image.PickedImage
import bg.dabulgaria.tibroish.domain.image.PickedImageSource
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import java.util.*
import javax.inject.Inject


interface IPhotoPickerInteractor{

    fun loadImages(data:PhotoPickerViewData, columnsCount:Int):List<PhotoItem>
}

class PhotoPickerInteractor @Inject constructor(private val galleryImagesRepository: IGalleryImagesRepository,
                                                private val photoItemTypeAdapter:IPhotoItemTypeAdapter,
                                                @AppContext
                                                private val context: Context) : IPhotoPickerInteractor {

    override fun loadImages(data:PhotoPickerViewData, columnsCount:Int): List<PhotoItem> {

        val width = context.resources.displayMetrics.widthPixels
        val margin =  context.resources.getDimensionPixelSize(R.dimen.picker_images_margin)
        val displaySize =  ((width - ( margin * columnsCount+1 )).toFloat() / columnsCount.toFloat()).toInt()
        val images = galleryImagesRepository.getImages().map {
            photoItemTypeAdapter.toPhotoItem( it, displaySize ) }

        for(image in images){

            val selected = data.selectedPhotos.find { it.id == image.id && it.source == image.source } != null
            image.isSelected = selected
        }
        return images
    }
}

interface IPhotoItemTypeAdapter{

    fun toPhotoItem(pickedImage: PickedImage, displaySize: Int) :PhotoItem
}

class PhotoItemTypeAdapter @Inject constructor(): IPhotoItemTypeAdapter{

    override fun toPhotoItem(pickedImage: PickedImage, displaySize:Int): PhotoItem
        = PhotoItem( pickedImage.id,
            pickedImage.source,
            pickedImage.imageFilePath,
            pickedImage.width,
            pickedImage.height,
            pickedImage.dateTaken,
            false,
            displaySize)

}
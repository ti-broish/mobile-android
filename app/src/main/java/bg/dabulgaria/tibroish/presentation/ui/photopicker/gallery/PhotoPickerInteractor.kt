package bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery

import android.content.Context
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.image.IGalleryImagesRepository
import bg.dabulgaria.tibroish.domain.image.PickedImage
import bg.dabulgaria.tibroish.domain.image.PickedImageSource
import bg.dabulgaria.tibroish.domain.protocol.image.IImageCopier
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImagesRepository
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage
import bg.dabulgaria.tibroish.domain.protocol.image.UploadStatus
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import java.util.*
import javax.inject.Inject


interface IPhotoPickerInteractor{

    fun loadImages(data:PhotoPickerViewData, columnsCount:Int):List<PhotoItem>

    fun addImagesToProtocol(data:PhotoPickerViewData)
}

class PhotoPickerInteractor @Inject constructor(private val galleryImagesRepo: IGalleryImagesRepository,
                                                private val protocolImagesRepo: IProtocolImagesRepository,
                                                private val photoItemTypeAdapter:IPhotoItemTypeAdapter,
                                                private val imageCopier: IImageCopier,
                                                @AppContext private val context: Context) : IPhotoPickerInteractor {

    override fun loadImages(data:PhotoPickerViewData, columnsCount:Int): List<PhotoItem> {

        val width = context.resources.displayMetrics.widthPixels
        val margin =  context.resources.getDimensionPixelSize(R.dimen.picker_images_margin)
        val displaySize =  ((width - ( margin * columnsCount+1 )).toFloat() / columnsCount.toFloat()).toInt()

        val protocolImages = protocolImagesRepo.getByProtocolId(data.protocolId)

        val images = galleryImagesRepo.getImages().map {
            photoItemTypeAdapter.toPhotoItem( it, displaySize ) }

        val selectedMap = data.selectedPhotos.map { getMapKey(it.id, it.source) to it }.toMap()

        val protocolImagesMap = protocolImages.map { getMapKey(it.providerId, it.source) to it }.toMap()

        for(image in images){

            val selected = selectedMap[ getMapKey(image.id, image.source) ] != null

            image.previouslySelected = protocolImagesMap[ getMapKey(image.id, image.source) ] != null
            image.isSelected = selected || image.previouslySelected
        }

        return images
    }

    override fun addImagesToProtocol(data: PhotoPickerViewData) {

        val protocolImages = mutableListOf<ProtocolImage>()
        protocolImagesRepo.runInTransaction(Runnable {

            for (image in data.selectedPhotos) {

                val protocolImage = ProtocolImage(id = 0,
                        protocolId = data.protocolId,
                        uuid = UUID.randomUUID().toString(),
                        serverId = "",
                        originalFilePath = image.imageFilePath,
                        localFilePath = "",
                        localFileThumbPath = "",
                        uploadStatus = UploadStatus.NotProcessed,
                        providerId = image.id,
                        source = image.source,
                        width = image.width,
                        height = image.height,
                        dateTaken = image.dateTaken)

                protocolImagesRepo.insert(protocolImage)
                protocolImages.add(protocolImage)
            }})

        for (protocolImage in protocolImages) {

            val copiedPath = imageCopier.copyToUploadsFolder(protocolImage.originalFilePath)
            protocolImage.localFilePath = copiedPath?:""
            if(protocolImage.localFilePath.isNotEmpty() )
                protocolImage.uploadStatus = UploadStatus.Copied
            protocolImagesRepo.update(protocolImage)
        }
    }

    private fun getMapKey( id:String, pickedImageSource: PickedImageSource ) :String{

        return "${id}_${pickedImageSource.code}"
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
            isSelected = false,
            previouslySelected = false,
            displaySize = displaySize)

}
package bg.dabulgaria.tibroish.presentation.ui.protocol.add


import android.graphics.BitmapFactory
import bg.dabulgaria.tibroish.domain.image.PickedImageSource
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.protocol.IProtocolsRepository
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.protocol.ProtocolExt
import bg.dabulgaria.tibroish.domain.protocol.ProtocolStatus
import bg.dabulgaria.tibroish.domain.protocol.image.IImageCopier
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImagesRepository
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage
import bg.dabulgaria.tibroish.domain.protocol.image.UploadStatus
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.ISectionPickerInteractor
import java.util.*
import javax.inject.Inject


interface IAddProtocolInteractor :ISectionPickerInteractor {

    fun addNew(protocol: Protocol): ProtocolExt

    fun loadData(viewData: AddProtocolViewData) :AddProtocolViewData

    fun deleteImage(image: ProtocolImage)

    fun addCameraImage(viewData: AddProtocolViewData)
}

class AddProtocolInteractor @Inject constructor(private val protocolsRepo: IProtocolsRepository,
                                                private val protocolsImagesRepo: IProtocolImagesRepository,
                                                private val fileRepo: IFileRepository,
                                                private val sectionPickerInteractor: ISectionPickerInteractor,
                                                private val imageCopier: IImageCopier)
    : IAddProtocolInteractor, ISectionPickerInteractor by sectionPickerInteractor{

    override fun addNew(protocol: Protocol): ProtocolExt {

        protocol.uuid = UUID.randomUUID().toString()
        protocol.status = ProtocolStatus.New
        protocolsRepo.insert(protocol)
        return ProtocolExt(protocol)
    }

    override fun loadData(viewData: AddProtocolViewData) :AddProtocolViewData{

        val newViewData = AddProtocolViewData()
        newViewData.protocolId = viewData.protocolId
        newViewData.sectionsData = loadSectionsData(viewData.sectionsData)

        if(newViewData.protocolId ?:0 > 0){

            val protocol = protocolsRepo.get( viewData.protocolId?:0 )
            val images = protocolsImagesRepo.getByProtocolId(protocol.id)
            newViewData.protocol = ProtocolExt(protocol).apply { this.images.addAll(images) }
        }

        newViewData.items.add(AddProtocolListItemHeader())
        newViewData.items.add(AddProtocolListItemSection(newViewData.sectionsData))

        for( photo in newViewData.protocol?.images.orEmpty())
            newViewData.items.add(AddProtocolListItemImage(photo))

        newViewData.items.add(AddProtocolListItemButtons())

        return newViewData
    }

    override fun deleteImage(image: ProtocolImage) {

        fileRepo.deleteFile(image.localFilePath)
        protocolsImagesRepo.delete(image)
    }

    override fun addCameraImage(data: AddProtocolViewData) {

        val protocolId = data.protocolId
                ?:return

        val imageFilePath = imageCopier.copyToLocalUploadsFolder(data.imageForCameraPath)
                ?: return

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, options)

        val protocolImage = ProtocolImage(id = 0,
                protocolId = protocolId,
                uuid = UUID.randomUUID().toString(),
                serverId = "",
                originalFilePath = imageFilePath,
                localFilePath = imageFilePath,
                localFileThumbPath = "",
                uploadStatus = UploadStatus.NotProcessed,
                providerId = "Camera_${UUID.randomUUID().toString()}",
                source = PickedImageSource.Camera,
                width = options.outWidth,
                height = options.outHeight,
                dateTaken = Date())

        protocolsImagesRepo.insert(protocolImage)
    }

    companion object {

        private val TAG = AddProtocolInteractor::class.simpleName
    }
}

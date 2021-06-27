package bg.dabulgaria.tibroish.presentation.ui.protocol.add


import android.content.Context
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.image.UploaderService
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImageUploader
import bg.dabulgaria.tibroish.domain.image.PickedImageSource
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.locations.ISelectedSectionLocalRepository
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.protocol.*
import bg.dabulgaria.tibroish.domain.protocol.image.IImageCopier
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImagesRepository
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.send.ImageSendStatus
import bg.dabulgaria.tibroish.domain.send.SendStatus
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.providers.ICameraTakenImageProvider
import bg.dabulgaria.tibroish.presentation.providers.IGallerySelectedImagesProvider
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.*
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.ISectionPickerInteractor
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.PhotoId
import java.util.*
import javax.inject.Inject


interface IAddProtocolInteractor : ISendItemInteractor

class AddProtocolInteractor @Inject constructor(sectionPickerInteractor: ISectionPickerInteractor,
                                                disposableHandler: IDisposableHandler,
                                                schedulersProvider: ISchedulersProvider,
                                                logger: ILogger,
                                                private val protocolsRepo: IProtocolsRepository,
                                                fileRepo: IFileRepository,
                                                imageCopier: IImageCopier,
                                                cameraTakenImageProvider: ICameraTakenImageProvider,
                                                private val protocolImageUploader: IProtocolImageUploader,
                                                private val selectedImagesProvider: IGallerySelectedImagesProvider,
                                                private val protocolImagesRepo: IProtocolImagesRepository,
                                                private val tiBroishRemoteRepository: ITiBroishRemoteRepository,
                                                private val resourceProvider: IResourceProvider,
                                                private val selectedSectionLocalRepo:
                                                ISelectedSectionLocalRepository,
                                                @AppContext val context: Context)
    : SendItemInteractor(sectionPickerInteractor,
        disposableHandler,
        schedulersProvider,
        logger,
        protocolImageUploader,
        imageCopier,
        fileRepo,
        cameraTakenImageProvider),
        IAddProtocolInteractor {

    override val titleString: String
        get() = resourceProvider.getString(R.string.send_protocol)

    override val successMessageString: String
        get() = resourceProvider.getString(R.string.protocol_send_successfully)

    override fun getItemImages(viewData: SendItemViewData): List<PhotoId> {

        val itemId = viewData.entityItem?.id ?: return emptyList()

        val images = protocolImagesRepo.getByProtocolId(itemId)

        return images.map { PhotoId(it.providerId, it.source) }
    }

    override fun addNew(): EntityItem {

        val protocol = Protocol()
        protocol.uuid = UUID.randomUUID().toString()
        protocol.status = SendStatus.New
        protocolsRepo.insert(protocol)
        return EntityItem(protocol.id)
    }

    override fun loadEntityItem(id: Long): EntityItem {

        val protocol = protocolsRepo.get(id)!!
        val images = protocolImagesRepo.getByProtocolId(protocol.id)

        return EntityItem(protocol.id, protocol.status).apply {

            this.images.addAll(images.map {

                EntityItemImage(it.id, protocol.id, it.localFilePath, it.imageSendStatus)
            })
        }
    }

    override fun updateEntityItemStatus(id: Long, status: SendStatus): EntityItem {

        val protocol = protocolsRepo.get(id)!!
        protocol.status = status
        protocolsRepo.update(protocol)
        return EntityItem(protocol.id, protocol.status)
    }

    override fun deleteImageConcrete(entityItemImage: EntityItemImage) {

        entityItemImage.localFilePath?.let { fileRepo.deleteFile(it) }
        protocolImagesRepo.delete(entityItemImage.id)
    }

    override fun addImageToRepo(itemId: Long, imageFilePath: String, width: Int, height: Int) {

        val protocolImage = ProtocolImage(id = 0,
                protocolId = itemId,
                uuid = UUID.randomUUID().toString(),
                serverId = "",
                originalFilePath = imageFilePath,
                localFilePath = imageFilePath,
                localFileThumbPath = "",
                imageSendStatus = ImageSendStatus.Copied,
                providerId = "Camera_${UUID.randomUUID().toString()}",
                source = PickedImageSource.Camera,
                width = width,
                height = height,
                dateTaken = Date())

        protocolImagesRepo.insert(protocolImage)
    }

    override fun sendItemConcrete(viewData: SendItemViewData): EntityItem {

        selectedSectionLocalRepo.selectedSectionData = viewData.sectionsData

        val metadata = ProtocolMetadata(
            protocolId = viewData.entityItem!!.id,
            sectionId = viewData.sectionsData!!.selectedSection!!.id)

        val entityItem = updateEntityItemStatus(metadata.protocolId, SendStatus.Sending)
        UploaderService.uploadProtocol(context, metadata)

        return entityItem
    }

    override fun addSelectedGalleryImages(currentData: SendItemViewData) {

        if (selectedImagesProvider.selectedImages.isEmpty())
            return

        val protocolImages = mutableListOf<ProtocolImage>()
        protocolImagesRepo.runInTransaction(Runnable {

            if (currentData.entityItem == null) {

                currentData.entityItem = addNew()
            }

            for (image in selectedImagesProvider.selectedImages) {

                val protocolImage = ProtocolImage(id = 0,
                        protocolId = currentData.entityItem!!.id,
                        uuid = UUID.randomUUID().toString(),
                        serverId = "",
                        originalFilePath = image.imageFilePath,
                        localFilePath = "",
                        localFileThumbPath = "",
                        imageSendStatus = ImageSendStatus.NotProcessed,
                        providerId = image.id,
                        source = image.source,
                        width = image.width,
                        height = image.height,
                        dateTaken = image.dateTaken)

                protocolImagesRepo.insert(protocolImage)
                protocolImages.add(protocolImage)
            }
            selectedImagesProvider.selectedImages.clear()
        })

        for (protocolImage in protocolImages) {

            val copiedPath = imageCopier.copyToLocalUploadsFolder(protocolImage.originalFilePath)
            protocolImage.localFilePath = copiedPath ?: ""

            if (protocolImage.localFilePath.isNotEmpty())
                protocolImage.imageSendStatus = ImageSendStatus.Copied

            protocolImagesRepo.update(protocolImage)
        }
    }
    //endregion private methods

    companion object {

        private val TAG = AddProtocolInteractor::class.simpleName
    }
}

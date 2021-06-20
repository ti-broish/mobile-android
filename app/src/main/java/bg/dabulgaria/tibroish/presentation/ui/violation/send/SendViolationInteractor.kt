package bg.dabulgaria.tibroish.presentation.ui.violation.send


import android.content.Context
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.image.UploaderService
import bg.dabulgaria.tibroish.domain.image.PickedImageSource
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.locations.ISelectedSectionLocalRepository
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.protocol.image.IImageCopier
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.send.ImageSendStatus
import bg.dabulgaria.tibroish.domain.send.SendStatus
import bg.dabulgaria.tibroish.domain.violation.IViolationRepository
import bg.dabulgaria.tibroish.domain.violation.ViolationMetadata
import bg.dabulgaria.tibroish.domain.violation.VoteViolation
import bg.dabulgaria.tibroish.domain.violation.image.IViolationImageUploader
import bg.dabulgaria.tibroish.domain.violation.image.IViolationImagesRepository
import bg.dabulgaria.tibroish.domain.violation.image.ViolationImage
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


interface ISendViolationInteractor : ISendItemInteractor

class SendViolationInteractor @Inject constructor(sectionPickerInteractor: ISectionPickerInteractor,
                                                  disposableHandler: IDisposableHandler,
                                                  schedulersProvider : ISchedulersProvider,
                                                  logger: ILogger,
                                                  private val violationsRepo: IViolationRepository,
                                                  private val violationImagesRepo: IViolationImagesRepository,
                                                  fileRepo: IFileRepository,
                                                  imageCopier: IImageCopier,
                                                  cameraTakenImageProvider: ICameraTakenImageProvider,
                                                  private val violationImageUploader: IViolationImageUploader,
                                                  private val selectedImagesProvider: IGallerySelectedImagesProvider,
                                                  private val tiBroishRemoteRepository: ITiBroishRemoteRepository,
                                                  private val resourceProvider: IResourceProvider,
                                                  private val selectedSectionLocalRepo:
                                                  ISelectedSectionLocalRepository,
                                                  @AppContext val context: Context)
    : SendItemInteractor(sectionPickerInteractor,
        disposableHandler,
        schedulersProvider,
        logger,
        violationImageUploader,
        imageCopier,
        fileRepo,
        cameraTakenImageProvider),
        ISendViolationInteractor {

    init {
        this.autoFillSection = false
    }

    override val supportsMessage:Boolean = true

    override val messageLabel:String = resourceProvider.getString(R.string.violation_description)

    override val titleString: String
        get() = resourceProvider.getString(R.string.send_signal)

    override val successMessageString: String
        get() = resourceProvider.getString(R.string.violation_send_successfully)

    override val hideUniqueUntilSectionGetsSelected = true

    override val sectionIsRequired = false

    override fun getItemImages(viewData: SendItemViewData): List<PhotoId> {

        val itemId = viewData.entityItem?.id ?: return emptyList()

        val images = violationImagesRepo.getByViolationId(itemId)

        return images.map { PhotoId(it.providerId, it.source) }
    }

    override fun addNew(): EntityItem {

        val violation = VoteViolation()
        violation.uuid = UUID.randomUUID().toString()
        violation.status = SendStatus.New
        violationsRepo.insert(violation)
        return EntityItem(violation.id)
    }

    override fun loadEntityItemExt(id: Long): EntityItem {

        val violation = violationsRepo.get(id)!!
        val images = violationImagesRepo.getByViolationId(violation.id)

        return EntityItem(violation.id).apply {

            this.images.addAll(images.map {

                EntityItemImage(it.id, violation.id, it.localFilePath, it.imageSendStatus)
            })
        }
    }

    override fun deleteImageConcrete(entityItemImage: EntityItemImage) {

        entityItemImage.localFilePath?.let{ fileRepo.deleteFile(it) }
        violationImagesRepo.delete(entityItemImage.id)
    }

    override fun addImageToRepo(itemId: Long, imageFilePath: String, width: Int, height: Int) {

        val violationImage = ViolationImage(id = 0,
                violationId = itemId,
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

        violationImagesRepo.insert(violationImage)
    }

    override fun sendItemConcrete(viewData: SendItemViewData) {

        if(viewData.entityItem == null)
            viewData.entityItem = addNew()

        selectedSectionLocalRepo.selectedSectionData = viewData.sectionsData

        val entity = viewData.entityItem?: return
        val metadata = ViolationMetadata(
            violationId = entity.id,
            sectionId = viewData.sectionsData?.selectedSection?.id ?:"",
            townId = viewData.sectionsData?.selectedTown?.id,
            description = viewData.message)
        UploaderService.uploadViolation(context, metadata)
    }

    override fun addSelectedGalleryImages(currentData: SendItemViewData){

        if(selectedImagesProvider.selectedImages.isEmpty())
            return

        val violationImages = mutableListOf<ViolationImage>()
        violationImagesRepo.runInTransaction(Runnable {

            if( currentData.entityItem == null ) {

                currentData.entityItem = addNew()
            }

            for (image in selectedImagesProvider.selectedImages) {

                val violationImage = ViolationImage(id = 0,
                        violationId = currentData.entityItem!!.id,
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

                violationImagesRepo.insert(violationImage)
                violationImages.add(violationImage)
            }
            selectedImagesProvider.selectedImages.clear()
        })

        for (violationImage in violationImages) {

            val copiedPath = imageCopier.copyToLocalUploadsFolder(violationImage.originalFilePath)
            violationImage.localFilePath = copiedPath?:""
            if(violationImage.localFilePath.isNotEmpty() )
                violationImage.imageSendStatus = ImageSendStatus.Copied
            violationImagesRepo.update(violationImage)
        }

    }
    //endregion private methods

    companion object {

        private val TAG = SendViolationInteractor::class.simpleName
    }
}

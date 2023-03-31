package bg.dabulgaria.tibroish.presentation.ui.common.item.send

import android.graphics.BitmapFactory
import bg.dabulgaria.tibroish.domain.ICountryCodesRepo
import bg.dabulgaria.tibroish.domain.image.IEntityImageUploader
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.protocol.image.IImageCopier
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.ISectionPickerInteractor
import bg.dabulgaria.tibroish.domain.send.SendStatus
import bg.dabulgaria.tibroish.presentation.providers.ICameraTakenImageProvider
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.PhotoId
import io.reactivex.rxjava3.disposables.Disposable


interface ISendItemInteractor :ISectionPickerInteractor, IDisposableHandler {

    fun addNew(): EntityItem

    fun loadData(viewData: SendItemViewData) : SendItemViewData

    fun deleteImage(image: EntityItemImage)

    fun addCameraImage(viewData: SendItemViewData)

    fun sendItem(viewData: SendItemViewData): EntityItem

    fun getItemImages(viewData: SendItemViewData):List<PhotoId>

    fun isSectionManual(): Boolean

    val successMessageString: String
}

abstract class SendItemInteractor(
    protected val sectionPickerInteractor: ISectionPickerInteractor,
    disposableHandler: IDisposableHandler,
    protected val schedulersProvider: ISchedulersProvider,
    protected val logger: ILogger,
    protected val entityImageUploader: IEntityImageUploader,
    protected val imageCopier: IImageCopier,
    protected val fileRepo: IFileRepository,
    protected val cameraTakenImageProvider: ICameraTakenImageProvider,
    protected val countryCodesRepo: ICountryCodesRepo,
    )
    : ISendItemInteractor,
        ISectionPickerInteractor by sectionPickerInteractor,
        IDisposableHandler by disposableHandler{

    abstract val titleString: String

    abstract fun loadEntityItem(id: Long) : EntityItem?

    abstract fun updateEntityItemStatus(id: Long, status: SendStatus): EntityItem

    abstract fun deleteImageConcrete(entityItemImage: EntityItemImage)

    abstract fun sendItemConcrete(viewData: SendItemViewData): EntityItem

    abstract fun addSelectedGalleryImages(viewData: SendItemViewData)

    abstract fun addImageToRepo(itemId: Long, imageFilePath: String, width: Int, height: Int)

    open val supportsMessage: Boolean = false
    open val messageLabel:String = ""
    open val hideUniqueUntilSectionGetsSelected = false
    open val sectionIsRequired = true
    open val supportsImages: Boolean = true
    open val hasImagesInfo: Boolean = false
    
    var imageUploadDisposable :Disposable?=null

    //region ISendItemInteractor implementation

    override fun loadData(viewData: SendItemViewData) : SendItemViewData {

        logger.i(TAG, "Start load")
        val newViewData = SendItemViewData()
        newViewData.entityItem = viewData.entityItem
        newViewData.message = viewData.message
        newViewData.phone = viewData.phone
        newViewData.email = viewData.email
        newViewData.names = viewData.names
        newViewData.imagesIndexesOffset = 0
        newViewData.imagePreviewOpen = viewData.imagePreviewOpen
        newViewData.manualSectionId = viewData.manualSectionId

        newViewData.sectionsData = loadSectionsData(viewData.sectionsData)
        newViewData.sectionsData?.hideUniqueUntilSectionIsSelected = hideUniqueUntilSectionGetsSelected
        newViewData.sectionsData?.isSectionRequired = sectionIsRequired

        newViewData.countryCodes = countryCodesRepo.getCountryCodes()

        logger.i(TAG, "section data loaded")

        addSelectedGalleryImages(newViewData)

        addCameraImage(newViewData)

        logger.i(TAG, "selected images loaded")

        val entityItemId: Long = when{
            newViewData.entityItem?.id != null ->  newViewData.entityItem!!.id
            viewData.entityDbId != null -> viewData.entityDbId
            else -> -1
        }

        if(entityItemId  > -1)
            newViewData.entityItem = loadEntityItem(entityItemId)

        logger.i(TAG, "entity item loaded")

        if(newViewData.entityItem?.sendStatus == SendStatus.Send){

            newViewData.items.clear()
            newViewData.items.add(SendItemListItemSendSuccess(successMessageString))
            return newViewData
        }

        if(entityItemId > 0 && (newViewData.entityItem?.sendStatus == SendStatus.SendError
                        ||newViewData.entityItem?.sendStatus == SendStatus.SendErrorInvalidSection))
            updateEntityItemStatus(entityItemId, SendStatus.New)

        newViewData.items.add(SendItemListItemHeader(titleString))
        newViewData.imagesIndexesOffset++
        if (isSectionManual()) {

            if(newViewData.manualSectionId == null)
                newViewData.manualSectionId = getManualDefaultSectionPrefill()

            newViewData.items.add(SendItemListItemSectionManual(newViewData.manualSectionId))
        } else {
            newViewData.items.add(SendItemListItemSection(newViewData.sectionsData))
        }
        newViewData.imagesIndexesOffset++

        if(hasImagesInfo){
            newViewData.items.add(SendItemListItemInfoText())
            newViewData.imagesIndexesOffset++
        }

        if(supportsMessage) {
            newViewData.items.add(
                SendItemListItemMessage(messageLabel,
                    newViewData.message ?: "",
                    newViewData.countryCodes
                )
            )
            newViewData.imagesIndexesOffset++
        }

        for( photo in newViewData.entityItem?.images.orEmpty())
            newViewData.items.add(SendItemListItemImage(photo))

        if(newViewData.entityItem?.images.isNullOrEmpty())
            newViewData.imagePreviewOpen = false

        newViewData.items.add(SendItemListItemButtons(supportsImages))

        logger.i(TAG, "End load")

        return newViewData
    }

    open fun getManualDefaultSectionPrefill(): String? = null

    abstract override fun isSectionManual(): Boolean

    override fun deleteImage(image: EntityItemImage) {

        deleteImageConcrete(image)
    }

    override fun addCameraImage(viewData: SendItemViewData) {

        val path = cameraTakenImageProvider.cameraImagePath
        logger.i(TAG, " addCameraImage cameraImagePath: ${path}" )
        if(path.isEmpty())
            return

        val size = fileRepo.getFileSizeKb(path)
        logger.i(TAG, "addCameraImage file size KB: ${size}" )

        if(fileRepo.getFileSizeKb(path) < MIN_FILE_SIZE_KB)
            return

        logger.i(TAG, "addCameraImage  viewData.entityItem?.id: ${viewData.entityItem?.id}" )

        val itemId = viewData.entityItem?.id
                ?:return

        val imageFilePath = imageCopier.copyToLocalUploadsFolder(path)
                ?: return

        logger.i(TAG, "addCameraImage  imageFilePath ${imageFilePath}" )

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, options)
        val width = options.outWidth
        val height = options.outHeight

        logger.i(TAG, "addCameraImage  options " )

        addImageToRepo(itemId, imageFilePath, width, height)

        logger.i(TAG, "addCameraImage  addImageToRepo OK" )

        cameraTakenImageProvider.cameraImagePath = ""
    }

    override fun sendItem(viewData: SendItemViewData): EntityItem {

        return sendItemConcrete(viewData)
    }
    //endregion ISendItemInteractor implementation

    companion object {

        private val TAG = SendItemInteractor::class.simpleName
        private val MIN_FILE_SIZE_KB = 512
    }
}
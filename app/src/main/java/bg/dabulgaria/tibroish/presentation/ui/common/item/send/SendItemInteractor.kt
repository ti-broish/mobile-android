package bg.dabulgaria.tibroish.presentation.ui.common.item.send

import android.graphics.BitmapFactory
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

    val successMessageString: String
}

abstract class SendItemInteractor constructor(protected val sectionPickerInteractor: ISectionPickerInteractor,
                                              disposableHandler: IDisposableHandler,
                                              protected val schedulersProvider: ISchedulersProvider,
                                              protected val logger: ILogger,
                                              protected val entityImageUploader: IEntityImageUploader,
                                              protected val imageCopier:IImageCopier,
                                              protected val fileRepo: IFileRepository,
                                              protected  val cameraTakenImageProvider: ICameraTakenImageProvider)
    : ISendItemInteractor,
        ISectionPickerInteractor by sectionPickerInteractor,
        IDisposableHandler by disposableHandler{

    abstract val titleString: String

    abstract fun loadEntityItem(id: Long) : EntityItem?

    abstract fun updateEntityItemStatus(id: Long, status: SendStatus)

    abstract fun deleteImageConcrete(entityItemImage: EntityItemImage)

    abstract fun sendItemConcrete(viewData: SendItemViewData): EntityItem

    abstract fun addSelectedGalleryImages(viewData: SendItemViewData)

    abstract fun addImageToRepo(itemId: Long, imageFilePath: String, width: Int, height: Int)

    open val supportsMessage: Boolean = false
    open val messageLabel:String = ""
    open val hideUniqueUntilSectionGetsSelected = false
    open val sectionIsRequired = true
    open val supportsImages: Boolean = true
    
    var imageUploadDisposable :Disposable?=null

    //region ISendItemInteractor implementation

    override fun loadData(viewData: SendItemViewData) : SendItemViewData {

        val newViewData = SendItemViewData()
        newViewData.entityItem = viewData.entityItem
        newViewData.message = viewData.message

        newViewData.sectionsData = loadSectionsData(viewData.sectionsData)
        newViewData.sectionsData?.hideUniqueUntilSectionIsSelected = hideUniqueUntilSectionGetsSelected
        newViewData.sectionsData?.isSectionRequired = sectionIsRequired

        addSelectedGalleryImages(newViewData)

        addCameraImage(newViewData)

        val entityItemId: Long = when{
            newViewData.entityItem?.id != null ->  newViewData.entityItem!!.id
            viewData.entityDbId != null -> viewData.entityDbId
            else -> 0
        }

        if(entityItemId  > 0)
            newViewData.entityItem = loadEntityItem(entityItemId)

        if(newViewData.entityItem?.sendStatus == SendStatus.Send){

            newViewData.items.clear()
            newViewData.items.add(SendItemListItemSendSuccess(successMessageString))
            return newViewData
        }

        if(entityItemId > 0 && newViewData.entityItem?.sendStatus == SendStatus.SendError)
            updateEntityItemStatus(entityItemId, SendStatus.New)

        newViewData.items.add(SendItemListItemHeader(titleString))
        newViewData.items.add(SendItemListItemSection(newViewData.sectionsData))

        if(supportsMessage)
            newViewData.items.add(SendItemListItemMessage(messageLabel,
                    newViewData.message?:"" ))

        for( photo in newViewData.entityItem?.images.orEmpty())
            newViewData.items.add(SendItemListItemImage(photo))

        newViewData.items.add(SendItemListItemButtons(supportsImages))

        return newViewData
    }

    override fun deleteImage(image: EntityItemImage) {

        deleteImageConcrete(image)
    }

    override fun addCameraImage(viewData: SendItemViewData) {

        val path = cameraTakenImageProvider.cameraImagePath
        if(path.isEmpty())
            return

        if(fileRepo.getFileSizeKb(path) < MIN_FILE_SIZE_KB)
            return

        val itemId = viewData.entityItem?.id
                ?:return

        val imageFilePath = imageCopier.copyToLocalUploadsFolder(path)
                ?: return

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, options)
        val width = options.outWidth
        val height = options.outHeight

        addImageToRepo(itemId, imageFilePath, width, height)

        cameraTakenImageProvider.cameraImagePath = ""
    }

    override fun sendItem(viewData: SendItemViewData): EntityItem {

        return sendItemConcrete(viewData)
    }
    //endregion ISendItemInteractor implementation

    //region private methods

    private fun retryUploadImages(itemId: Long){

        try {

            entityImageUploader.uploadImages(itemId)
        }
        catch (th:Throwable){

            logger.e(TAG, th)
            retryUploadImages(itemId)
        }
    }

    private fun stopUpload(){

        if( (imageUploadDisposable != null) && imageUploadDisposable?.isDisposed == false )
            imageUploadDisposable?.dispose()
    }
    //endregion private methods

    companion object {

        private val TAG = SendItemInteractor::class.simpleName
        private val MIN_FILE_SIZE_KB = 1024
    }
}
package bg.dabulgaria.tibroish.presentation.ui.protocol.add


import android.graphics.BitmapFactory
import bg.dabulgaria.tibroish.domain.image.IProtocolImageUploader
import bg.dabulgaria.tibroish.domain.image.PickedImageSource
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.protocol.*
import bg.dabulgaria.tibroish.domain.protocol.image.IImageCopier
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImagesRepository
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage
import bg.dabulgaria.tibroish.domain.protocol.image.UploadStatus
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.persistence.remote.repo.TiBroishRemoteRepository
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.providers.IGallerySelectedImagesProvider
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.ISectionPickerInteractor
import bg.dabulgaria.tibroish.presentation.ui.photopicker.camera.PhotoItem
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.PhotoId
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


interface IAddProtocolInteractor :ISectionPickerInteractor, IDisposableHandler {

    fun addNew(protocol: Protocol): ProtocolExt

    fun loadData(viewData: AddProtocolViewData) :AddProtocolViewData

    fun deleteImage(image: ProtocolImage)

    fun addCameraImage(viewData: AddProtocolViewData)

    fun sendProtocol(viewData: AddProtocolViewData)

    fun getProtocolImages(viewData: AddProtocolViewData):List<PhotoId>
}

class AddProtocolInteractor @Inject constructor(private val protocolsRepo: IProtocolsRepository,
                                                private val protocolsImagesRepo: IProtocolImagesRepository,
                                                private val fileRepo: IFileRepository,
                                                private val sectionPickerInteractor: ISectionPickerInteractor,
                                                private val imageCopier: IImageCopier,
                                                dispHandler: IDisposableHandler,
                                                private val schedulersProvider : ISchedulersProvider,
                                                private val protocolImageUploader:IProtocolImageUploader,
                                                private val logger: ILogger,
                                                private val selectedImagesProvider: IGallerySelectedImagesProvider,
                                                private val protocolImagesRepo:IProtocolImagesRepository,
                                                private val tiBroishRemoteRepository: ITiBroishRemoteRepository)
    : IAddProtocolInteractor, ISectionPickerInteractor by sectionPickerInteractor,
        IDisposableHandler by dispHandler{

    var imageUploadDisposable :Disposable?=null

    //region IAddProtocolInteractor implementation
    override fun addNew(protocol: Protocol): ProtocolExt {

        protocol.uuid = UUID.randomUUID().toString()
        protocol.status = ProtocolStatus.New
        protocolsRepo.insert(protocol)
        return ProtocolExt(protocol)
    }

    override fun loadData(viewData: AddProtocolViewData) :AddProtocolViewData{

        val newViewData = AddProtocolViewData()
        newViewData.protocolId = viewData.protocolId
        newViewData.protocol = viewData.protocol
        newViewData.sectionsData = loadSectionsData(viewData.sectionsData)

        addSelectedGalleryImages(newViewData)

        if(newViewData.protocolId ?:0 > 0){

            val protocol = protocolsRepo.get( newViewData.protocolId?:0 )
            val images = protocolsImagesRepo.getByProtocolId(protocol.id)
            newViewData.protocol = ProtocolExt(protocol).apply { this.images.addAll(images) }
        }

        newViewData.items.add(AddProtocolListItemHeader())
        newViewData.items.add(AddProtocolListItemSection(newViewData.sectionsData))

        for( photo in newViewData.protocol?.images.orEmpty())
            newViewData.items.add(AddProtocolListItemImage(photo))

        newViewData.items.add(AddProtocolListItemButtons())

        newViewData.protocolId?.let { startImageUpload(it) }

        return newViewData
    }

    override fun deleteImage(image: ProtocolImage) {

        stopUpload()

        fileRepo.deleteFile(image.localFilePath)
        protocolsImagesRepo.delete(image)

        startImageUpload(image.protocolId)
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
                uploadStatus = UploadStatus.Copied,
                providerId = "Camera_${UUID.randomUUID().toString()}",
                source = PickedImageSource.Camera,
                width = options.outWidth,
                height = options.outHeight,
                dateTaken = Date())

        protocolsImagesRepo.insert(protocolImage)

        startImageUpload(protocolId)
    }

    override fun sendProtocol(viewData: AddProtocolViewData) {

        stopUpload()
        val protocolId = viewData.protocol!!.id
        protocolImageUploader.uploadImages(protocolId)

        val images = protocolsImagesRepo.getByProtocolId(protocolId)

        val request = SendProtocolRequest( viewData.sectionsData!!.mSelectedSection!!.id,
                images.map { it.serverId })

        val response = tiBroishRemoteRepository.sendProtocol(request)

        val protocol = protocolsRepo.get(protocolId)
        protocol.remoteStatus = response.status
        protocol.serverId = response.id.toString()
        protocolsRepo.update(protocol)
    }

    override fun getProtocolImages(viewData: AddProtocolViewData): List<PhotoId> {

        val protocolId = viewData.protocol?.id
                ?: return emptyList()

        val protocolImages = protocolImagesRepo.getByProtocolId(protocolId)

        return protocolImages.map { PhotoId(it.providerId, it.source) }
    }
    //endregion IAddProtocolInteractor implementation

    //region private methods
    private fun startImageUpload(protocolId: Long){

        stopUpload()

        imageUploadDisposable = Observable.interval(2, TimeUnit.SECONDS)
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.ioScheduler())
                .doOnNext {

                    var success = false
                    try {

                        protocolImageUploader.uploadImages(protocolId)
                        success = true
                    }
                    catch (th:Throwable){}

                    if(success)
                        this.dispose()

                } //endregion
                .subscribe({  }) { e: Throwable? ->
                    logger.e(TAG, e) }

        imageUploadDisposable?.let { add(it) }
    }

    private fun stopUpload(){

        if( (imageUploadDisposable != null) && imageUploadDisposable?.isDisposed == false )
            imageUploadDisposable?.dispose()
    }

    private fun addSelectedGalleryImages(currentData:AddProtocolViewData){

        if(selectedImagesProvider.selectedImages.isEmpty())
            return

        val protocolImages = mutableListOf<ProtocolImage>()
        protocolImagesRepo.runInTransaction(Runnable {

            if( currentData.protocol == null ) {

                val protocol = Protocol()
                currentData.protocol = addNew(protocol)
                currentData.protocolId = currentData.protocol!!.id
            }

            for (image in selectedImagesProvider.selectedImages) {

                val protocolImage = ProtocolImage(id = 0,
                        protocolId = currentData.protocolId!!,
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
            }
            selectedImagesProvider.selectedImages.clear()
        })

        for (protocolImage in protocolImages) {

            val copiedPath = imageCopier.copyToLocalUploadsFolder(protocolImage.originalFilePath)
            protocolImage.localFilePath = copiedPath?:""
            if(protocolImage.localFilePath.isNotEmpty() )
                protocolImage.uploadStatus = UploadStatus.Copied
            protocolImagesRepo.update(protocolImage)
        }

    }
    //endregion private methods

    companion object {

        private val TAG = AddProtocolInteractor::class.simpleName
    }
}

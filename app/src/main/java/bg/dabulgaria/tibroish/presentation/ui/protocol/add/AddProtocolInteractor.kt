package bg.dabulgaria.tibroish.presentation.ui.protocol.add


import android.graphics.BitmapFactory
import bg.dabulgaria.tibroish.domain.image.IProtocolImageUploader
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
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.ISectionPickerInteractor
import io.reactivex.rxjava3.core.Observable
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


interface IAddProtocolInteractor :ISectionPickerInteractor, IDisposableHandler {

    fun addNew(protocol: Protocol): ProtocolExt

    fun loadData(viewData: AddProtocolViewData) :AddProtocolViewData

    fun deleteImage(image: ProtocolImage)

    fun addCameraImage(viewData: AddProtocolViewData)
}

class AddProtocolInteractor @Inject constructor(private val protocolsRepo: IProtocolsRepository,
                                                private val protocolsImagesRepo: IProtocolImagesRepository,
                                                private val fileRepo: IFileRepository,
                                                private val sectionPickerInteractor: ISectionPickerInteractor,
                                                private val imageCopier: IImageCopier,
                                                dispHandler: IDisposableHandler,
                                                private val schedulersProvider : ISchedulersProvider,
                                                private val protocolImageUploader:IProtocolImageUploader,
                                                private val logger: ILogger)
    : IAddProtocolInteractor, ISectionPickerInteractor by sectionPickerInteractor,
        IDisposableHandler by dispHandler{

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

        newViewData.protocolId?.let { startImageUpload(it) }

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
                uploadStatus = UploadStatus.Copied,
                providerId = "Camera_${UUID.randomUUID().toString()}",
                source = PickedImageSource.Camera,
                width = options.outWidth,
                height = options.outHeight,
                dateTaken = Date())

        protocolsImagesRepo.insert(protocolImage)

        startImageUpload(protocolId)
    }

    fun startImageUpload(protocolId: Long){

        add(Observable.interval(2, TimeUnit.SECONDS)
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
                    logger.e(TAG, e) } )
    }

    companion object {

        private val TAG = AddProtocolInteractor::class.simpleName
    }
}

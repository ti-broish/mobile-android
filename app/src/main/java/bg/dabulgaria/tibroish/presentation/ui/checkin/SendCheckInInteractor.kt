package bg.dabulgaria.tibroish.presentation.ui.checkin


import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.locations.ISelectedSectionLocalRepository
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.user.SendCheckInRequest
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.persistence.local.ImageCopier
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.*
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.ISectionPickerInteractor
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.PhotoId
import javax.inject.Inject


interface ISendCheckInInteractor : ISendItemInteractor

class SendCheckInInteractor @Inject constructor(sectionPickerInteractor: ISectionPickerInteractor,
                                                disposableHandler: IDisposableHandler,
                                                schedulersProvider: ISchedulersProvider,
                                                logger: ILogger,
                                                imageCopier: ImageCopier,
                                                private val tiBroishRemoteRepository: ITiBroishRemoteRepository,
                                                private val resourceProvider: IResourceProvider,
                                                private val selectedSectionLocalRepo: ISelectedSectionLocalRepository)
    : SendItemInteractor(sectionPickerInteractor,
        disposableHandler,
        schedulersProvider,
        logger,
        EmptyImageUploader(),
        imageCopier),
        ISendCheckInInteractor {

    init {
        this.autoFillSection = true
    }

    override val supportsMessage:Boolean = false

    override val messageLabel:String = ""

    override val titleString: String
        get() = resourceProvider.getString(R.string.check_in)

    override val successMessageString: String
        get() = resourceProvider.getString(R.string.check_in_send_successfully)

    override val hideUniqueUntilSectionGetsSelected = false

    override val sectionIsRequired = true

    override val supportsImages = false

    override fun getItemImages(viewData: SendItemViewData): List<PhotoId> = emptyList()

    override fun addNew() = EntityItem(0)

    override fun loadEntityItemExt(id: Long) = EntityItem(0)

    override fun deleteImageConcrete(entityItemImage: EntityItemImage) {}

    override fun addImageToRepo(itemId: Long, imageFilePath: String, width: Int, height: Int) { }

    override fun sendItemConcrete(viewData: SendItemViewData) {

        selectedSectionLocalRepo.selectedSectionData = viewData.sectionsData

        val request = SendCheckInRequest(viewData!!.sectionsData!!.selectedSection!!.id)
        val response = tiBroishRemoteRepository.sendCheckIn(request)
    }

    override fun addSelectedGalleryImages(currentData: SendItemViewData){}
    //endregion private methods

    companion object {

        private val TAG = SendCheckInInteractor::class.simpleName
    }
}

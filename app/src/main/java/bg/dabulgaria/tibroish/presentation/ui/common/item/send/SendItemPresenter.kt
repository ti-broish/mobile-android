package bg.dabulgaria.tibroish.presentation.ui.common.item.send

import android.os.Bundle
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.locations.*
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.persistence.local.Folders
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.ISectionPickerPresenter
import bg.dabulgaria.tibroish.domain.locations.SectionViewType
import bg.dabulgaria.tibroish.domain.locations.SectionsViewData
import bg.dabulgaria.tibroish.domain.send.ItemSendResultEvent
import bg.dabulgaria.tibroish.domain.send.SendStatus
import bg.dabulgaria.tibroish.infrastructure.permission.IPermissionRequester
import bg.dabulgaria.tibroish.infrastructure.permission.PermissionCodes
import bg.dabulgaria.tibroish.presentation.event.CameraPhotoTakenEvent
import bg.dabulgaria.tibroish.presentation.main.IPermissionResponseListener
import bg.dabulgaria.tibroish.presentation.providers.ICameraTakenImageProvider
import bg.dabulgaria.tibroish.presentation.ui.common.preview.images.PreviewImage
import io.reactivex.rxjava3.core.Single
import org.greenrobot.eventbus.Logger
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

interface ISendItemPresenter : IBasePresenter<ISendItemView>, ISectionPickerPresenter {

    fun onAddFromGalleryClick()

    fun onAddFromCameraClick()

    fun onSend()

    fun onImageDeleteClick(item: SendItemListItemImage, position:Int)

    fun onSuccessOkClick()

    fun onMessageChanged(message: String)

    fun onNamesChanged(names: String)
    fun onPhoneChanged(phone: String)
    fun onEmailChanged(email: String)

    fun onHandleBack(imagePosition: Int?): Boolean

    fun onImagePreviewClick(position:Int)

    fun onPreviewDelete(previewPosition: Int, image: PreviewImage)

    fun onPreviewCloseClick(previewLastPosition: Int)
}

abstract class SendItemPresenter
constructor(private val schedulersProvider: ISchedulersProvider,
            private val mainRouter: IMainRouter,
            private val interactor: ISendItemInteractor,
            private val fileRepository: IFileRepository,
            private val logger: ILogger,
            disposableHandler: IDisposableHandler,
            private val cameraTakenImageProvider: ICameraTakenImageProvider,
            private val permissionRequester : IPermissionRequester)
    : BasePresenter<ISendItemView>(disposableHandler), ISendItemPresenter, IPermissionResponseListener {

    init {
        logger.i(TAG, "SendItemPresenter constructor")
    }

    override val registerEventBus = true

    val permission = PermissionCodes.CAMERA

    var data: SendItemViewData? = null

    abstract val MinSelectedImages: Int

    abstract fun validateData(data: SendItemViewData):Boolean

    //region ISendItemPresenter implementation
    override fun onRestoreData(bundle: Bundle?) {

        bundle?.let {

            data = bundle.getSerializable(SendItemConstants.VIEW_DATA_KEY) as SendItemViewData?
        }
    }

    override fun onSaveData(outState: Bundle) {
        outState.putSerializable(SendItemConstants.VIEW_DATA_KEY, data)
    }

    override fun loadData() {

        logger.i(TAG, "loadData()" )
        val currentData = data?:return

        view?.onLoadingStateChange(true)

        add(Single.fromCallable{ interactor.loadData(currentData)}
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( { onDataLoaded( it ) },{ th -> onError(th) }) )
    }

    override fun onAddFromGalleryClick() {

        val currentData = data?: return

        view?.hideSoftKeyboard()
        view?.onLoadingStateChange(true)

        add( Single.fromCallable{

            interactor.getItemImages(currentData)
        }
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( { selectedImages ->

                    view?.onLoadingStateChange(false)
                    mainRouter.showPhotoPicker(selectedImages)
                },{
                    onError(it)
                }))

    }

    override fun onAddFromCameraClick() {

        if(permissionRequester.hasPermission(permission)){
            addFromCamera()
        }
        else if(data?.cameraPermissionRequested == true
                && !permissionRequester.shouldShowRequestPermissionRationale(permission) ){
            mainRouter.openAppSettings()
        }
        else{
            data?.cameraPermissionRequested = true
            mainRouter.permissionResponseListener = this
            permissionRequester.requestPermission(permission)
        }
    }

    override fun onSend() {

        val viewData = data ?: return

        view?.hideSoftKeyboard()

        if(!validateData(viewData))
            return

        val neededImages = MinSelectedImages - data?.entityItem?.images.orEmpty().size
        if(neededImages > 0){

            if(neededImages == 1)
                view?.onError(resourceProvider.getString(R.string.please_choose_one_more_images))
            else
                view?.onError(resourceProvider.getString(R.string.please_choose_min_images, neededImages))

            return
        }

        if( !networkInfoProvider.isNetworkConnected ) {

            view?.onError(resourceProvider.getString(R.string.internet_connection_offline))
            return
        }

        view?.onLoadingStateChange(true)

        add(Single.fromCallable{interactor.sendItem(viewData)}
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( { entityItem ->

                    onSendItemResult(viewData, entityItem)
                },{
                    onError(it)
                }))
    }

    protected fun isManualSectionValid(manualSectionId: String?): Boolean {

//        if (manualSectionId == null) {
//            view?.onError(resourceProvider.getString(R.string.input_section_number))
//            return false
//        }
//        if (!manualSectionId.matches(Regex("\\d+"))) {
//            view?.onError(resourceProvider.getString(R.string.error_section_id_contains_non_digits))
//            return false
//        }
//
//        if (manualSectionId.length != SectionNumLength) {
//            view?.onError(resourceProvider.getString(
//                R.string.error_section_must_contain_n_digits, SectionNumLength))
//            return false
//        }
        return true
    }

    override fun onImageDeleteClick(item: SendItemListItemImage, position: Int) {

        onImageDelete(item.image)
    }

    override fun onViewHide() {
        mainRouter.permissionResponseListener = null
        interactor.dispose()
        super.onViewHide()
    }

    override fun onError( throwable : Throwable? ){

        logger.e(TAG, throwable)

        view?.onLoadingStateChange(false )

        super.onError(throwable)
    }

    override fun onImagePreviewClick(position: Int) {

        val viewData = data?:return
        viewData.previewImageIndex = position - viewData.imagesIndexesOffset
        viewData.imagePreviewOpen = true

        view?.setData( viewData )
    }

    override fun onPreviewCloseClick(previewLastPosition: Int) {

        val viewData = data?:return
        viewData.imagePreviewOpen = false
        view?.setData( viewData )
    }

    override fun onPreviewDelete(previewPosition: Int, image: PreviewImage) {

        if(image is EntityItemImage)
            onImageDelete(image)
    }
    //endregion ISendItemPresenter implementation

    //region ISectionPickerPresenter implementation
    override fun onAbroadChecked(abroad: Boolean) {

        view?.onLoadingStateChange(true)

        add( Single.fromCallable{ interactor.loadSectionsData(
                SectionsViewData(if (abroad)
                    SectionViewType.Abroad
                else
                    SectionViewType.Home).apply {
                    isSectionRequired = data?.sectionsData?.isSectionRequired ?: true
                    hideUniqueUntilSectionIsSelected = data?.sectionsData?.hideUniqueUntilSectionIsSelected ?: false
                })}
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( { onSectionDataLoaded( it ) },{ th -> onError(th) }) )
    }

    override fun onCountrySelected(country: CountryRemote) {

        onSectionFieldSelected(country, { data, item ->
            interactor.onCountrySelected(data, item)
        })
    }

    override fun onElectionRegionSelected(electionRegion: ElectionRegionRemote) {

        onSectionFieldSelected(electionRegion, { data, item ->
            interactor.onElectionRegionSelected(data, item)
        })
    }

    override fun onMunicipalitySelected(municipality: MunicipalityRemote) {

        onSectionFieldSelected(municipality, { data, item ->
            interactor.onMunicipalitySelected(data, item)
        })
    }

    override fun onTownSelected(town: TownRemote) {

        onSectionFieldSelected(town, { data, item ->
            interactor.onTownSelected(data, item)
        })
    }

    override fun onCityRegionSelected(cityRegion: CityRegionRemote) {

        onSectionFieldSelected(cityRegion, { data, item ->
            interactor.onCityRegionSelected(data, item)
        })
    }

    override fun onSectionSelected(section: SectionRemote) {

        val sectionsData = data?.sectionsData ?:return
        data?.sectionsData = interactor.onSectionSelected(sectionsData, section)
        view?.hideSoftKeyboard()
    }

    override fun onSuccessOkClick() = mainRouter.showHomeScreen()

    override fun onMessageChanged(message: String) {
        
        data?.message = message
    }

    override fun onNamesChanged(names: String) {
        data?.names = names
    }

    override fun onPhoneChanged(phone: String) {
        data?.phone = phone
    }

    override fun onEmailChanged(email: String) {
        data?.email = email
    }

    override fun onHandleBack(imagePosition: Int?): Boolean {

        val viewData = data?: return false
        if(!viewData.imagePreviewOpen)
            return false

        viewData.imagePreviewOpen = false
        viewData.previewImageIndex = 0
        view?.setData(viewData)

        return true
    }
    //endregion ISectionPickerPresenter implementation

    // region IPermissionResponseListener implementation
    override fun onPermissionResult(permissionCode: Int, granted: Boolean) {

        if(permissionCode != PermissionCodes.CAMERA.code || !granted)
            return

        addFromCamera()
    }
    // endregion IPermissionResponseListener implementation

    //region Subscriptions
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onItemSendResultEvent(event: ItemSendResultEvent){

        loadData()
    }
    //endregion Subscriptions

    private fun <ItemType> onSectionFieldSelected(item:ItemType, loadMethod:(data: SectionsViewData, item:ItemType )-> SectionsViewData){

        val sectionsData = data?.sectionsData ?:return

        view?.onLoadingStateChange(true)

        add( Single.fromCallable{ loadMethod(sectionsData, item)}
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( { onSectionDataLoaded( it ) },{ th -> onError(th) }) )
    }

    private fun onDataLoaded(newData: SendItemViewData){

        val currentData = data?:
        return

        currentData.items.clear()
        currentData.items.addAll(newData.items)
        currentData.entityItem = newData.entityItem
        currentData.message = newData.message
        currentData.email = newData.email
        currentData.phone = newData.phone
        currentData.sectionsData = newData.sectionsData
        currentData.previewImageIndex = newData.previewImageIndex
        currentData.imagePreviewOpen = newData.imagePreviewOpen
        currentData.imagesIndexesOffset = newData.imagesIndexesOffset
        currentData.manualSectionId = newData.manualSectionId

        view?.onLoadingStateChange(false)
        view?.setData(currentData)

        when(currentData.entityItem?.sendStatus) {

            SendStatus.SendError -> {
                view?.onError(resourceProvider.getString(R.string.send_error_try_again))
                currentData.entityItem?.sendStatus = SendStatus.New
            }

            SendStatus.SendErrorInvalidSection -> {
                view?.onError(resourceProvider.getString(R.string.invalid_section_number))
                currentData.entityItem?.sendStatus = SendStatus.New
            }

            SendStatus.Sending -> view?.onLoadingStateChange(true)
        }
    }

    private fun onSectionDataLoaded(sectionsData: SectionsViewData){

        val currentData = data?:
        return

        view?.onLoadingStateChange(false)

        currentData.sectionsData = sectionsData

        if(currentData.sectionsData?.selectedSection != null)
            view?.hideSoftKeyboard()

        view?.setSectionsData(currentData)
    }

    private fun addFromCamera(){

        val currentData = data?: return

        view?.hideSoftKeyboard()
        view?.onLoadingStateChange(true)

        add( Single.fromCallable{

            val imageFilePath = fileRepository.createNewJpgFile(Folders.LocalPicturesFolder)!!.absolutePath
            val protocol= if( currentData.entityItem != null )
                currentData.entityItem!!
            else
                interactor.addNew()

            Pair(protocol, imageFilePath)
        }
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( { pair ->

                    view?.onLoadingStateChange(false)
                    currentData.entityItem = pair.first
                    val path = pair.second
                    cameraTakenImageProvider.cameraImagePath = path
                    mainRouter.openCamera(path)
                },{

                    view?.onLoadingStateChange(false)
                    onError(it)
                }))
    }
    private fun onSendItemResult(viewData:SendItemViewData, entityItem: EntityItem){

        when(entityItem.sendStatus){

            SendStatus.Send -> onItemSend(viewData, entityItem)
            SendStatus.SendError -> onError(null )
            else -> view?.onLoadingStateChange(true) // wait for upload service to do its job
        }
    }

    private fun onItemSend(viewData:SendItemViewData, entityItem: EntityItem){

        val newData = SendItemViewData(viewData)
        newData.entityItem = entityItem
        view?.onLoadingStateChange(false)
        newData.items.clear()
        newData.items.add(SendItemListItemSendSuccess(interactor.successMessageString))
        onDataLoaded(newData)
    }

    private fun onImageDelete(image: EntityItemImage){

        view?.onLoadingStateChange(true)
        add(Single.fromCallable{interactor.deleteImage(image)}
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( {
                    loadData()
                },{
                    onError(it)
                }))
    }

    companion object {

        private val TAG = SendItemPresenter::class.simpleName
        private val SectionNumLength = 9
    }
}
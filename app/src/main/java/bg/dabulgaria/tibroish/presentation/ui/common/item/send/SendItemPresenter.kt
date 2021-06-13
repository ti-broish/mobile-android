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
import bg.dabulgaria.tibroish.presentation.event.CameraPhotoTakenEvent
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.ISectionPickerPresenter
import bg.dabulgaria.tibroish.domain.locations.SectionViewType
import bg.dabulgaria.tibroish.domain.locations.SectionsViewData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


interface ISendItemPresenter : IBasePresenter<ISendItemView>, ISectionPickerPresenter {

    fun onAddFromGalleryClick()

    fun onAddFromCameraClick()

    fun onContinue()

    fun onImageDeleteClick(item: SendItemListItemImage, position:Int)

    fun onSuccessOkClick()

    fun onMessageChanged(message: String)
}

abstract class SendItemPresenter
constructor(private val schedulersProvider: ISchedulersProvider,
            private val mainRouter: IMainRouter,
            private val interactor: ISendItemInteractor,
            private val fileRepository: IFileRepository,
            private val logger: ILogger,
            disposableHandler: IDisposableHandler)
    : BasePresenter<ISendItemView>(disposableHandler), ISendItemPresenter {

    override val registerEventBus = true

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

        val currentData = data?:return

        view?.onLoadingStateChange(true)

        add( Single.fromCallable{ interactor.loadData(currentData)}
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
                    currentData.imageForCameraPath = pair.second
                    mainRouter.openCamera(currentData.imageForCameraPath)
                },{

                    view?.onLoadingStateChange(false)
                    onError(it)
                }))
    }

    override fun onContinue() {

        val viewData = data ?: return

        view?.hideSoftKeyboard()

        if(!validateData(viewData))
            return

        val neededImages = MinSelectedImages - data?.entityItem?.images.orEmpty().size
        if(neededImages > 0){

            view?.onError(resourceProvider.getString(R.string.please_choose_min_images, neededImages))
            return
        }

        view?.onLoadingStateChange(true)

        add(Single.fromCallable{interactor.sendItem(viewData)}
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( {

                    val newData = SendItemViewData(viewData)
                    view?.onLoadingStateChange(false)
                    newData.items.clear()
                    newData.items.add(SendItemListItemSendSuccess(interactor.successMessageString))
                    onDataLoaded(newData)
                },{
                    onError(it)
                }))
    }

    override fun onImageDeleteClick(item: SendItemListItemImage, position: Int) {

        data?: return

        view?.onLoadingStateChange(true)
        add(Single.fromCallable{interactor.deleteImage(item.image)}
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( {
                    loadData()
                },{
                    onError(it)
                }))
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
    //endregion ISendItemPresenter implementation

    //endregion ISectionPickerPresenter implementation
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
    //endregion ISectionPickerPresenter implementation

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
        currentData.sectionsData = newData.sectionsData

        view?.onLoadingStateChange(false)
        view?.setData(currentData)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCameraPhotoTakenEvent(event: CameraPhotoTakenEvent){

        val viewData = data
                ?:return

        if( viewData.imageForCameraPath.isEmpty())
            return

        view?.onLoadingStateChange(true)

        add( Completable.fromCallable { interactor.addCameraImage(viewData = viewData) }
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe({
                    loadData()
                }, {th->

                    logger.e(TAG, th)
                    view?.onLoadingStateChange(false)
                }))
    }

    companion object {

        private val TAG = SendItemPresenter::class.simpleName
    }
}
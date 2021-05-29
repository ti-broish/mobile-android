package bg.dabulgaria.tibroish.presentation.ui.protocol.add

import android.os.Bundle
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.locations.*
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.persistence.local.Folders
import bg.dabulgaria.tibroish.presentation.event.CameraPhotoTakenEvent
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.ISectionPickerPresenter
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.SectionViewType
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.SectionsViewData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


import javax.inject.Inject

interface IAddProtocolPresenter: ISectionPickerPresenter, IBasePresenter<IAddProtocolView> {

    fun onAddFromGalleryClick()

    fun onAddFromCameraClick()

    fun onContinue()

    fun onImageDeleteClick(item:AddProtocolListItemImage, position:Int)
}

class AddProtocolPresenter @Inject constructor(private val schedulersProvider : ISchedulersProvider,
                                               private val mainRouter : IMainRouter,
                                               private val interactor:IAddProtocolInteractor,
                                               private val fileRepository: IFileRepository,
                                               private val logger:ILogger,
                                               dispHandler: IDisposableHandler)
    : BasePresenter<IAddProtocolView>(dispHandler),
        IAddProtocolPresenter{

    override val registerEventBus = true

    var data :AddProtocolViewData? = null

    //region IAddProtocolPresenter implementation
    override fun onRestoreData(bundle: Bundle?) {
        bundle?.let {
            data = bundle.getSerializable(AddProtocolConstants.VIEW_DATA_KEY) as AddProtocolViewData?
        }
    }

    override fun onSaveData(outState: Bundle) {
        outState.putSerializable(AddProtocolConstants.VIEW_DATA_KEY, data)
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

        if( currentData.protocol == null ){

            val protocol = Protocol()
            add( Single.fromCallable{

                interactor.addNew(protocol)
            }
                    .subscribeOn(schedulersProvider.ioScheduler())
                    .observeOn(schedulersProvider.uiScheduler())
                    .subscribe( {
                        currentData.protocol = it
                        currentData.protocolId = it.id
                        mainRouter.showPhotoPicker(it.id)
                    },{
                        onError(it)
                    }))

            return
        }
        else {
            currentData.protocol?.id?.let { mainRouter.showPhotoPicker(it) }
        }
    }

    override fun onAddFromCameraClick() {

        val currentData = data?: return

        view?.onLoadingStateChange(true)
        add( Single.fromCallable{

            val imageFilePath = fileRepository.createNewJpgFile(Folders.LocalPicturesFolder)!!.absolutePath
            val protocol= if( currentData.protocol != null )
                currentData.protocol!!
            else
                interactor.addNew(Protocol())

            Pair(protocol, imageFilePath)
        }
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( { pair ->

                    view?.onLoadingStateChange(false)
                    currentData.protocol = pair.first
                    currentData.protocolId = pair.first.id
                    currentData.imageForCameraPath = pair.second
                    mainRouter.openCamera(currentData.imageForCameraPath)
                },{

                    view?.onLoadingStateChange(false)
                    onError(it)
                }))
    }

    override fun onContinue() {
        TODO("Not yet implemented")
    }

    override fun onImageDeleteClick(item: AddProtocolListItemImage, position: Int) {

        data?: return

        view?.onLoadingStateChange(true)
        add( Single.fromCallable{interactor.deleteImage(item.image)}
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
    //endregion IAddProtocolPresenter implementation

    //endregion ISectionPickerPresenter implementation
    override fun onAbroadChecked(abroad: Boolean) {

        view?.onLoadingStateChange(true)

        add( Single.fromCallable{ interactor.loadSectionsData(
                SectionsViewData(if(abroad)
                    SectionViewType.Abroad
                else
                    SectionViewType.Home))}
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( { onSectionDataLoaded( it ) },{ th -> onError(th) }) )
    }

    override fun onCountrySelected(country:CountryRemote){

        onSectionFieldSelected( country, { data, item ->
            interactor.onCountrySelected(data, item)
        })
    }

    override fun onElectionRegionSelected(electionRegion: ElectionRegionRemote) {

        onSectionFieldSelected( electionRegion, { data, item ->
            interactor.onElectionRegionSelected(data, item)
        })
    }

    override fun onMunicipalitySelected(municipality: MunicipalityRemote) {

        onSectionFieldSelected( municipality, { data, item ->
            interactor.onMunicipalitySelected(data, item)
        })
    }

    override fun onTownSelected(town: TownRemote) {

        onSectionFieldSelected( town, { data, item ->
            interactor.onTownSelected(data, item)
        })
    }

    override fun onCityRegionSelected(cityRegion: CityRegionRemote) {

        onSectionFieldSelected( cityRegion, { data, item ->
            interactor.onCityRegionSelected(data, item)
        })
    }

    //endregion ISectionPickerPresenter implementation

    private fun <ItemType> onSectionFieldSelected(item:ItemType, loadMethod:(data:SectionsViewData, item:ItemType )->SectionsViewData){

        val sectionsData = data?.sectionsData ?:return

        view?.onLoadingStateChange(true)

        add( Single.fromCallable{ loadMethod(sectionsData, item)}
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( { onSectionDataLoaded( it ) },{ th -> onError(th) }) )
    }

    private fun onDataLoaded(newData:AddProtocolViewData){

        val currentData = data?:
        return

        currentData.items.clear()
        currentData.items.addAll(newData.items)
        currentData.protocol = newData.protocol
        currentData.protocolId = newData.protocolId
        currentData.sectionsData = newData.sectionsData

        view?.onLoadingStateChange(false)
        view?.setData(currentData)
    }

    private fun onSectionDataLoaded(sectionsData:SectionsViewData){

        val currentData = data?:
        return

        view?.onLoadingStateChange(false)

        currentData.sectionsData = sectionsData

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

        private val TAG = AddProtocolPresenter::class.simpleName
    }
}

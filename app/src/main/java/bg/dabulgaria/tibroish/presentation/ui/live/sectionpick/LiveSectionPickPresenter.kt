package bg.dabulgaria.tibroish.presentation.ui.live.sectionpick


import android.os.Bundle
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.locations.*
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.stream.StreamRequest
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.persistence.remote.repo.TiBroishRemoteRepository
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.ISectionPickerInteractor
import bg.dabulgaria.tibroish.presentation.ui.common.sectionpicker.ISectionPickerPresenter
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface ILiveSectionPickPresenter :IBasePresenter<ILiveSectionPickView>, ISectionPickerPresenter {

    fun onContinue()
}

class LiveSectionPickPresenter @Inject constructor(private val schedulersProvider: ISchedulersProvider,
                                                   private val mainRouter: IMainRouter,
                                                   private val logger: ILogger,
                                                   private val interactor: ISectionPickerInteractor,
                                                   private val tiBroishRemoteRepository: TiBroishRemoteRepository,
                                                   dispHandler: IDisposableHandler)
    : BasePresenter<ILiveSectionPickView>(dispHandler),
        ILiveSectionPickPresenter{

    var data: LiveSectionPickViewData? = null

    override fun onRestoreData(bundle: Bundle?) {

        bundle?.let {

            data = bundle.getSerializable(LiveSectionPickConstants.KEY) as LiveSectionPickViewData?
        }
    }

    override fun onSaveData(outState: Bundle) {
        outState.putSerializable(LiveSectionPickConstants.KEY, data)
    }

    override fun loadData() {

        if(data == null)
            data = LiveSectionPickViewData()

        val data = data ?:return

        view?.onLoadingStateChange(true)

        add(Single.fromCallable{ interactor.loadSectionsData(data.sectionsData)}
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( { onSectionDataLoaded( it ) },{ th -> onError(th) }) )
    }

    override fun onContinue() {

        val viewData = data ?: return

        view?.hideSoftKeyboard()

        if(viewData.sectionsData?.selectedSection == null){

            view?.onError(resourceProvider.getString(R.string.please_choose_section))
            return
        }

        view?.onLoadingStateChange(true)

        add(Single.fromCallable {
            tiBroishRemoteRepository.getStream(StreamRequest(viewData.sectionsData!!.selectedSection!!.id))
        }.subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe({ result->

                    view?.onLoadingStateChange(false)
                    mainRouter.showLiveStream(result)
                }, { throwable->

                    view?.onLoadingStateChange(false)
                    logger.e(TAG, throwable)
                    onError(throwable)
                } )
        )
    }

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
    //endregion ISectionPickerPresenter implementation

    private fun <ItemType> onSectionFieldSelected(item:ItemType, loadMethod:(data: SectionsViewData, item:ItemType )-> SectionsViewData){

        val sectionsData = data?.sectionsData ?:return

        view?.onLoadingStateChange(true)

        add( Single.fromCallable{ loadMethod(sectionsData, item)}
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( { onSectionDataLoaded( it ) },{ th -> onError(th) }) )
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

    override fun onManualSectionChanged(sectionId: String) {}

    companion object{
        val TAG = LiveSectionPickPresenter::class.java.simpleName
    }
}
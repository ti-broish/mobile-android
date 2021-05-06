package bg.dabulgaria.tibroish.presentation.ui.protocol.add

import android.os.Bundle
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import io.reactivex.rxjava3.core.Single


import javax.inject.Inject

interface IAddProtocolPresenter: IBasePresenter<IAddProtocolView> {

    fun onAddFromGalleryClick()

    fun onAddFromCameraClick()

    fun onContinue()

    fun onImageDeleteClick(item:AddProtocolListItemImage, position:Int)
}

class AddProtocolPresenter @Inject constructor(private val schedulersProvider : ISchedulersProvider,
                                               private val mainRouter : IMainRouter,
                                               private val interactor:IAddProtocolInteractor,
                                               private val logger:ILogger,
                                               dispHandler: IDisposableHandler)
    : BasePresenter<IAddProtocolView>(dispHandler), IAddProtocolPresenter{

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
        //currentData.protocolId = 1

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
            add( Single.fromCallable{interactor.addNew(protocol)}
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
        TODO("Not yet implemented")
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
        super.onViewHide()
    }

    override fun onError( throwable : Throwable? ){

        logger.e(TAG, throwable)

        view?.onLoadingStateChange(false )

        super.onError(throwable)
    }
    //endregion IAddProtocolPresenter implementation

    private fun onDataLoaded(newData:AddProtocolViewData){

        val currentData = data?:
        return

        currentData.items.clear()
        currentData.items.addAll(newData.items)
        currentData.protocol = newData.protocol
        currentData.protocolId = newData.protocolId

        view?.onLoadingStateChange(false)
        view?.setData(currentData)
    }

    companion object {

        private val TAG = AddProtocolPresenter::class.simpleName
    }
}

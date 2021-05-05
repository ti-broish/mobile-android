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
}

class AddProtocolPresenter @Inject constructor(private val schedulersProvider : ISchedulersProvider,
                                               private val mainRouter : IMainRouter,
                                               private val interactor: IAddProtocolInteractor,
                                               private val logger : ILogger,
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

        data?.protocolId =1
    }

    override fun onAddFromCameraClick() {
        TODO("Not yet implemented")
    }

    override fun onAddFromGalleryClick() {

        val dat = data?: return

        if( dat.protocol == null ){

            val protocol = Protocol()
            add( Single.fromCallable{interactor.addNew(protocol)}
                    .subscribeOn(schedulersProvider.ioScheduler())
                    .observeOn(schedulersProvider.uiScheduler())
                    .subscribe( {
                        dat.protocol = it
                        mainRouter.showPhotoPicker(it.id)
                    },{
                        onError(it)
                    }))

            return
        }

        dat.protocol?.id?.let { mainRouter.showPhotoPicker(it) }
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



    companion object {

        private val TAG = AddProtocolPresenter::class.simpleName
    }
}

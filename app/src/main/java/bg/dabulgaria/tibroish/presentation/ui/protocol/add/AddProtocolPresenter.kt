package bg.dabulgaria.tibroish.presentation.ui.protocol.add

import android.os.Bundle
import android.util.Log
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.providers.INetworkInfoProvider
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider


import javax.inject.Inject

interface IAddProtocolPresenter: IBasePresenter<IAddProtocolView> {

}

class AddProtocolPresenter @Inject
constructor(private val schedulersProvider : ISchedulersProvider,
            private val resourceProvider : IResourceProvider,
            private val networkInfoProvider : INetworkInfoProvider,
            dispHandler: IDisposableHandler) : BasePresenter<IAddProtocolView>(dispHandler),
        IAddProtocolPresenter,
        IDisposableHandler by dispHandler{

    var data :AddProtocolViewData? = null

    override fun onRestoreData(bundle: Bundle?) {
        bundle?.let {
            data = bundle.getSerializable(AddProtocolConstants.VIEW_DATA_KEY) as AddProtocolViewData?
        }
    }

    override fun onSaveData(outState: Bundle) {
        outState.putSerializable(AddProtocolConstants.VIEW_DATA_KEY, data)
    }

    override fun loadData() {
    }

//    fun loadComicDetails(refresh: Boolean, id:Long ) {
//
//        add(getComicDetails(refresh, id ) )
//    }
//
//    private fun getComicDetails(refresh: Boolean, id:Long ) : Disposable {
//
//        return comicInteractor.getComic( refresh, id ).map {
//
//                return@map createFrom( it )
//            }
//                .subscribeOn( schedulersProvider.ioScheduler())
//                .observeOn( schedulersProvider.uiScheduler())
//                .subscribe({
//
//                    comicDetailsView?.onDetailsLoaded(it )
//                    comicDetailsView?.onLoadingStateChange(false )
//                },
//                        {
//
//                            onError( throwable = it )
//                        })
//
//    }

    private fun onError( throwable : Throwable ){

        Log.e(TAG, throwable.message, throwable)

        view?.onLoadingStateChange(false )

        val errMsg = resourceProvider.getString( if( !networkInfoProvider.isNetworkConnected )
            R.string.internet_connection_offline
        else
            R.string.oops_went_wrong_try )

        view?.onError( errMsg )
    }

    companion object {

        private val TAG = AddProtocolPresenter::class.simpleName
    }
}

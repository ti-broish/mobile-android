package bg.dabulgaria.tibroish.presentation.ui.protocol.add

import android.os.Bundle
import android.util.Log
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.infrastructure.permission.AppPermission
import bg.dabulgaria.tibroish.infrastructure.permission.IPermissionRequester
import bg.dabulgaria.tibroish.infrastructure.permission.PermissionCodes
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.providers.INetworkInfoProvider
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.presentation.main.IMainNavigator


import javax.inject.Inject

interface IAddProtocolPresenter: IBasePresenter<IAddProtocolView> {

    fun onAddFromGalleryClick()

    fun onAddFromCameraClick()
}

class AddProtocolPresenter @Inject constructor(private val schedulersProvider : ISchedulersProvider,
                                               private val resourceProvider : IResourceProvider,
                                               private val networkInfoProvider : INetworkInfoProvider,
                                               private val permissionRequester : IPermissionRequester,
                                               private val mainNavigator : IMainNavigator,
                                               dispHandler: IDisposableHandler)
    : BasePresenter<IAddProtocolView>(dispHandler), IAddProtocolPresenter{

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

    override fun onAddFromCameraClick() {

        val protocolId = data?.protocolId ?:return

        val permission = PermissionCodes.READ_STORAGE
        if(permissionRequester.hasPermission(permission)){
            mainNavigator.showPhotoPicker(protocolId)
        }
        else if(data?.photosPermissionRequested == true
                && !permissionRequester.shouldShowRequestPermissionRationale(permission) ){
            mainNavigator.openAppSettings()
        }
        else{
            data?.photosPermissionRequested = true
            permissionRequester.requestPermission(permission)
        }
    }

    override fun onAddFromGalleryClick() {
        TODO("Not yet implemented")
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

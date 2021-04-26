package bg.dabulgaria.tibroish.presentation.ui.photopicker.Camera//package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import android.os.Bundle
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.persistence.remote.ILocationsRemoteRepo
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.providers.INetworkInfoProvider
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.presentation.main.IMainNavigator
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import io.reactivex.rxjava3.core.Single

import io.reactivex.rxjava3.disposables.Disposable


import javax.inject.Inject

interface ICameraPickerPresenter : IBasePresenter<ICameraPickerView> {

    fun onImageClick(photo: PhotoItem)

    fun loadComics(refresh: Boolean )
}

class CameraPickerPresenter @Inject constructor(private val interactor : ICameraPickerInteractor,
                                               private val schedulersProvider : ISchedulersProvider,
                                               private val resourceProvider : IResourceProvider,
                                               private val networkInfoProvider : INetworkInfoProvider,
                                               private val logger: ILogger,
                                               private val mainRouter: IMainNavigator,
                                               private val locationsRemoteRepo: ILocationsRemoteRepo,
                                               dispHandler: IDisposableHandler,)
    : BasePresenter<ICameraPickerView>(dispHandler), ICameraPickerPresenter {

    var data :CameraPickerViewData? = null

    override fun onRestoreData(bundle: Bundle?) {
        bundle?.let {
            data = bundle.getSerializable(CameraPickerConstants.VIEW_DATA_KEY) as CameraPickerViewData?
        }
    }

    override fun onSaveData(outState: Bundle) {
        outState.putSerializable(CameraPickerConstants.VIEW_DATA_KEY, data)
    }

    override fun onImageClick(photo: PhotoItem) {

        TODO("Not Implemented")
    }

    override fun loadData() {
        TODO("Not yet implemented")
    }

    override fun loadComics(refresh: Boolean ) {
        add( getLocations( refresh ) )
    }

    private fun getLocations(refresh: Boolean) : Disposable {

        return Single.fromCallable{ locationsRemoteRepo.getLocations() }
                .subscribeOn( schedulersProvider.ioScheduler())
                .observeOn( schedulersProvider.uiScheduler())
                .subscribe({

                    view?.onComicsLoaded(it)
                    view?.onLoadingStateChange(false )
                },
                        {

                            onError( throwable = it )
                        })

    }

    private fun onError( throwable : Throwable ){

        logger.e(TAG, throwable.message, throwable)

        view?.onLoadingStateChange(false )

        val resId =
                if( !networkInfoProvider.isNetworkConnected )
                    R.string.internet_connection_offline
                else
                    R.string.oops_went_wrong_try

        view?.onError(resourceProvider.getString(resId ) )
    }


    companion object {

        private val TAG = CameraPickerPresenter::class.simpleName
        private val LIST_LIMIT = 20L
    }
}

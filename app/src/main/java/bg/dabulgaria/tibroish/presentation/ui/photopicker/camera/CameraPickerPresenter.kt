package bg.dabulgaria.tibroish.presentation.ui.photopicker.camera//package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import android.os.Bundle
import bg.dabulgaria.tibroish.domain.image.PickedImage
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider


import javax.inject.Inject

interface ICameraPickerPresenter : IBasePresenter<ICameraPickerView> {

    fun onImageClick(photo: PickedImage)

    fun reload()
}

class CameraPickerPresenter @Inject constructor(private val interactor : ICameraPickerInteractor,
                                                private val schedulersProvider : ISchedulersProvider,
                                                private val logger: ILogger,
                                                private val mainRouter: IMainRouter,
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

    override fun loadData() {

    }

    override fun reload() {

        loadData()
    }

    override fun onImageClick(photo: PickedImage) {

        TODO("Not Implemented")
    }


    override fun onError( throwable : Throwable? ){

        logger.e(TAG, throwable)

        view?.onLoadingStateChange(false )
    }


    companion object {

        private val TAG = CameraPickerPresenter::class.simpleName
        private val LIST_LIMIT = 20L
    }
}

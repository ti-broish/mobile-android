package bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery//package bg.dabulgaria.tibroish.presentation.ui.protocol.list


import android.os.Bundle
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.infrastructure.permission.IPermissionRequester
import bg.dabulgaria.tibroish.infrastructure.permission.PermissionCodes
import bg.dabulgaria.tibroish.infrastructure.schedulers.ISchedulersProvider
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.main.IPermissionResponseListener
import bg.dabulgaria.tibroish.presentation.providers.IGallerySelectedImagesProvider
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface IPhotoPickerPresenter : IBasePresenter<IPhotoPickerView> {

    fun onImageClick(photo: PhotoItem, index:Int)

    fun onRequestPermissionClick()

    fun onDoneClick()
}

class PhotoPickerPresenter @Inject constructor(private val interactor : IPhotoPickerInteractor,
                                               private val schedulersProvider : ISchedulersProvider,
                                               private val logger: ILogger,
                                               private val mainRouter: IMainRouter,
                                               dispHandler: IDisposableHandler,
                                               private val permissionRequester : IPermissionRequester,
                                               private val selectedImagesProvider: IGallerySelectedImagesProvider)
    : BasePresenter<IPhotoPickerView>(dispHandler), IPhotoPickerPresenter, IPermissionResponseListener {

    var data : PhotoPickerViewData? = null
    val permission = PermissionCodes.READ_STORAGE

    override fun onRestoreData(bundle: Bundle?) {
        bundle?.let {
            data = bundle.getSerializable(PhotoPickerConstants.VIEW_DATA_KEY) as PhotoPickerViewData?
        }
    }

    override fun onSaveData(outState: Bundle)
        = outState.putSerializable(PhotoPickerConstants.VIEW_DATA_KEY, data)

    override fun loadData() {

        val viewData = data?:return
        view?.onLoadingStateChange(ViewState.Loading)

        if(!permissionRequester.hasPermission(permission)){

            view?.onLoadingStateChange(ViewState.NoPermission)
            return
        }

        add( Single.fromCallable{interactor.loadImages(viewData, 3)}
                .subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe( { onLoaded(it) },{ onError(it) }))
    }

    override fun onImageClick(photo: PhotoItem, index:Int) {

        val selected = !photo.isSelected

        val item = data?.photoItems?.find { it.id == photo.id && it.source == photo.source }
                ?: return

        item.isSelected  = selected
        photo.isSelected = selected

        val selectedIndex = data?.selectedPhotos?.indexOf( item ) ?:-1
        if( selected && selectedIndex < 0 ){
            data?.selectedPhotos?.add(item)
        }
        else if( selectedIndex >= 0 )
            data?.selectedPhotos?.removeAt( selectedIndex )

        view?.onItemUpdated(item, index)
    }

    override fun onDoneClick() {

        val currentData = data?:return

        selectedImagesProvider.selectedImages.addAll(currentData.selectedPhotos)
        mainRouter.navigateBack()
    }

    override fun onRequestPermissionClick() {

        if(permissionRequester.hasPermission(permission)){
            loadData()
        }
        else if(data?.photosPermissionRequested == true
                && !permissionRequester.shouldShowRequestPermissionRationale(permission) ){
            mainRouter.openAppSettings()
        }
        else{
            data?.photosPermissionRequested = true
            mainRouter.permissionResponseListener = this
            permissionRequester.requestPermission(permission)
        }
    }

    override fun onError( throwable : Throwable? ){

        logger.e(TAG, throwable)

        view?.onLoadingStateChange(ViewState.Error)

        super.onError(throwable)
    }

    override fun onPermissionResult(permissionCode: Int, granted: Boolean) {

        if( permissionCode != PermissionCodes.READ_STORAGE.code)
            return

        loadData()
    }

    private fun onPhotosAdded(){

        mainRouter.navigateBack()
    }

    private fun onLoaded(list: List<PhotoItem>){

        data?.photoItems?.clear()
        data?.photoItems?.addAll( list )
        data?.let{ view?.onDataLoaded( it ) }
        view?.onLoadingStateChange(ViewState.Loaded)
    }

    companion object {

        private val TAG = PhotoPickerPresenter::class.simpleName
    }
}

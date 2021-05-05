package bg.dabulgaria.tibroish.presentation.base

import android.os.Bundle
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.providers.INetworkInfoProvider
import bg.dabulgaria.tibroish.presentation.providers.IResourceProvider
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.PhotoPickerPresenter
import javax.inject.Inject

interface IBasePresenter<IView: IBaseView>  {

    fun onRestoreData(bundle: Bundle?)

    fun onViewCreated( view:IView )

    fun onViewDestroyed()

    fun onViewShow()

    fun onViewHide()

    fun onSaveData(outState: Bundle)

    fun loadData()

    fun onError( throwable : Throwable? )
}

abstract class BasePresenter<IView:IBaseView> ( disposableHandler: IDisposableHandler)
    :IBasePresenter<IView>, IDisposableHandler by disposableHandler{

    var view:IView? = null

    @Inject
    lateinit var networkInfoProvider :INetworkInfoProvider

    @Inject
    lateinit var resourceProvider :IResourceProvider

    override fun onViewCreated(view: IView) {
        this.view = view
    }

    override fun onViewDestroyed() {
        this.view = null
    }

    override fun onViewShow() {
        loadData()
    }

    override fun onViewHide() {
    }

    override fun onError( throwable : Throwable?){

        val resId = if( !networkInfoProvider.isNetworkConnected )
                    R.string.internet_connection_offline
                else
                    R.string.oops_went_wrong_try

        view?.onError(resourceProvider.getString(resId ) )
    }

}
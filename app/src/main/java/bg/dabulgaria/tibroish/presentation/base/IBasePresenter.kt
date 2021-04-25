package bg.dabulgaria.tibroish.presentation.base

import android.os.Bundle

interface IBasePresenter<IView: IBaseView>  {

    fun onRestoreData(bundle: Bundle?)

    fun onViewCreated( view:IView )

    fun onViewDestroyed()

    fun onViewShow()

    fun onViewHide()

    fun onSaveData(outState: Bundle)
}

abstract class BasePresenter<IView:IBaseView> ( disposableHandler: IDisposableHandler)
    :IBasePresenter<IView>, IDisposableHandler by disposableHandler{

    var view:IView? = null

    abstract fun loadData()

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

}
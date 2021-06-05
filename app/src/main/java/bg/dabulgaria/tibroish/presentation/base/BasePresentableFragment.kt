package bg.dabulgaria.tibroish.presentation.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import javax.inject.Inject

open class BasePresentableFragment<IView:IBaseView, IPresenter:IBasePresenter<IView>>: BaseFragment(), IBaseView {

    @Inject
    lateinit var presenter:IPresenter

    private fun getBaseView():IView = this as IView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.onRestoreData(if( arguments != null ) arguments else  savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.onViewCreated( getBaseView() )
    }


    override fun onStart() {
        super.onStart()
        presenter.onViewShow()
    }

    override fun onStop() {
        presenter.onViewHide()
        super.onStop()
    }

    override fun onDestroyView() {
        presenter.onViewDestroyed()
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        presenter.onSaveData(outState = outState)
        super.onSaveInstanceState(outState)
    }

    override fun onError(errorMessage: String) {

        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
    }
}
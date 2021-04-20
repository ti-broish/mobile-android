package bg.dabulgaria.tibroish.presentation.base

import android.os.Bundle
import android.view.View
import javax.inject.Inject

class BasePresentableFragment<IView:IBaseView, IPresenter:IBasePresenter<IView>>: BaseFragment() {


    @Inject
    lateinit var presenter:IPresenter

    private fun getBaseView():IView = this as IView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.onViewCreated( getBaseView() )
    }

    override fun onDestroyView() {
        presenter.onViewDestroyed()
        super.onDestroyView()
    }


    override fun onStart() {
        super.onStart()
        presenter.onViewShow()
    }

    override fun onStop() {
        presenter.onViewHide()
        super.onStop()
    }

}
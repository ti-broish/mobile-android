package bg.dabulgaria.tibroish.presentation.ui.licenses


import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.presentation.ui.common.IHtmlTextUtil
import javax.inject.Inject

interface ILicensesView : IBaseView {

}

class LicensesFragment constructor()
    : BasePresentableFragment<ILicensesView, ILicensesPresenter>(),
    ILicensesView {

    @Inject
    lateinit var htmlTextUtil: IHtmlTextUtil

    private fun getLicensesTextView() = view?.findViewById<TextView>(R.id.licensesText)

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View? {
        return inflater.inflate(R.layout.fragment_licenses, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getLicensesTextView()?.text = htmlTextUtil.convertTextToHtml(presenter.getLicensesText())
    }

    override fun onError(errorMessage: String) {

        dialogUtil.showDismissableDialog(activity= requireActivity(),
                message = errorMessage,
                dismissCallback = {} )
    }

    companion object {

        val TAG = LicensesFragment::class.java.simpleName

        fun newInstance(licensesViewData: LicensesViewData):
                LicensesFragment {

            return LicensesFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(LicensesConstants.VIEW_DATA_KEY, licensesViewData)
                }
            }
        }
    }
}

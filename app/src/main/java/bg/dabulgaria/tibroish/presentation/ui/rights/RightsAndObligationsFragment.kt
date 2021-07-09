package bg.dabulgaria.tibroish.presentation.ui.rights


import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.presentation.ui.common.DialogUtil
import bg.dabulgaria.tibroish.presentation.ui.common.IHtmlTextUtil
import javax.inject.Inject

interface IRightsAndObligationsView : IBaseView {

}

class RightsAndObligationsFragment constructor()
    : BasePresentableFragment<IRightsAndObligationsView, IRightsAndObligationsPresenter>(), IRightsAndObligationsView {

    @Inject
    lateinit var htmlTextUtil: IHtmlTextUtil

    private fun getRightsAndObligationsTextView() = view?.findViewById<TextView>(R.id.rightsAndObligationsText)

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View? {
        return inflater.inflate(R.layout.fragment_rights, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getRightsAndObligationsTextView()?.text =
            htmlTextUtil.convertTextToHtml(presenter.getRightsAndObligationsText())
    }

    override fun onError(errorMessage: String) {

        dialogUtil.showDismissableDialog(activity= requireActivity(),
                message = errorMessage,
                dismissCallback = {} )
    }

    companion object {

        val TAG = RightsAndObligationsFragment::class.java.simpleName

        fun newInstance(RightsAndObligationsViewData: RightsAndObligationsViewData): RightsAndObligationsFragment {

            return RightsAndObligationsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(RightsAndObligationsConstants.VIEW_DATA_KEY, RightsAndObligationsViewData)
                }
            }
        }
    }
}

package bg.dabulgaria.tibroish.presentation.ui.rights


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import javax.inject.Inject

interface IRightsAndObligationsView : IBaseView {

}

class RightsAndObligationsFragment @Inject constructor()
    : BasePresentableFragment<IRightsAndObligationsView, IRightsAndObligationsPresenter>(), IRightsAndObligationsView {

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View? {
        return inflater.inflate(R.layout.fragment_rights, container, false)
    }

    override fun onError(errorMessage: String) {

        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
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

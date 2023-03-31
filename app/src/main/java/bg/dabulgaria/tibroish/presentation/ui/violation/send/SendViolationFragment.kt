package bg.dabulgaria.tibroish.presentation.ui.violation.send


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.navigation.BackHandlerInterface
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemConstants
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemFragment
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemViewData
import javax.inject.Inject


class SendViolationFragment @Inject constructor() : SendItemFragment<ISendViolationPresenter>() {

    private var backHandlerInterface: BackHandlerInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity is BackHandlerInterface)
            backHandlerInterface = activity as BackHandlerInterface?
        else
            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_send_item, container, false)
    }

    companion object {

        val TAG = SendViolationFragment::class.java.simpleName

        fun newInstance(sendItemViewData: SendItemViewData): SendViolationFragment {

            return SendViolationFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(SendItemConstants.VIEW_DATA_KEY, sendItemViewData)
                }
            }
        }
    }
}

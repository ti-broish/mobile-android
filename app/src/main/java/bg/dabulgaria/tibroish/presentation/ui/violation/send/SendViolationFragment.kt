package bg.dabulgaria.tibroish.presentation.ui.violation.send


import android.os.Bundle
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemConstants
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemFragment
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemViewData
import javax.inject.Inject


class SendViolationFragment @Inject constructor() : SendItemFragment<ISendViolationPresenter>() {

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

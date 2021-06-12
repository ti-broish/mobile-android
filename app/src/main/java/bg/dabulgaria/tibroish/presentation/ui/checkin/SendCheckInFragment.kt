package bg.dabulgaria.tibroish.presentation.ui.checkin


import android.os.Bundle
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemConstants
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemFragment
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemViewData
import javax.inject.Inject

class SendCheckInFragment @Inject constructor() : SendItemFragment<ISendCheckInPresenter>() {

    companion object {

        val TAG = SendCheckInFragment::class.java.simpleName

        fun newInstance(sendItemViewData: SendItemViewData): SendCheckInFragment {

            return SendCheckInFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(SendItemConstants.VIEW_DATA_KEY, sendItemViewData)
                }
            }
        }
    }
}

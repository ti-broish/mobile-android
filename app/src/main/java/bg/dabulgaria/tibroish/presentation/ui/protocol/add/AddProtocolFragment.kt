package bg.dabulgaria.tibroish.presentation.ui.protocol.add


import android.os.Bundle
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemConstants
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemFragment
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemViewData
import javax.inject.Inject

class AddProtocolFragment @Inject constructor()
    : SendItemFragment<IAddProtocolPresenter>() {
    
    companion object {

        val TAG = AddProtocolFragment::class.java.simpleName

        fun newInstance(sendItemViewData: SendItemViewData): AddProtocolFragment {

            return AddProtocolFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(SendItemConstants.VIEW_DATA_KEY, sendItemViewData)
                }
            }
        }
    }
}

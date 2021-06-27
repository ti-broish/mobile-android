package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import android.os.Parcelable
import bg.dabulgaria.tibroish.domain.protocol.ProtocolRemote
import kotlinx.android.parcel.Parcelize

@Parcelize
class ProtocolsViewData(
    var userProtocols: List<ProtocolRemote>,
    var state: ProtocolsPresenter.State) : Parcelable {
}

class ProtocolsConstants {
    companion object {
        const val VIEW_DATA_KEY = "ProtocolsConstants.ProtocolsViewData"
    }
}
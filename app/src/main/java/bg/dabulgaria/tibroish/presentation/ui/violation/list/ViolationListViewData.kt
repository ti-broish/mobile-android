package bg.dabulgaria.tibroish.presentation.ui.violation.list

import android.os.Parcelable
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import kotlinx.android.parcel.Parcelize

@Parcelize
class ViolationsViewData(
    var userViolations: List<VoteViolationRemote>,
    var state: ViolationsListPresenter.State
) : Parcelable {
}

class ViolationConstants {
    companion object {
        const val VIEW_DATA_KEY = "ViolationConstants.ViolationsViewData"
    }
}
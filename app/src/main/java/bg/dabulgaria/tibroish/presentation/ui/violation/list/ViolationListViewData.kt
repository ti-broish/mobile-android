package bg.dabulgaria.tibroish.presentation.ui.violation.list

import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import java.io.Serializable

class ViolationListViewData: Serializable {

    val items = mutableListOf<VoteViolationRemote>()
}
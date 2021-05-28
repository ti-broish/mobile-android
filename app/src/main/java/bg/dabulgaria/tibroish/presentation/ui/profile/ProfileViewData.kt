package bg.dabulgaria.tibroish.presentation.ui.profile

import android.os.Parcelable
import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.domain.user.User
import kotlinx.android.parcel.Parcelize

@Parcelize
class ProfileViewData(
        val organizationsData: MutableList<Organization>,
        var userDetails: User?) : Parcelable

class ProfileConstants {
    companion object {
        const val VIEW_DATA_KEY = "ProfileConstants.ProfileViewData"
    }
}
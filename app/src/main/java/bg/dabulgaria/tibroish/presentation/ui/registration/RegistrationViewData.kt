package bg.dabulgaria.tibroish.presentation.ui.registration

import android.os.Parcelable
import bg.dabulgaria.tibroish.domain.organisation.Organization
import kotlinx.android.parcel.Parcelize

@Parcelize
class RegistrationViewData(
        val countryCodesData: MutableList<CountryCode>,
        val organizationsData: MutableList<Organization>) : Parcelable

class RegistrationConstants {
    companion object {
        val VIEW_DATA_KEY = "RegistrationConstants.RegistrationViewData"
    }
}
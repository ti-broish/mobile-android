package bg.dabulgaria.tibroish.presentation.ui.registration

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class RegistrationOrganization (val name: String, val id: BigDecimal) : Parcelable
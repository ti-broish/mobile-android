package bg.dabulgaria.tibroish.domain.violation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ViolationMetadata(
    val violationId: Long,
    val sectionId: String,
    val townId: Long?,
    val description: String): Parcelable
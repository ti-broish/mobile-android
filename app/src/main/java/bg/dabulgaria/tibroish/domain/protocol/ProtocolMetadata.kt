package bg.dabulgaria.tibroish.domain.protocol

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProtocolMetadata(
    val protocolId: Long,
    val sectionId: String?): Parcelable
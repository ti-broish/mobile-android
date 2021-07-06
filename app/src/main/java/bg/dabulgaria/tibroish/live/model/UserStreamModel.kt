package bg.dabulgaria.tibroish.live.model

import android.os.Parcelable
import bg.dabulgaria.tibroish.domain.locations.SectionRemote
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserStreamModel(
        @SerializedName("id") val id: String,
        @SerializedName("section") val section: SectionRemote?,
        @SerializedName("streamUrl") val streamUrl: String?,
        @SerializedName("viewUrl") val viewUrl: String?,
        @SerializedName("audioDisabled") val audioDisabled: Boolean?) : Parcelable

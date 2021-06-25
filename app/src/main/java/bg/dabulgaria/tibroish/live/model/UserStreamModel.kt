package bg.dabulgaria.tibroish.live.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserStreamModel(
        @SerializedName("streamUrl") val streamUrl: String?,
        @SerializedName("viewUrl") val viewUrl: String?,
        @SerializedName("audioDisabled") val audioDisabled: Boolean?) : Parcelable

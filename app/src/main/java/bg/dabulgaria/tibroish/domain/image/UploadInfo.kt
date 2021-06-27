package bg.dabulgaria.tibroish.domain.image

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

enum class UploadType {
    PROTOCOL,
    VIOLATION
}

@Parcelize
data class UploadInfo(
    val uploadType: UploadType,
    val metadata: Parcelable): Parcelable
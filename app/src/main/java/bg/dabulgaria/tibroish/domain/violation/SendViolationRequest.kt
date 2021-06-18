package bg.dabulgaria.tibroish.domain.violation

import bg.dabulgaria.tibroish.domain.image.PictureDto
import bg.dabulgaria.tibroish.domain.locations.SectionRemote
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SendViolationRequest constructor(
        @SerializedName("section")
        val section: String,//section.id
        @SerializedName("town")
        val town: Long?,//town.id
        @SerializedName("pictures")//picture server id's
        val pictures: List<String>,
        @SerializedName("description")
        val description: String) : Serializable

class VoteViolationRemote(
        val id: String,
        val section: SectionRemote?,
        val pictures: List<PictureDto>,
        val description: String,
        val status: ViolationRemoteStatus,
        val statusLocalized: String,
        var errorMessage: String?) : Serializable
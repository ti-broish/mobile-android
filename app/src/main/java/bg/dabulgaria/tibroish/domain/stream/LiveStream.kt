package bg.dabulgaria.tibroish.domain.stream

import bg.dabulgaria.tibroish.domain.locations.SectionRemote
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StreamRequest(@SerializedName("section") val section: String/*section.id*/ ): Serializable

class StreamResponse(): Serializable{

    var id: String=""
    var section: SectionRemote?=null
    var streamUrl: String = ""
    var viewUrl: String = ""
}


package bg.dabulgaria.tibroish.persistence.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TownsRequestParams constructor(

    @SerializedName("country")
    val countryCode: String,
    @SerializedName("election_region")
    val electionRegionCode:String?,
    @SerializedName("municipality")
    val municipalityCode:String?
):Serializable

class SectionsRequestParams constructor(

        @SerializedName("town")
        val townId: Long,
        @SerializedName("city_region")
        val cityRegionCode:String?
):Serializable
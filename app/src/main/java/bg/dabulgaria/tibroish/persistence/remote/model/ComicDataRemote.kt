package bg.dabulgaria.tibroish.persistence.remote.model

import com.google.gson.annotations.SerializedName

data class ComicDataRemote
 constructor(
    @SerializedName("count")
    var count: Int = 0,

    @SerializedName("total")
    var total: Int =0,

    @SerializedName("results")
    var results: List<ComicRemote>? = null
    ){
}
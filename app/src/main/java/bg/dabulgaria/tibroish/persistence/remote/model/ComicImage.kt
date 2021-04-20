package bg.dabulgaria.tibroish.persistence.remote.model

import com.google.gson.annotations.SerializedName

data class ComicImage (

    @SerializedName("path")
    var path: String? = null,

    @SerializedName("extension")
    var extension: String? = null){

    val url : String get()= "$path.$extension"
}
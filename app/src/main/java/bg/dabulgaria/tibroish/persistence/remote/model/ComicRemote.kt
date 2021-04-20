package bg.dabulgaria.tibroish.persistence.remote.model

import com.google.gson.annotations.SerializedName

data class ComicRemote ( @SerializedName("id")
                         var id: Long? = null,

                         @SerializedName("title")
                         var title: String? = null,

                        @SerializedName("detailLink")
                        var description: String? = null,

                        @SerializedName("thumbnail")
                        var thumbnail: ComicImage? = null) {
}
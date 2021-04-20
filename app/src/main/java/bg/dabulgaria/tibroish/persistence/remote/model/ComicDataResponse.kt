package bg.dabulgaria.tibroish.persistence.remote.model

import com.google.gson.annotations.SerializedName

data class ComicDataResponse
    constructor(
            @SerializedName("status")
            var status: String ?= null,

            @SerializedName("code")
            var code: Int ?= null,

            @SerializedName("data")
            var data: ComicDataRemote ?= null ){
}
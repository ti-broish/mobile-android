package bg.dabulgaria.tibroish.domain.image

import java.io.Serializable

class UploadImage(val filename: String,
                  val filepath: String,
                  val webviewPath: String?,
                  val data: String, // base64 data
                  val isSelected: Boolean,
                  val index: Number) :Serializable {
}

class UploadImageRequest(val image:UploadImage) :Serializable


class UploadImageResponse(val id: String,
                          val url: String?,
                          val sortPosition: Int,
                          val path: String?,
                          val index: Int?):Serializable
package bg.dabulgaria.tibroish.domain.image

import bg.dabulgaria.tibroish.domain.send.ImageSendStatus

interface ImageBase {
    fun getSendImageStatus(): ImageSendStatus
    fun setSendImageStatus(status: ImageSendStatus)
    fun getFileLocalPath(): String
    fun setImageServerId(serverId: String)
    fun setImageServerUrl(serverUrl: String)
}
package bg.dabulgaria.tibroish.domain.image

import bg.dabulgaria.tibroish.domain.send.ImageSendStatus

interface ImageBase {
    fun getImageSendStatus(): ImageSendStatus
    fun setImageSendStatus(status: ImageSendStatus)
    fun getLocalFilePath(): String
    fun setServerId(serverId: String)
    fun setServerUrl(serverUrl: String)
}
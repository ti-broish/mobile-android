package bg.dabulgaria.tibroish.domain.protocol.image

import bg.dabulgaria.tibroish.domain.image.IEntityImageUploader
import bg.dabulgaria.tibroish.domain.image.UploadImage
import bg.dabulgaria.tibroish.domain.image.UploadImageRequest
import bg.dabulgaria.tibroish.domain.image.UploadImageResponse
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.protocol.IProtocolsRepository
import bg.dabulgaria.tibroish.domain.send.ImageSendStatus
import bg.dabulgaria.tibroish.domain.send.SendStatus
import javax.inject.Inject

interface IProtocolImageUploader : IEntityImageUploader

class ProtocolImageUploader @Inject constructor(private val tiBroishRemoteRepo: ITiBroishRemoteRepository,
                                                private val protocolImagesLocalRepo: IProtocolImagesRepository,
                                                private val protocolLocalRepo: IProtocolsRepository,
                                                private val fileRepo: IFileRepository)
    : IProtocolImageUploader {

    override fun uploadImages(entityId: Long) {

        val protocol = protocolLocalRepo.get(entityId)
                ?: return

        if (protocol.status == SendStatus.Send)
            return

        val images = protocolImagesLocalRepo.getByProtocolId(entityId)

        images.mapIndexed { index, protocolImage -> uploadImage(index, protocolImage) }
    }

    private fun uploadImage(index: Int, protocolImage: ProtocolImage) {

        if (protocolImage.imageSendStatus == ImageSendStatus.Uploaded)
            return

        val fileName = fileRepo.getFileName(protocolImage.localFilePath)

        if (fileName.isNullOrEmpty()) {

            markWithStatus(protocolImage, ImageSendStatus.Replace)
            return
        }

        val fileContentB64 = fileRepo.getFileContentBase64Encoded(protocolImage.localFilePath)

        if (fileContentB64.isNullOrEmpty()) {

            markWithStatus(protocolImage, ImageSendStatus.Replace)
            return
        }

        val uploadImageRequest = UploadImageRequest(fileContentB64)

        val response = tiBroishRemoteRepo.uploadImage(uploadImageRequest)

        updateUploaded(protocolImage, response)
    }

    private fun updateUploaded(protocolImage: ProtocolImage, response: UploadImageResponse) {

        if (response.id.isNullOrEmpty()
                || response.url.isNullOrEmpty())
            return

        protocolImage.serverId = response.id
        protocolImage.serverUrl = response.url
        markWithStatus(protocolImage, ImageSendStatus.Uploaded)
    }

    private fun markWithStatus(protocolImage: ProtocolImage, imageSendStatus: ImageSendStatus) {

        protocolImage.imageSendStatus = imageSendStatus
        protocolImagesLocalRepo.update(protocolImage)
    }
}
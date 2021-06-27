package bg.dabulgaria.tibroish.domain.violation.image

import bg.dabulgaria.tibroish.domain.image.IEntityImageUploader
import bg.dabulgaria.tibroish.domain.image.UploadImageRequest
import bg.dabulgaria.tibroish.domain.image.UploadImageResponse
import bg.dabulgaria.tibroish.domain.io.IFileRepository
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.send.ImageSendStatus
import bg.dabulgaria.tibroish.domain.send.SendStatus
import bg.dabulgaria.tibroish.domain.violation.IViolationRepository
import javax.inject.Inject

interface IViolationImageUploader : IEntityImageUploader

class ViolationImageUploader @Inject constructor(private val tiBroishRemoteRepo: ITiBroishRemoteRepository,
                                                 private val violationImagesLocalRepo: IViolationImagesRepository,
                                                 private val violationLocalRepo: IViolationRepository,
                                                 private val fileRepo: IFileRepository)
    : IViolationImageUploader {

    override fun uploadImages(entityId: Long) {

        val violation = violationLocalRepo.get(entityId)
                ?: return

        if (violation.status == SendStatus.Send)
            return

        val images = violationImagesLocalRepo.getByViolationId(entityId)

        images.map { violationImage -> uploadImage(violationImage) }
    }

    private fun uploadImage(violationImage: ViolationImage) {

        if (violationImage.imageSendStatus == ImageSendStatus.Uploaded)
            return

        val fileName = fileRepo.getFileName(violationImage.localFilePath)

        if (fileName.isNullOrEmpty()) {

            markWithStatus(violationImage, ImageSendStatus.Replace)
            return
        }

        val fileContentB64 = fileRepo.getFileContentBase64Encoded(violationImage.localFilePath)

        if (fileContentB64.isNullOrEmpty()) {

            markWithStatus(violationImage, ImageSendStatus.Replace)
            return
        }

        val uploadImageRequest = UploadImageRequest(fileContentB64)

        val response = tiBroishRemoteRepo.uploadImage(uploadImageRequest)

        updateUploaded(violationImage, response)
    }

    private fun updateUploaded(violationImage: ViolationImage, response: UploadImageResponse) {

        if (response.id.isNullOrEmpty()
                || response.url.isNullOrEmpty())
            return

        violationImage.serverId = response.id
        violationImage.serverUrl = response.url
        markWithStatus(violationImage, ImageSendStatus.Uploaded)
    }

    private fun markWithStatus(violationImage: ViolationImage, imageSendStatus: ImageSendStatus) {

        violationImage.imageSendStatus = imageSendStatus
        violationImagesLocalRepo.update(violationImage)
    }
}
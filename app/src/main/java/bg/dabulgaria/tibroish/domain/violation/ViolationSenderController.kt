package bg.dabulgaria.tibroish.domain.violation

import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.send.SendStatus
import bg.dabulgaria.tibroish.domain.violation.image.IViolationImageUploader
import bg.dabulgaria.tibroish.domain.violation.image.IViolationImagesRepository

class ViolationSenderController(
    val violationImageUploader: IViolationImageUploader,
    val violationsRepo: IViolationRepository,
    val violationImagesRepo: IViolationImagesRepository,
    val tiBroishRemoteRepository: ITiBroishRemoteRepository) : IViolationSenderController {

    override fun upload(metadata: ViolationMetadata) {
        val violationId = metadata.violationId
        violationImageUploader.uploadImages(violationId)

        val images = violationImagesRepo.getByViolationId(violationId)

        val request = SendViolationRequest( metadata.sectionId,
            metadata.townId,
            images.map { it.serverId },
            metadata.description)

        val response = tiBroishRemoteRepository.sendViolation(request)

        val violation = violationsRepo.get(violationId)!!
        violation.remoteStatus = response.status
        violation.serverId = response.id
        violation.message = metadata.description
        violation.status = SendStatus.Send
        violationsRepo.update(violation)
    }
}
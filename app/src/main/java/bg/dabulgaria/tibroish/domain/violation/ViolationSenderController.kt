package bg.dabulgaria.tibroish.domain.violation

import android.content.Context
import android.content.Intent
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.send.SendStatus
import bg.dabulgaria.tibroish.domain.violation.image.IViolationImageUploader
import bg.dabulgaria.tibroish.domain.violation.image.IViolationImagesRepository
import bg.dabulgaria.tibroish.presentation.main.MainActivity
import bg.dabulgaria.tibroish.presentation.push.PushActionRouter

class ViolationSenderController(
    private val violationsRepo: IViolationRepository,
    private val violationImagesRepo: IViolationImagesRepository,
    val violationImageUploader: IViolationImageUploader,
    val tiBroishRemoteRepository: ITiBroishRemoteRepository) : IViolationSenderController {

    override fun upload(metadata: ViolationMetadata): String {
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
        return violation.serverId
    }

    override fun getIntent(context: Context, violationServerId: String): Intent {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(PushActionRouter.ACTION_TYPE_KEY, "ShowScreen")
        intent.putExtra(PushActionRouter.ACTION_VALUE_KEY, "ViolationDetails")
        intent.putExtra(PushActionRouter.ENTITY_ID_KEY, violationServerId)
        return intent
    }
}
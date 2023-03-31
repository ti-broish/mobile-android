package bg.dabulgaria.tibroish.domain.violation

import android.content.Context
import android.content.Intent
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.send.SendStatus
import bg.dabulgaria.tibroish.domain.violation.image.IViolationImageUploader
import bg.dabulgaria.tibroish.domain.violation.image.IViolationImagesRepository
import bg.dabulgaria.tibroish.presentation.main.MainActivity
import bg.dabulgaria.tibroish.presentation.push.PushActionRouter
import bg.dabulgaria.tibroish.presentation.push.PushActionType
import bg.dabulgaria.tibroish.presentation.push.PushActionValuesShowScreen
import com.google.gson.Gson
import java.lang.Exception
import java.util.logging.Logger
import javax.inject.Inject

class ViolationSenderController @Inject constructor(
        private val violationsRepo: IViolationRepository,
        private val violationImagesRepo: IViolationImagesRepository,
        private val violationImageUploader: IViolationImageUploader,
        private val tiBroishRemoteRepository: ITiBroishRemoteRepository,
        private val logger: ILogger,
        private val gson: Gson
        ) : IViolationSenderController {

    override fun upload(metadata: ViolationMetadata): Pair<String,Long>  {

        val violationId = metadata.violationId
        var violation: VoteViolation? =null

        try {
            violation = violationsRepo.get(violationId)!!
            violationImageUploader.uploadImages(violationId)

            val images = violationImagesRepo.getByViolationId(violationId)

            val request = SendViolationRequest(
                section = metadata.sectionId,
                town =metadata.townId,
                pictures =images.map { it.serverId },
                description = metadata.description,
                email = metadata.email,
                phone = metadata.phone,
                name = metadata.names
            )

            val response: VoteViolationRemote = tiBroishRemoteRepository.sendViolation(request)

            violation.remoteStatus = response.status
            violation.serverId = response.id
            violation.message = metadata.description
            violation.status = SendStatus.Send
            violation.remoteViolationJson = gson.toJson(response)

        }
        catch (ex:Exception){
            logger.e(TAG, ex)
            violation?.status = SendStatus.SendError
        }

        violation?.let { violationsRepo.update(it) }

        return Pair(violation?.serverId?: "", violation?.id ?: -1)
    }

    override fun getIntent(context: Context, serverAndDbIds: Pair<String,Long>): Intent {

        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(PushActionRouter.ACTION_TYPE_KEY, PushActionType.ShowScreen.stringValue)
        if(serverAndDbIds.first.isEmpty()){

            intent.putExtra(PushActionRouter.ACTION_VALUE_KEY, PushActionValuesShowScreen.SendViolation.stringValue)
            intent.putExtra(PushActionRouter.ENTITY_DB_ID_KEY, serverAndDbIds.second)
        }
        else{
            intent.putExtra(PushActionRouter.ACTION_VALUE_KEY, PushActionValuesShowScreen.ViolationDetails.stringValue)
            intent.putExtra(PushActionRouter.ENTITY_ID_KEY, serverAndDbIds.first)
        }

        return intent
    }

    companion object{
        val TAG = ViolationSenderController::class.java.simpleName
    }
}
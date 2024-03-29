package bg.dabulgaria.tibroish.domain.protocol

import android.content.Context
import android.content.Intent
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImageUploader
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImagesRepository
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.send.SendStatus
import bg.dabulgaria.tibroish.persistence.remote.ApiException
import bg.dabulgaria.tibroish.presentation.main.MainActivity
import bg.dabulgaria.tibroish.presentation.push.PushActionRouter
import bg.dabulgaria.tibroish.presentation.push.PushActionType
import bg.dabulgaria.tibroish.presentation.push.PushActionValuesShowScreen
import com.google.gson.Gson
import javax.inject.Inject

class ProtocolSenderController @Inject constructor(
        private val protocolImageUploader: IProtocolImageUploader,
        private val protocolImagesRepo: IProtocolImagesRepository,
        private val protocolsRepo: IProtocolsRepository,
        private val tiBroishRemoteRepository: ITiBroishRemoteRepository,
        private val logger: ILogger) : IProtocolSenderController {

    override fun upload(metadata: ProtocolMetadata): Pair<String,Long> {

        val protocolId = metadata.protocolId
        var protocol: Protocol? = null
        try {
            protocol = protocolsRepo.get(protocolId)!!
            protocolImageUploader.uploadImages(protocolId)
            val images = protocolImagesRepo.getByProtocolId(protocolId)

            val section:String? = if(metadata.sectionId.isNullOrEmpty())
                null
            else
                metadata.sectionId

            val request = SendProtocolRequest(
                section,
                images.map { it.serverId }
            )

            val response: ProtocolRemote = tiBroishRemoteRepository.sendProtocol(request)

            protocol.remoteStatus = response.status
            protocol.serverId = response.id
            protocol.status = SendStatus.Send
            protocol.remoteProtocolJson = Gson().toJson(response)
        }
        catch (apiEx: ApiException){

            logger.e(TAG, apiEx)
            if(apiEx.response.code == 400)
                protocol?.status = SendStatus.SendErrorInvalidSection
            else
                protocol?.status = SendStatus.SendError
        }
        catch (ex: Exception){
            logger.e(TAG, ex)
            protocol?.status = SendStatus.SendError
        }

        protocol?.let { protocolsRepo.update(it) }

        return Pair(protocol?.serverId ?: "", protocol?.id?:-1)
    }

    override fun getIntent(context: Context, serverAndDbIds: Pair<String,Long>): Intent {

        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(PushActionRouter.ACTION_TYPE_KEY, PushActionType.ShowScreen.stringValue)

        if(serverAndDbIds.first.isEmpty()){

            intent.putExtra(PushActionRouter.ACTION_VALUE_KEY, PushActionValuesShowScreen.SendProtocol.stringValue)
            intent.putExtra(PushActionRouter.ENTITY_DB_ID_KEY, serverAndDbIds.second)
        }
        else{
            intent.putExtra(PushActionRouter.ACTION_VALUE_KEY, PushActionValuesShowScreen.ProtocolDetails.stringValue)
            intent.putExtra(PushActionRouter.ENTITY_ID_KEY, serverAndDbIds.first)
        }

        return intent
    }

    companion object{
        val TAG = ProtocolSenderController::class.java.simpleName
    }
}
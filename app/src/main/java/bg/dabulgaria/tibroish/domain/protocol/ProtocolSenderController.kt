package bg.dabulgaria.tibroish.domain.protocol

import android.content.Context
import android.content.Intent
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImageUploader
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImagesRepository
import bg.dabulgaria.tibroish.domain.send.SendStatus
import bg.dabulgaria.tibroish.presentation.main.MainActivity
import bg.dabulgaria.tibroish.presentation.push.PushActionRouter

class ProtocolSenderController(
    val protocolImageUploader: IProtocolImageUploader,
    val protocolImagesRepo: IProtocolImagesRepository,
    val protocolsRepo: IProtocolsRepository,
    val tiBroishRemoteRepository: ITiBroishRemoteRepository) : IProtocolSenderController {

    override fun upload(metadata: ProtocolMetadata): String {
        val protocolId = metadata.protocolId
        protocolImageUploader.uploadImages(protocolId)
        val images = protocolImagesRepo.getByProtocolId(protocolId)

        val request = SendProtocolRequest(metadata.sectionId,
            images.map { it.serverId })

        val response = tiBroishRemoteRepository.sendProtocol(request)

        val protocol = protocolsRepo.get(protocolId)!!
        protocol.remoteStatus = response.status
        protocol.serverId = response.id
        protocol.status = SendStatus.Send
        protocolsRepo.update(protocol)
        return protocol.serverId
    }

    override fun getIntent(context: Context, protocolServerId: String): Intent {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(PushActionRouter.ACTION_TYPE_KEY, "ShowScreen")
        intent.putExtra(PushActionRouter.ACTION_VALUE_KEY, "ProtocolDetails")
        intent.putExtra(PushActionRouter.ENTITY_ID_KEY, protocolServerId)
        return intent
    }
}
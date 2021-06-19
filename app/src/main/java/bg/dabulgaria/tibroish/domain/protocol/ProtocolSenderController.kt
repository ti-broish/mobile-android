package bg.dabulgaria.tibroish.domain.protocol

import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImageUploader
import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImagesRepository
import bg.dabulgaria.tibroish.domain.send.SendStatus

class ProtocolSenderController(
    val protocolImageUploader: IProtocolImageUploader,
    val protocolImagesRepo: IProtocolImagesRepository,
    val protocolsRepo: IProtocolsRepository,
    val tiBroishRemoteRepository: ITiBroishRemoteRepository) : IProtocolSenderController {

    override fun upload(metadata: ProtocolMetadata) {
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
    }
}
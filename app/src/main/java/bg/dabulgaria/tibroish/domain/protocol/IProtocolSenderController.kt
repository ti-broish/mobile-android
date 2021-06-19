package bg.dabulgaria.tibroish.domain.protocol

interface IProtocolSenderController {
    fun upload(metadata: ProtocolMetadata)
}
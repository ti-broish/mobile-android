package bg.dabulgaria.tibroish.domain.protocol.image

import bg.dabulgaria.tibroish.domain.protocol.Protocol

interface IProtocolImagesRepository {

    fun getAll(): List<ProtocolImage>

    fun get(id: Long): ProtocolImage

    fun getByProtocolId(protocolId: Long): List<ProtocolImage>

    fun insert(protocol: ProtocolImage)

    fun update(protocol: ProtocolImage)

    fun delete(protocol: ProtocolImage)
}
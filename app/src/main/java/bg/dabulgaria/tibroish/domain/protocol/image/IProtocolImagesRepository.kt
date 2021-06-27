package bg.dabulgaria.tibroish.domain.protocol.image

import bg.dabulgaria.tibroish.domain.io.IBaseTransactionalRepository

interface IProtocolImagesRepository : IBaseTransactionalRepository {

    fun getAll(): List<ProtocolImage>

    fun get(id: Long): ProtocolImage?

    fun getByProtocolId(protocolId: Long): List<ProtocolImage>

    fun insert(image: ProtocolImage)

    fun update(image: ProtocolImage)

    fun delete(image: ProtocolImage)

    fun delete(protocolImageId: Long)
}
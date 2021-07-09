package bg.dabulgaria.tibroish.domain.violation.image

import bg.dabulgaria.tibroish.domain.io.IBaseTransactionalRepository

interface IViolationImagesRepository : IBaseTransactionalRepository {

    fun getAll(): List<ViolationImage>

    fun get(id: Long): ViolationImage?

    fun getByViolationId(protocolId: Long): List<ViolationImage>

    fun insert(image: ViolationImage)

    fun update(image: ViolationImage)

    fun delete(image: ViolationImage)

    fun delete(ViolationImageId: Long)
}
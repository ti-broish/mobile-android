package bg.dabulgaria.tibroish.persistence.local

import bg.dabulgaria.tibroish.domain.violation.image.IViolationImagesRepository
import bg.dabulgaria.tibroish.domain.violation.image.ViolationImage
import bg.dabulgaria.tibroish.persistence.local.db.BaseTiBroishLocalRepository
import bg.dabulgaria.tibroish.persistence.local.db.TiBroishDatabase
import javax.inject.Inject

class ViolationImagesRepository @Inject
constructor(private val database: TiBroishDatabase) : BaseTiBroishLocalRepository(database), IViolationImagesRepository {

    companion object {

        private val TAG = ViolationImagesRepository::class.simpleName
    }

    override fun getAll(): List<ViolationImage> {

        return database.daoViolationImage().getAll()
    }

    override fun getByViolationId(violationId: Long): List<ViolationImage> {

        return database.daoViolationImage().getByViolationId(violationId)
    }

    override fun get(id: Long): ViolationImage? {

        return database.daoViolationImage().getById(id)
    }

    override fun insert(image: ViolationImage) {

        val id = database.daoViolationImage().insert(image)
        image.id = id
    }

    override fun update(image: ViolationImage) {
        database.daoViolationImage().update(image)
    }

    override fun delete(image: ViolationImage) {
        database.daoViolationImage().delete(image)
    }

    override fun delete(protocolImageId: Long) {
        database.daoViolationImage().delete(protocolImageId)
    }

}
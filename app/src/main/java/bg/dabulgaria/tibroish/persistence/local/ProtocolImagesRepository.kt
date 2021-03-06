package bg.dabulgaria.tibroish.persistence.local


import bg.dabulgaria.tibroish.domain.protocol.image.IProtocolImagesRepository
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage
import bg.dabulgaria.tibroish.persistence.local.db.BaseTiBroishLocalRepository
import bg.dabulgaria.tibroish.persistence.local.db.TiBroishDatabase
import javax.inject.Inject

class ProtocolImagesRepository @Inject
constructor(private val database: TiBroishDatabase) : BaseTiBroishLocalRepository(database), IProtocolImagesRepository {

    companion object {

        private val TAG = ProtocolImagesRepository::class.simpleName
    }

    override fun getAll(): List<ProtocolImage> {

        return database.daoProtocolImage().getAll()
    }

    override fun getByProtocolId(protocolId: Long): List<ProtocolImage> {

        return database.daoProtocolImage().getByProtocolId(protocolId)
    }

    override fun get(id: Long): ProtocolImage? {

        return database.daoProtocolImage().getById(id)
    }

    override fun insert(image: ProtocolImage) {

        val id = database.daoProtocolImage().insert(image)
        image.id = id
    }

    override fun update(image: ProtocolImage) {
        database.daoProtocolImage().update(image)
    }

    override fun delete(image: ProtocolImage) {
        database.daoProtocolImage().delete(image)
    }

    override fun delete(protocolImageId: Long) {
        database.daoProtocolImage().delete(protocolImageId)
    }

}
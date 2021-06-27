package bg.dabulgaria.tibroish.persistence.local

import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.violation.IViolationRepository
import bg.dabulgaria.tibroish.domain.violation.VoteViolation
import bg.dabulgaria.tibroish.persistence.local.db.BaseTiBroishLocalRepository
import bg.dabulgaria.tibroish.persistence.local.db.TiBroishDatabase
import javax.inject.Inject

class ViolationRepository @Inject constructor(private val database: TiBroishDatabase) : IViolationRepository,
        BaseTiBroishLocalRepository(database) {

    override fun getAll(): List<VoteViolation> = database.daoViolation().getAll()

    override fun get(id: Long): VoteViolation?  = database.daoViolation().getById(id)

    override fun insert(violation: VoteViolation) {

        val id = database.daoViolation().insert( violation )
        violation.id = id
    }

    override fun update(violation: VoteViolation) {
        database.daoViolation().update(violation)
    }

    override fun delete(violation: VoteViolation) {
        database.daoViolation().delete(violation)
    }
}
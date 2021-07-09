package bg.dabulgaria.tibroish.domain.violation

import bg.dabulgaria.tibroish.domain.io.IBaseTransactionalRepository

interface IViolationRepository : IBaseTransactionalRepository {

    fun getAll(): List<VoteViolation>

    fun get(id: Long): VoteViolation?

    fun insert(voteViolation: VoteViolation)

    fun update(voteViolation: VoteViolation)

    fun delete(voteViolation: VoteViolation)
}
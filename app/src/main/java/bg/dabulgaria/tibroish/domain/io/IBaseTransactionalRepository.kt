package bg.dabulgaria.tibroish.domain.io

interface IBaseTransactionalRepository {

    fun runInTransaction(runnable: Runnable)
}
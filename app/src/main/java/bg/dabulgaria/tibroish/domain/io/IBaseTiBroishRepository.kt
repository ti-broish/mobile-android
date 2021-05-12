package bg.dabulgaria.tibroish.domain.io

interface IBaseTiBroishRepository {

    fun runInTransaction(runnable: Runnable)
}
package bg.dabulgaria.tibroish.persistence.local.db


open class BaseTiBroishLocalRepository  constructor(private val database: TiBroishDatabase)   {

    fun runInTransaction(runnable: Runnable){
        database.runInTransaction( runnable )
    }
}
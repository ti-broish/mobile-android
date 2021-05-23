package bg.dabulgaria.tibroish.persistence.local


open class BaseTiBroishLocalRepository  constructor(private val database:TiBroishDatabase )   {

    fun runInTransaction(runnable: Runnable){
        database.runInTransaction( runnable )
    }
}
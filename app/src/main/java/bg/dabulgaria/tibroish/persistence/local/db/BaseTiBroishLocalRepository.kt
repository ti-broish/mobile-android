package bg.dabulgaria.tibroish.persistence.local.db

import bg.dabulgaria.tibroish.persistence.local.db.TiBroishDatabase


open class BaseTiBroishLocalRepository  constructor(private val database: TiBroishDatabase)   {

    fun runInTransaction(runnable: Runnable){
        database.runInTransaction( runnable )
    }
}
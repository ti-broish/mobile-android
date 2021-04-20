package bg.dabulgaria.tibroish.persistence.local

import androidx.room.Database
import androidx.room.RoomDatabase
import bg.dabulgaria.tibroish.persistence.local.model.LocalComics

@Database(entities = arrayOf( LocalComics::class ), version = 1, exportSchema = false)
abstract class MarvelsDatabase: RoomDatabase() {

    abstract fun daoAccess(): DaoAccess
}
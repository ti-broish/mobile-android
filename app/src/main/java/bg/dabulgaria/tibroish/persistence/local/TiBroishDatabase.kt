package bg.dabulgaria.tibroish.persistence.local

import androidx.room.Database
import androidx.room.RoomDatabase
import bg.dabulgaria.tibroish.domain.protocol.Protocol

@Database(entities = arrayOf( Protocol::class ), version = 1, exportSchema = false)
abstract class TiBroishDatabase: RoomDatabase() {

    abstract fun daoProtocol(): DaoProtocol
}
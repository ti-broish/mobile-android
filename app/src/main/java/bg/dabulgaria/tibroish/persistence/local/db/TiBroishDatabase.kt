package bg.dabulgaria.tibroish.persistence.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage

@Database(entities = [Protocol::class, ProtocolImage::class],
        version = 2,
        exportSchema = false
)
abstract class TiBroishDatabase: RoomDatabase() {

    abstract fun daoProtocol(): DaoProtocol

    abstract fun daoProtocolImage(): DaoProtocolImage
}
package bg.dabulgaria.tibroish.persistence.local

import androidx.room.Database
import androidx.room.RoomDatabase
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage

@Database(entities = [Protocol::class, ProtocolImage::class],
        version = 1,
        exportSchema = false)
abstract class TiBroishDatabase: RoomDatabase() {

    abstract fun daoProtocol(): DaoProtocol

    abstract fun daoProtocolImage(): DaoProtocolImage
}
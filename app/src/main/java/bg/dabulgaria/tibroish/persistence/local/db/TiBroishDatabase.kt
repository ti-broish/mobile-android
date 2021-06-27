package bg.dabulgaria.tibroish.persistence.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage
import bg.dabulgaria.tibroish.domain.violation.VoteViolation
import bg.dabulgaria.tibroish.domain.violation.image.ViolationImage

@Database(entities =
[Protocol::class,
    ProtocolImage::class,
    VoteViolation::class,
    ViolationImage::class],
        version = 1,
        exportSchema = false
)
abstract class TiBroishDatabase : RoomDatabase() {

    abstract fun daoProtocol(): DaoProtocol

    abstract fun daoProtocolImage(): DaoProtocolImage

    abstract fun daoViolation(): DaoViolation

    abstract fun daoViolationImage(): DaoViolationImage
}
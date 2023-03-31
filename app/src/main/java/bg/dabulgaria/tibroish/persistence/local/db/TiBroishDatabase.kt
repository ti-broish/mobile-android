package bg.dabulgaria.tibroish.persistence.local.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage
import bg.dabulgaria.tibroish.domain.violation.VoteViolation
import bg.dabulgaria.tibroish.domain.violation.image.ViolationImage

@Database(entities =
[
    Protocol::class,
    ProtocolImage::class,
    VoteViolation::class,
    ViolationImage::class,
],
    version = 2,
    exportSchema = false,
)
abstract class TiBroishDatabase : RoomDatabase() {

    abstract fun daoProtocol(): DaoProtocol

    abstract fun daoProtocolImage(): DaoProtocolImage

    abstract fun daoViolation(): DaoViolation

    abstract fun daoViolationImage(): DaoViolationImage
}

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Protocol ADD COLUMN remoteProtocolJson TEXT")

        database.execSQL("ALTER TABLE VoteViolation ADD COLUMN names TEXT")
        database.execSQL("ALTER TABLE VoteViolation ADD COLUMN email TEXT")
        database.execSQL("ALTER TABLE VoteViolation ADD COLUMN phone TEXT")
        database.execSQL("ALTER TABLE VoteViolation ADD COLUMN remoteViolationJson TEXT")
    }
}

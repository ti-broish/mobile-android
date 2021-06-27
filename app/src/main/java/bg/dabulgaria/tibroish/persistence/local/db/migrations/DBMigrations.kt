package bg.dabulgaria.tibroish.persistence.local.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration_1_2(): Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE ProtocolImage ADD COLUMN serverUrl TEXT")
        database.execSQL("ALTER TABLE Protocol ADD COLUMN remoteStatus INT")
    }
}
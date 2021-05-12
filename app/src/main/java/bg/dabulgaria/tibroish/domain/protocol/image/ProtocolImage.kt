package bg.dabulgaria.tibroish.domain.protocol.image

import androidx.room.*
import bg.dabulgaria.tibroish.domain.image.PickedImageSource
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.protocol.ProtocolStatus
import bg.dabulgaria.tibroish.domain.protocol.ProtocolTypeConverter
import java.io.Serializable
import java.util.*

enum class UploadStatus(val code:Int){
    NotProcessed(0), Copied(1), Uploaded(2);

    companion object {

        private val lookup = hashMapOf<Int, UploadStatus>()

        init {

            for (status in EnumSet.allOf(UploadStatus::class.java))
                lookup.put(status.code, status)
        }

        operator fun get(code: Int): UploadStatus? = lookup[code]
    }
}

class UploadStatusTypeConverter {
    @TypeConverter
    fun toUploadStatus(value: Int): UploadStatus? = UploadStatus.get( value )

    @TypeConverter
    fun fromUploadStatus(value: UploadStatus) = value.code
}

class PickedImageSourceTypeConverter {
    @TypeConverter
    fun toPickedImageSource(value: Int): PickedImageSource? = PickedImageSource.get( value )

    @TypeConverter
    fun fromPickedImageSource(value: PickedImageSource) = value.code
}

class DateTypeConverter {
    @TypeConverter
    fun toDate(value: String): Date? = Date( value.toLong() )

    @TypeConverter
    fun fromDate(value: Date) = value.time.toString()
}

@Entity(tableName = "ProtocolImage",
        foreignKeys = [ForeignKey( entity = Protocol::class,
                parentColumns = ["id"],
                childColumns = ["protocolId"],
                onDelete = ForeignKey.CASCADE)])
@TypeConverters( UploadStatusTypeConverter::class,
        PickedImageSourceTypeConverter::class,
        DateTypeConverter::class)
class ProtocolImage constructor(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id:Long                     = 0,
        @ColumnInfo(name = "protocolId")
        var protocolId:Long             = 0,
        var uuid:String                 = "",
        var serverId:String             = "",
        var originalFilePath:String     = "",
        var localFilePath:String        = "",
        var localFileThumbPath:String   = "",
        var uploadStatus :UploadStatus        = UploadStatus.NotProcessed,
        var providerId:String           = "",
        val source: PickedImageSource   = PickedImageSource.None,
        val width:Int                   = -1,
        val height:Int                  = -1,
        val dateTaken: Date             = Date(0) ):Serializable
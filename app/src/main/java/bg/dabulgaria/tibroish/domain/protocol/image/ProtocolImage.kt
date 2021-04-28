package bg.dabulgaria.tibroish.domain.protocol.image

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import bg.dabulgaria.tibroish.domain.image.PickedImageSource
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import java.io.Serializable
import java.util.*

enum class UploadStatus{
    NotProcessed, Uploaded
}

@Entity(tableName = "ProtocolImage",
        foreignKeys = [ForeignKey( entity = Protocol::class,
                parentColumns = ["id"],
                childColumns = ["protocolId"],
                onDelete = ForeignKey.CASCADE)])
class ProtocolImage(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id:Long                     = -1,
        @ColumnInfo(name = "protocolId")
        var protocolId:Long             = -1,
        var uuid:String                 = "",
        var serverId:String             = "",
        var localFilePath:String        = "",
        var localFileThumbPath:String   = "",
        var uploadStatus :UploadStatus  = UploadStatus.NotProcessed,
        var providerId:String           = "",
        val source: PickedImageSource   = PickedImageSource.None,
        val imageFilePath:String        = "",
        val width:Int                   = -1,
        val height:Int                  = -1,
        val dateTaken: Date             = Date(0) ):Serializable
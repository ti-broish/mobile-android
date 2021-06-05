package bg.dabulgaria.tibroish.domain.protocol.image

import androidx.room.*
import bg.dabulgaria.tibroish.domain.date.DateTypeConverter
import bg.dabulgaria.tibroish.domain.image.PickedImageSource
import bg.dabulgaria.tibroish.domain.image.PickedImageSourceTypeConverter
import bg.dabulgaria.tibroish.domain.protocol.Protocol
import bg.dabulgaria.tibroish.domain.send.ImageSendStatus
import bg.dabulgaria.tibroish.domain.send.ImageSendStatusTypeConverter
import java.io.Serializable
import java.util.*

@Entity(tableName = "ProtocolImage",
        foreignKeys = [ForeignKey(entity = Protocol::class,
                parentColumns = ["id"],
                childColumns = ["protocolId"],
                onDelete = ForeignKey.CASCADE)])
@TypeConverters(ImageSendStatusTypeConverter::class,
        PickedImageSourceTypeConverter::class,
        DateTypeConverter::class)
class ProtocolImage constructor(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long = 0,
        @ColumnInfo(name = "protocolId", index = true)
        var protocolId: Long = 0,
        var uuid: String = "",
        var serverId: String = "",
        var originalFilePath: String = "",
        var localFilePath: String = "",
        var localFileThumbPath: String = "",
        var imageSendStatus: ImageSendStatus = ImageSendStatus.NotProcessed,
        var providerId: String = "",
        val source: PickedImageSource = PickedImageSource.None,
        val width: Int = -1,
        val height: Int = -1,
        val dateTaken: Date = Date(0),
        @ColumnInfo(name = "serverUrl")
        var serverUrl: String? = null) : Serializable
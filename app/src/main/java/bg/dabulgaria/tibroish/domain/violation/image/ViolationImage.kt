package bg.dabulgaria.tibroish.domain.violation.image

import androidx.room.*
import bg.dabulgaria.tibroish.domain.date.DateTypeConverter
import bg.dabulgaria.tibroish.domain.image.ImageBase
import bg.dabulgaria.tibroish.domain.image.PickedImageSource
import bg.dabulgaria.tibroish.domain.image.PickedImageSourceTypeConverter
import bg.dabulgaria.tibroish.domain.send.ImageSendStatus
import bg.dabulgaria.tibroish.domain.send.ImageSendStatusTypeConverter
import bg.dabulgaria.tibroish.domain.violation.VoteViolation
import java.io.Serializable
import java.util.*


@Entity(tableName = "ViolationImage",
        foreignKeys = [ForeignKey(entity = VoteViolation::class,
                parentColumns = ["id"],
                childColumns = ["violationId"],
                onDelete = ForeignKey.CASCADE)])
@TypeConverters(ImageSendStatusTypeConverter::class,
        PickedImageSourceTypeConverter::class,
        DateTypeConverter::class)
class ViolationImage constructor(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long = 0,
        @ColumnInfo(name = "violationId", index = true)
        var violationId: Long = 0,
        var uuid: String = "",
        @set:JvmName("setServerIdFake")
        var serverId: String = "",
        var originalFilePath: String = "",
        @get:JvmName("localFilePathFake")
        var localFilePath: String = "",
        var localFileThumbPath: String = "",
        @get:JvmName("getImageSendStatusFake")
        @set:JvmName("setImageSendStatusFake")
        var imageSendStatus: ImageSendStatus = ImageSendStatus.NotProcessed,
        var providerId: String = "",
        val source: PickedImageSource = PickedImageSource.None,
        val width: Int = -1,
        val height: Int = -1,
        val dateTaken: Date = Date(0),
        @ColumnInfo(name = "serverUrl")
        @set:JvmName("setServerUrlFake")
        var serverUrl: String? = null) : Serializable, ImageBase {
        override fun getImageSendStatus(): ImageSendStatus {
                return imageSendStatus
        }

        override fun setImageSendStatus(status: ImageSendStatus) {
                imageSendStatus = status
        }

        override fun getLocalFilePath(): String {
                return localFilePath
        }

        override fun setServerId(serverId: String) {
                this.serverId = serverId
        }

        override fun setServerUrl(serverUrl: String) {
                this.serverUrl = serverUrl
        }
        }
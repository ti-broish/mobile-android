package bg.dabulgaria.tibroish.domain.violation

import androidx.room.*
import bg.dabulgaria.tibroish.domain.protocol.ProtocolRemoteStatusConverter
import bg.dabulgaria.tibroish.domain.send.SendStatus
import bg.dabulgaria.tibroish.domain.send.SendStatusTypeConverter
import java.io.Serializable


@Entity(tableName = "VoteViolation")
@TypeConverters(SendStatusTypeConverter::class, ViolationRemoteStatusTypeConverter::class)
class VoteViolation constructor(@PrimaryKey(autoGenerate = true)
                                @ColumnInfo(name = "id")
                                var id: Long = 0,
                                var uuid: String = "",
                                var serverId: String = "",
                                var status: SendStatus = SendStatus.New,
                                var message: String = "",
                                var remoteStatus: ViolationRemoteStatus? = null,
                                @ColumnInfo(name = "names")
                                var names: String = "",
                                @ColumnInfo(name = "email")
                                var email: String = "",
                                @ColumnInfo(name = "phone")
                                var phone: String = "",
                                @ColumnInfo(name = "remoteViolationJson")
                                var remoteViolationJson: String? = ""
) : Serializable {
}
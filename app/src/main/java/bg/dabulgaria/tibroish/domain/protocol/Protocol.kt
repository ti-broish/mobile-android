package bg.dabulgaria.tibroish.domain.protocol

import androidx.room.*
import bg.dabulgaria.tibroish.domain.send.SendStatus
import bg.dabulgaria.tibroish.domain.send.SendStatusTypeConverter
import java.io.Serializable


class ProtocolRemoteStatusConverter {
    @TypeConverter
    fun toProtocolStatusRemote(value: String?):ProtocolStatusRemote? = if(value==null) null else ProtocolStatusRemote[value]

    @TypeConverter
    fun fromProtocolStatusRemote(value: ProtocolStatusRemote?) = value?.stringValue
}

@Entity(tableName = "Protocol")
@TypeConverters(SendStatusTypeConverter::class, ProtocolRemoteStatusConverter::class)
open class Protocol constructor(@PrimaryKey(autoGenerate = true)
                                @ColumnInfo(name = "id")
                                var id: Long = 0,
                                var uuid: String = "",
                                var serverId: String = "",
                                var status: SendStatus = SendStatus.New,
                                var remoteStatus: ProtocolStatusRemote?= null,
                                @ColumnInfo(name = "remoteProtocolJson")
                                var remoteProtocolJson: String? = null
) : Serializable {

    constructor(source: Protocol) : this(source.id,
            source.uuid,
            source.serverId,
            source.status,
            source.remoteStatus)
}
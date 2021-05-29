package bg.dabulgaria.tibroish.domain.protocol

import androidx.room.*
import bg.dabulgaria.tibroish.domain.protocol.image.ProtocolImage
import java.io.Serializable
import java.util.*

enum class ProtocolStatus constructor(val code:Int){
    New(0),
    Uploaded(1);

    companion object {

        private val lookup = hashMapOf<Int, ProtocolStatus>()

        init {

            for (status in EnumSet.allOf(ProtocolStatus::class.java))
                lookup.put(status.code, status)
        }

        operator fun get(code: Int): ProtocolStatus? = lookup[code]
    }
}

class ProtocolTypeConverter {
    @TypeConverter
    fun toProtocolStatus(value: Int):ProtocolStatus? = ProtocolStatus[value]

    @TypeConverter
    fun fromProtocolStatus(value: ProtocolStatus) = value.code
}

class ProtocolRemoteStatusConverter {
    @TypeConverter
    fun toProtocolStatusRemote(value: String?):ProtocolStatusRemote? = if(value==null) null else ProtocolStatusRemote[value]

    @TypeConverter
    fun fromProtocolStatusRemote(value: ProtocolStatusRemote?) = value?.stringValue
}

@Entity(tableName = "Protocol")
@TypeConverters(ProtocolTypeConverter::class, ProtocolRemoteStatusConverter::class)
open class Protocol constructor(@PrimaryKey(autoGenerate = true)
                                @ColumnInfo(name = "id")
                                var id: Long = 0,
                                var uuid: String = "",
                                var serverId: String = "",
                                var status: ProtocolStatus = ProtocolStatus.New,
                                var remoteStatus: ProtocolStatusRemote?= null) : Serializable {

    constructor(source: Protocol) : this(source.id,
            source.uuid,
            source.serverId,
            source.status,
    source.remoteStatus)
}

class ProtocolExt(id:Long=-1,
                  uuid:String="",
                  serverId:String ="",
                  status:ProtocolStatus = ProtocolStatus.New,
                  remoteStatus: ProtocolStatusRemote?)
    :Protocol( id, uuid, serverId, status, remoteStatus), Serializable{

    constructor(source: Protocol) : this(source.id,
            source.uuid,
            source.serverId,
            source.status,
            source.remoteStatus)

    val images = mutableListOf<ProtocolImage>()
}
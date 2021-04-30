package bg.dabulgaria.tibroish.domain.protocol

import androidx.room.*
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
    fun toProtocolStatus(value: Int):ProtocolStatus? = ProtocolStatus.Companion.get( value )

    @TypeConverter
    fun fromProtocolStatus(value: ProtocolStatus) = value.code
}

@Entity( tableName = "Protocol")
@TypeConverters( ProtocolTypeConverter::class)
class Protocol constructor(@PrimaryKey(autoGenerate = true)
                           @ColumnInfo(name = "id")
                           var id:Long=-1,
                           var uuid:String="",
                           var serverId:String ="",
                           var status:ProtocolStatus = ProtocolStatus.New) :Serializable {
}
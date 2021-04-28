package bg.dabulgaria.tibroish.domain.protocol

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

enum class ProtocolStatus constructor(val code:Int){
    New(0),
    Uploaded(1)
}

@Entity( tableName = "Protocol")
class Protocol constructor(@PrimaryKey(autoGenerate = true)
                           @ColumnInfo(name = "id")
                           var id:Long=-1,
                           var uuid:String="",
                           var serverId:String ="",
                           var status:ProtocolStatus = ProtocolStatus.New) :Serializable {
}
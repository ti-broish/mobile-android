package bg.dabulgaria.tibroish.domain.protocol

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

enum class ProtocolStatus constructor(val code:Int){
    New(0),
    Uploaded(1)
}

@Entity
class Protocol constructor() :Serializable {

    @PrimaryKey
    var id:Long=-1
    var uuid:String=""
    var sererId:String =""
    var status:ProtocolStatus = ProtocolStatus.New
}
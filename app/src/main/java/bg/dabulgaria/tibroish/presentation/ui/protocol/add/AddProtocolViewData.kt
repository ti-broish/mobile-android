package bg.dabulgaria.tibroish.presentation.ui.protocol.add

import bg.dabulgaria.tibroish.domain.protocol.ProtocolExt
import java.io.Serializable


class AddProtocolViewData( ) : Serializable {

    var protocolId :Long? = null
    var title: String?= null
    var thumbUlr: String?= null
    var description:String?= null
    var protocol:ProtocolExt?= null
}

class AddProtocolConstants{

    companion object {
        val VIEW_DATA_KEY = "AddProtocolConstants.AddProtocolViewData"
    }
}
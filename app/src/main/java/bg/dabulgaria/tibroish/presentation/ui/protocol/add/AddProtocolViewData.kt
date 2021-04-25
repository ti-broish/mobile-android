package bg.dabulgaria.tibroish.presentation.ui.protocol.add

import java.io.Serializable


class AddProtocolViewData( ) : Serializable {

    var id :Long? = null
    var title: String?= null
    var thumbUlr: String?= null
    var description:String?= null
}

class AddProtocolConstants{
    companion object {
        val VIEW_DATA_KEY = "AddProtocolConstants.AddProtocolViewData"
    }
}
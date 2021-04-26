package bg.dabulgaria.tibroish.presentation.ui.protocol.add

import java.io.Serializable


class AddProtocolViewData( ) : Serializable {

    var protocolId :Long? = null
    var title: String?= null
    var thumbUlr: String?= null
    var description:String?= null
    var photosPermissionRequested = false
    var cameraPermissionRequested = false
}

class AddProtocolConstants{
    companion object {
        val VIEW_DATA_KEY = "AddProtocolConstants.AddProtocolViewData"
    }
}
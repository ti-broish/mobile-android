package bg.dabulgaria.tibroish.presentation.ui.rights

import java.io.Serializable


class RightsAndObligationsViewData( ) : Serializable {

    var title: String?= null
    var subtitle: String?= null
    var rightsAndObligationsText:String?= null
}

class RightsAndObligationsConstants{

    companion object {
        val VIEW_DATA_KEY = "RightsAndObligationsConstants.RightsAndObligationsViewData"
    }
}
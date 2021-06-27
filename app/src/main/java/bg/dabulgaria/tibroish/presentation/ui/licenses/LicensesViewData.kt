package bg.dabulgaria.tibroish.presentation.ui.licenses

import java.io.Serializable


class LicensesViewData( ) : Serializable {
    var licensesText:String?= null
}

class LicensesConstants{

    companion object {
        val VIEW_DATA_KEY = "LicensesConstants.LicensesViewData"
    }
}
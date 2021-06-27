package bg.dabulgaria.tibroish.presentation.ui.login//package bg.dabulgaria.tibroish.presentation.ui.protocol.list

import java.io.Serializable


class LoginViewData(): Serializable {

    var emailValid = true
    var passValid = true
}

class LoginConstants{
    companion object {
        val VIEW_DATA_KEY = "LoginConstants.LoginViewData"
    }
}

package bg.dabulgaria.tibroish.presentation.navigation

import androidx.annotation.StringRes

enum class NavItemAction{
    Home,
    Profile,
    CheckIn,
    SendProtocol,
    SendSignal,
    MyProtocols,
    MySignals,
    RightsAndObligations,
    YouCountLive,
    Licenses,
    Exit,
    Login
}

data class NavItem(val action:NavItemAction,
              @StringRes val labelResId:Int,
              @StringRes val postfixRedText:Int? )
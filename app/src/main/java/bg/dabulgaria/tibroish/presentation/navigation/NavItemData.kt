package bg.dabulgaria.tibroish.presentation.navigation

import androidx.annotation.StringRes

enum class NavItemAction{
    Home,
    Profile,
    SendProtocol,
    SendSignal,
    MyProtocols,
    MySignals,
    RightsAndObligations,
    YouCountLive,
    Exit,
}

data class NavItem(val action:NavItemAction,
              @StringRes val labelResId:Int,
              @StringRes val postfixRedText:Int? )
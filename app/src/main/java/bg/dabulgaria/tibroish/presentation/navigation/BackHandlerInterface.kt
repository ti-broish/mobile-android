package bg.dabulgaria.tibroish.presentation.navigation

interface BackHandlerObject{

    fun handleBackPressed(): Boolean
}

interface BackHandlerInterface {

    fun setSelectedHandler(backHandlerObject: BackHandlerObject?, tag: String?)
}


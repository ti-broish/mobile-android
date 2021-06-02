package bg.dabulgaria.tibroish.presentation.main

import bg.dabulgaria.tibroish.presentation.navigation.OnMenuClickListener

interface IMainRouter: OnMenuClickListener {

    fun setView(view: IMainScreenView?)

    fun setPresenter(presenter: IMainPresenter?)

    fun navigateBack()

    fun onPermissionResult(permissionCode:Int, granted:Boolean)

    var permissionResponseListener: IPermissionResponseListener?

    fun showHomeScreen()

    fun showAddProtocol()

    fun openAppSettings()

    fun showPhotoPicker(protocolId:Long)

    fun showCameraPicker(protocolId:Long)

    fun showLoginScreen()

    fun onAuthEvent()

    fun showRegisterScreen(email:String)

    fun showForgotPasswordScreen(email:String)

    fun openCamera(imageFilePath: String)

    fun showProfile()

    fun showRightsAndObligations()
}

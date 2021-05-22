package bg.dabulgaria.tibroish.presentation.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.FragmentManager
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import bg.dabulgaria.tibroish.presentation.navigation.NavItemAction
import bg.dabulgaria.tibroish.presentation.ui.home.HomeFragment
import bg.dabulgaria.tibroish.presentation.ui.auth.login.LoginFragment
import bg.dabulgaria.tibroish.presentation.ui.forgotpassword.ForgotPasswordFragment
import bg.dabulgaria.tibroish.presentation.ui.photopicker.camera.CameraPickerFragment
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.PhotoPickerFragment
import bg.dabulgaria.tibroish.presentation.ui.protocol.add.AddProtocolFragment
import bg.dabulgaria.tibroish.presentation.ui.protocol.add.AddProtocolViewData
import bg.dabulgaria.tibroish.presentation.ui.registration.RegistrationFragment
import javax.inject.Inject

class MainRouter @Inject constructor(@AppContext private val appContext: Context )
    :IMainRouter{

    private var view: IMainScreenView? = null
    private var presenter: IMainPresenter? = null

    override var permissionResponseListener: IPermissionResponseListener? = null

    override fun setView(view: IMainScreenView?) {

        this.view = view
    }

    override fun setPresenter(presenter: IMainPresenter?) {
        this.presenter = presenter
    }

    override fun onAuthEvent() {
        presenter?.onAuthEvent(true)
    }

    override fun onNavigateToItem(action: NavItemAction) {

        Log.i( TAG, action.name )

        when(action){
            NavItemAction.Home -> {
                clearBackStack()
                showHomeScreen()
            }
            NavItemAction.Profile ->{}
            NavItemAction.SendProtocol -> {
                showAddProtocol()
            }
            NavItemAction.SendSignal -> {}
            NavItemAction.MyProtocols -> {}
            NavItemAction.MySignals -> {}
            NavItemAction.RightsAndObligations -> {}
            NavItemAction.YouCountLive -> {}
            NavItemAction.Exit -> {}
        }
    }
    //region IMainNavigator implementation
    override fun showHomeScreen() {

        view ?: return

        clearBackStack()

        var homeFragment = view?.supportFragmentMngr?.findFragmentByTag(HomeFragment.TAG )
        if (homeFragment == null) {

            homeFragment = HomeFragment.newInstance()
        }

        view?.showScreen(homeFragment, HomeFragment.TAG, false, false)
    }

    override fun openAppSettings() {

        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:" + appContext.packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        appContext.startActivity(intent)
    }

    override fun showAddProtocol() {

        var content = view?.supportFragmentMngr?.findFragmentByTag(AddProtocolFragment.TAG )
        if (content == null) {

            clearBackStack()
            content = AddProtocolFragment.newInstance(AddProtocolViewData())
        }

        view?.showScreen(content, AddProtocolFragment.TAG, addToBackStack = true, transitionContent = true)
    }

    override fun showPhotoPicker(protocolId:Long) {

        var content = view?.supportFragmentMngr?.findFragmentByTag(PhotoPickerFragment.TAG )
        if (content == null) {

            content = PhotoPickerFragment.newInstance(protocolId)
        }

        view?.showScreen(content, PhotoPickerFragment.TAG, addToBackStack = true, transitionContent = true)
    }

    override fun showCameraPicker(protocolId:Long) {

        var content = view?.supportFragmentMngr?.findFragmentByTag(CameraPickerFragment.TAG )
        if (content == null) {

            clearBackStack()
            content = CameraPickerFragment.newInstance(protocolId)
        }

        view?.showScreen(content, CameraPickerFragment.TAG, addToBackStack = true, transitionContent = true)
    }

    override fun navigateBack() {
        view?.navigateBack()
    }

    override fun onPermissionResult(permissionCode:Int, granted:Boolean){

        permissionResponseListener?.onPermissionResult(permissionCode, granted)
    }

    override fun showLoginScreen() {

        view ?: return

        clearBackStack()

        var content = view?.supportFragmentMngr?.findFragmentByTag(LoginFragment.TAG )
        if (content == null) {

            content = LoginFragment.newInstance()
        }

        view?.showScreen(content, LoginFragment.TAG, false, false)
    }

    override fun showRegisterScreen(email: String) {
        val content =
                view?.supportFragmentMngr?.findFragmentByTag(RegistrationFragment.TAG)
                        ?: RegistrationFragment.newInstance()
        view?.showScreen(content,
                RegistrationFragment.TAG,
                /* addToBackstack= */ true,
                /* transitionContent= */ false)
    }

    override fun showForgotPasswordScreen(email: String) {
        val content =
            view?.supportFragmentMngr?.findFragmentByTag(ForgotPasswordFragment.TAG)
                ?: ForgotPasswordFragment.newInstance()
        val arguments = Bundle()
        arguments.putString(ForgotPasswordFragment.KEY_EMAIL, email)
        content.arguments = arguments
        view?.showScreen(content,
            ForgotPasswordFragment.TAG,
            /* addToBackstack= */ true,
            /* transitionContent= */ false)
    }

    private fun clearBackStack() {

        try {
            val fragmentManager = view?.supportFragmentMngr ?: return
            //this will clear the back stack and displays no animation on the screen
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } catch (ex: Exception) {
            Log.e(TAG, "Error occurred while trying to navigate to product chooser screen.", ex)
        }
    }
    //endregion IMainNavigator implementation

    companion object {
        val TAG = MainRouter::class.java.simpleName
    }
}
package bg.dabulgaria.tibroish.presentation.main

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentManager
import bg.dabulgaria.tibroish.domain.protocol.ProtocolRemote
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import bg.dabulgaria.tibroish.infrastructure.di.annotations.AppContext
import bg.dabulgaria.tibroish.presentation.ui.common.item.send.SendItemViewData
import bg.dabulgaria.tibroish.presentation.navigation.NavItemAction
import bg.dabulgaria.tibroish.presentation.ui.home.HomeFragment
import bg.dabulgaria.tibroish.presentation.ui.auth.login.LoginFragment
import bg.dabulgaria.tibroish.presentation.ui.checkin.SendCheckInFragment
import bg.dabulgaria.tibroish.presentation.ui.forgotpassword.ForgotPasswordFragment
import bg.dabulgaria.tibroish.presentation.ui.photopicker.camera.CameraPickerFragment
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.PhotoId
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.PhotoPickerConstants
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.PhotoPickerFragment
import bg.dabulgaria.tibroish.presentation.ui.profile.ProfileFragment
import bg.dabulgaria.tibroish.presentation.ui.protocol.add.AddProtocolFragment
import bg.dabulgaria.tibroish.presentation.ui.protocol.details.ProtocolDetailsFragment
import bg.dabulgaria.tibroish.presentation.ui.protocol.list.ProtocolsFragment
import bg.dabulgaria.tibroish.presentation.ui.registration.RegistrationFragment
import bg.dabulgaria.tibroish.presentation.ui.violation.send.SendViolationFragment
import java.io.File
import bg.dabulgaria.tibroish.presentation.ui.rights.RightsAndObligationsFragment
import bg.dabulgaria.tibroish.presentation.ui.rights.RightsAndObligationsViewData
import bg.dabulgaria.tibroish.presentation.ui.violation.details.ViolationDetailsFragment
import bg.dabulgaria.tibroish.presentation.ui.violation.list.ViolationsListFragment
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
            NavItemAction.Profile ->{
                showProfile()
            }
            NavItemAction.CheckIn -> {
                showCheckIn()
            }
            NavItemAction.SendProtocol -> {
                showAddProtocol()
            }
            NavItemAction.SendSignal -> {
                showSendViolation()
            }
            NavItemAction.MyProtocols -> {
                showMyProtocols()
            }
            NavItemAction.MySignals -> {
                showViolations()
            }
            NavItemAction.RightsAndObligations -> {
                showRightsAndObligations()
            }
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
            content = AddProtocolFragment.newInstance(SendItemViewData())
        }

        view?.showScreen(content, AddProtocolFragment.TAG, addToBackStack = true, transitionContent = true)
    }

    override fun showPhotoPicker(selectedImages: List<PhotoId>) {

        var content = view?.supportFragmentMngr?.findFragmentByTag(PhotoPickerFragment.TAG)
        if (content == null) {

            content = PhotoPickerFragment.newInstance(selectedImages)
        }

        view?.showScreen(content, PhotoPickerFragment.TAG, addToBackStack = true, transitionContent = true)
    }

    override fun showCameraPicker(protocolId: Long) {

        var content = view?.supportFragmentMngr?.findFragmentByTag(CameraPickerFragment.TAG)
        if (content == null) {

            clearBackStack()
            content = CameraPickerFragment.newInstance(protocolId)
        }

        view?.showScreen(content, CameraPickerFragment.TAG, addToBackStack = true, transitionContent = true)
    }

    override fun showProfile() {
        var content = view?.supportFragmentMngr?.findFragmentByTag(ProfileFragment.TAG )
        if (content == null) {
            clearBackStack()
            content = ProfileFragment.newInstance()
        }
        view?.showScreen(
            content,
            ProfileFragment.TAG,
            addToBackStack = true,
            transitionContent = true)
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

    override fun openCamera(imageFilePath: String){

        val context = view?.appCompatActivity?:return

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val imageFile = File(imageFilePath)
        val photoURI: Uri = FileProvider.getUriForFile(
                    context, "bg.dabulgaria.tibroish.file_provider_camera", imageFile)

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        try {

            view?.appCompatActivity?.startActivityForResult(takePictureIntent,
                    PhotoPickerConstants.REQUEST_IMAGE_CAPTURE)
        }
        catch (e: ActivityNotFoundException) {
        }
    }

    override fun showSendViolation() {

        var content = view?.supportFragmentMngr?.findFragmentByTag(SendViolationFragment.TAG )
        if (content == null) {

            clearBackStack()
            content = SendViolationFragment.newInstance(SendItemViewData())
        }

        view?.showScreen(content, SendViolationFragment.TAG, addToBackStack = true, transitionContent = true)
    }

    override fun showMyProtocols() {

        var content = view?.supportFragmentMngr?.findFragmentByTag(ProtocolsFragment.TAG )
        if (content == null) {
            clearBackStack()
            content = ProtocolsFragment.newInstance()
        }

        view?.showScreen(content, ProtocolsFragment.TAG, addToBackStack = true, transitionContent = true)
    }

    override fun showProtocolDetails(protocol: ProtocolRemote) {
        var content = view?.supportFragmentMngr?.findFragmentByTag(ProtocolDetailsFragment.TAG )
        if (content == null) {
            content = ProtocolDetailsFragment.newInstance(protocol)
        }

        view?.showScreen(content, ProtocolDetailsFragment.TAG, addToBackStack = true,
            transitionContent = true)
    }

    override fun showRightsAndObligations() {

        var content = view?.supportFragmentMngr?.findFragmentByTag(RightsAndObligationsFragment.TAG )
        if (content == null) {

            clearBackStack()
            content = RightsAndObligationsFragment.newInstance(RightsAndObligationsViewData())
        }

        view?.showScreen(content, RightsAndObligationsFragment.TAG, addToBackStack = true, transitionContent = true)
    }

    override fun showViolations() {

        var content = view?.supportFragmentMngr?.findFragmentByTag(ViolationsListFragment.TAG)
        if (content == null) {

            clearBackStack()
            content = ViolationsListFragment.newInstance()
        }

        view?.showScreen(content, ViolationsListFragment.TAG, addToBackStack = true, transitionContent = true)
    }

    override fun showViolationDetails(violation: VoteViolationRemote) {
        var content = view?.supportFragmentMngr?.findFragmentByTag(ViolationDetailsFragment.TAG )
        if (content == null) {
            content = ViolationDetailsFragment.newInstance(violation)
        }

        view?.showScreen(content, ViolationDetailsFragment.TAG, addToBackStack = true,
            transitionContent = true)
    }

    private fun showCheckIn() {

        var content = view?.supportFragmentMngr?.findFragmentByTag(SendCheckInFragment.TAG )
        if (content == null) {

            clearBackStack()
            content = SendCheckInFragment.newInstance(SendItemViewData())
        }

        view?.showScreen(content, SendCheckInFragment.TAG, addToBackStack = true, transitionContent = true)
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
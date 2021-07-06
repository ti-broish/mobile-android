package bg.dabulgaria.tibroish.presentation.main

import bg.dabulgaria.tibroish.domain.locations.SectionRemote
import bg.dabulgaria.tibroish.domain.protocol.ProtocolRemote
import bg.dabulgaria.tibroish.domain.violation.VoteViolationRemote
import bg.dabulgaria.tibroish.live.model.UserStreamModel
import bg.dabulgaria.tibroish.presentation.navigation.OnMenuClickListener
import bg.dabulgaria.tibroish.presentation.ui.photopicker.gallery.PhotoId

interface IMainRouter: OnMenuClickListener {

    fun setView(view: IMainScreenView?)

    fun setPresenter(presenter: IMainPresenter?)

    fun navigateBack()

    fun onPermissionResult(permissionCode:Int, granted:Boolean)

    var permissionResponseListener: IPermissionResponseListener?

    fun showHomeScreen()

    fun showAddProtocol(dbId: Long? = null)

    fun openAppSettings()

    fun showPhotoPicker(selectedImages:List<PhotoId>)

    fun showCameraPicker(protocolId:Long)

    fun showLoginScreen(email: String? = null)

    fun onAuthEvent()

    fun showRegisterScreen(email:String)

    fun showForgotPasswordScreen(email:String)

    fun openCamera(imageFilePath: String)

    fun showProfile()

    fun showSendViolation(dbId: Long? = null)

    fun showRightsAndObligations()

    fun showMyProtocols()

    fun showProtocolDetails(protocol: ProtocolRemote)

    fun showViolations()

    fun showViolationDetails(violation: VoteViolationRemote)

    fun showDismissableDialog(message: String, dismissCallback: () -> Unit)

    fun showLicenses()

    fun showLivePickSection()

    fun showLiveStream(userStreamModel: UserStreamModel)
}

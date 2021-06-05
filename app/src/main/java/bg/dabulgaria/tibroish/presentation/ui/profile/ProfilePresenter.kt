package bg.dabulgaria.tibroish.presentation.ui.profile

import android.os.Bundle
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.organisation.ITiBroishRemoteRepository
import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.user.IUserAuthenticator
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.ui.common.FormValidator
import bg.dabulgaria.tibroish.presentation.ui.common.IOrganizationsManager
import bg.dabulgaria.tibroish.presentation.ui.profile.ProfileConstants.Companion.VIEW_DATA_KEY
import bg.dabulgaria.tibroish.presentation.ui.protocol.list.ProtocolsConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

interface IProfilePresenter : IBasePresenter<IProfileView> {

    fun fetchUserDetails()

    fun send(user: User, callback: IUpdateProfileCallback)

    fun getOrganizationWithName(organizationName: String): Organization?

    fun getOrganizations(callback: (List<Organization>?) -> Unit)

    fun processRequiredField(text: String, callback: (Int) -> Unit): Boolean

    fun processPhoneNumberField(
        telephone: String,
        callback: (Int) -> Unit
    ): Boolean

    fun processEgnLastFourDigits(egn: String, callback: (Int) -> Unit): Boolean

    fun processOrganization(organizationName: String, callback: (Int) -> Unit): Boolean

    fun createUserDetailsCopy(): User?

    fun deleteUser(callback: IDeleteUserCallback)

    fun navigateToLoginScreen()
}

interface IUpdateProfileCallback {
    fun onSuccess()
}

interface IDeleteUserCallback {
    fun onSuccess()
}

class ProfilePresenter @Inject constructor(
    private val mainRouter: IMainRouter,
    disposableHandler: IDisposableHandler,
    private val logger: ILogger,
    private val tiBroishRemoteRepository: ITiBroishRemoteRepository,
    private val formValidator: FormValidator,
    private val organizationsManager: IOrganizationsManager,
    private val userAuthenticator: IUserAuthenticator
) : BasePresenter<IProfileView>(disposableHandler),
    IProfilePresenter {

    private var viewData: ProfileViewData? = null

    companion object {
        @JvmField
        val TAG: String = ProfilePresenter::class.java.name
    }

    override fun fetchUserDetails() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userDetails = createFakeUser()//TODO: put this back when APIs work
                // tiBroishRemoteRepository.getUserDetails()
                withContext(Dispatchers.Main) {
                    viewData?.userDetails = userDetails
                    view?.onProfileFetchSuccess(userDetails)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    logger.e(TAG, e)
                    view?.onProfileFetchFail(R.string.profile_generic_error)
                }
            }
        }
    }

    private fun createFakeUser(): User {
        val user = User()
        user.firstName = "Bruce"
        user.lastName = "Wayne"
        user.email = "bruce.wayne@asd.com"
        user.phone = "+359333234234"
        user.pin = "1234"
        user.organization =
            Organization(BigDecimal(123), "Демократична България", Organization.Type.party)
        user.hasAgreedToKeepData = true
        return user
    }

    override fun send(user: User, callback: IUpdateProfileCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                tiBroishRemoteRepository.updateUserDetails(user)
                withContext(Dispatchers.Main) {
                    callback.onSuccess()
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    onError(exception)
                }
            }
        }
    }

    override fun getOrganizationWithName(organizationName: String): Organization? {
        return organizationsManager.getOrganizationWithName(
            organizationName, viewData?.organizationsData
        )
    }

    override fun getOrganizations(callback: (List<Organization>?) -> Unit) {
        if (viewData?.organizationsData!!.isNullOrEmpty()) {
            organizationsManager.loadOrganizationsAsync {
                callback(it)
                viewData?.organizationsData?.clear()
                viewData?.organizationsData?.addAll(it!!)
            }
            return
        }
        return callback(viewData?.organizationsData)
    }

    override fun processRequiredField(text: String, callback: (Int) -> Unit): Boolean {
        return formValidator.processRequiredField(text, callback)
    }

    override fun processPhoneNumberField(telephone: String, callback: (Int) -> Unit): Boolean {
        return formValidator.processPhoneNumberField(telephone, callback)
    }

    override fun processEgnLastFourDigits(egn: String, callback: (Int) -> Unit): Boolean {
       return formValidator.processEgnLastFourDigits(egn, callback)
    }

    override fun processOrganization(organizationName: String, callback: (Int) -> Unit): Boolean {
        return formValidator.processOrganization(
            organizationName,
            viewData?.organizationsData,
            callback
        )
    }

    override fun createUserDetailsCopy(): User? {
        return viewData?.userDetails?.copy()
    }

    override fun deleteUser(callback: IDeleteUserCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                tiBroishRemoteRepository.deleteUser()
                userAuthenticator.logout()
                withContext(Dispatchers.Main) {
                    callback.onSuccess()
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    onError(exception)
                }
            }
        }
    }

    override fun navigateToLoginScreen() {
        mainRouter.showLoginScreen()
    }

    override fun onRestoreData(bundle: Bundle?) {
        viewData = bundle?.getParcelable(
            VIEW_DATA_KEY
        )
            ?: ProfileViewData(
                /* organizationsData= */ mutableListOf(),
                /* userDetails= */ null
            )
    }

    override fun onSaveData(outState: Bundle) {
        outState.putParcelable(VIEW_DATA_KEY, viewData)
    }

    override fun loadData() {
        if (viewData == null) {
            viewData = ProfileViewData(
                /* organizationsData= */ mutableListOf(),
                /* userDetails= */ null
            )
        }
    }
}

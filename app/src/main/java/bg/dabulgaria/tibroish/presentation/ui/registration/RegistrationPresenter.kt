package bg.dabulgaria.tibroish.presentation.ui.registration

import android.content.Context
import android.os.Bundle
import android.widget.CheckBox
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.organisation.ITiBorishRemoteRepository
import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.domain.organisation.OrganizationDto
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.ui.common.FormValidator
import bg.dabulgaria.tibroish.presentation.ui.common.IOrganizationsManager
import bg.dabulgaria.tibroish.presentation.ui.common.UserDataWrapper
import bg.dabulgaria.tibroish.utils.AssetReader
import com.google.firebase.auth.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IRegistrationPresenter : IBasePresenter<IRegisterView> {
    fun getCountryCodes(context: Context, callback: (List<CountryCode>?) -> Unit)

    fun getOrganizations(callback: (List<Organization>?) -> Unit)

    fun processRequiredField(text: String, callback: (Int) -> Unit): Boolean

    fun processEmailField(
        email: String,
        callback: (Int) -> Unit
    ): Boolean

    fun processPhoneNumberField(
        telephone: String,
        callback: (Int) -> Unit
    ): Boolean

    fun processEgnLastFourDigits(egn: String, callback: (Int) -> Unit): Boolean

    fun processRepeatPassword(
        password: String,
        repeatPassword: String,
        callback: (Int) -> Unit
    ): Boolean

    fun processPassword(password: String, callback: (Int) -> Unit): Boolean

    fun processOrganization(organizationName: String, callback: (Int) -> Unit): Boolean

    fun processRequiredCheckbox(
        checkBox: CheckBox, callback: (Int) -> Unit, errorCode: Int
    ): Boolean

    fun organizationToOrganizationDto(organization: Organization): OrganizationDto

    fun register(userData: UserDataWrapper, callback: IRegistrationCallback)

    fun navigateToLoginScreen()

    fun getOrganizationWithName(organizationName: String): Organization?
}

interface IRegistrationCallback {
    fun onSuccess()
}

class RegistrationPresenter @Inject constructor(
    private val mainRouter: IMainRouter,
    disposableHandler: IDisposableHandler,
    private val logger: ILogger,
    private val tiBroishRemoteRepository: ITiBorishRemoteRepository,
    private val formValidator: FormValidator,
    private val organizationsManager: IOrganizationsManager
) :
    BasePresenter<IRegisterView>(disposableHandler), IRegistrationPresenter {
    private var registrationViewData: RegistrationViewData? = null

    companion object {
        @JvmField
        val TAG: String = RegistrationPresenter::class.java.name
    }

    override fun getCountryCodes(context: Context, callback: (List<CountryCode>?) -> Unit) {
        if (registrationViewData?.countryCodesData.isNullOrEmpty()) {
            loadCountryCodesAsync(context, callback)
            return
        }
        callback.invoke(registrationViewData?.countryCodesData)
    }

    override fun getOrganizations(callback: (List<Organization>?) -> Unit) {
        if (registrationViewData?.organizationsData!!.isNullOrEmpty()) {
            organizationsManager.loadOrganizationsAsync {
                callback(it)
                registrationViewData?.organizationsData?.clear()
                registrationViewData?.organizationsData?.addAll(it!!)
            }
            return
        }
        return callback(registrationViewData?.organizationsData)
    }

    override fun organizationToOrganizationDto(organization: Organization) =
        OrganizationDto(organization.id, organization.name, organization.type.value)

    private fun loadCountryCodesAsync(context: Context, callback: (List<CountryCode>?) -> Unit) {
        val json: String? = AssetReader().loadJsonFromAsset(
            "phone-codes.json",
            context.applicationContext.assets
        )
        val fromJson = Gson().fromJson(json, Countries::class.java)
        val countries = fromJson.countries
        callback.invoke(countries)
        registrationViewData?.countryCodesData?.clear()
        if (countries != null) {
            registrationViewData?.countryCodesData?.addAll(countries)
        }
    }

    override fun register(userData: UserDataWrapper, callback: IRegistrationCallback) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(userData.email, userData.password)
            .addOnSuccessListener {
                logger.i(TAG, "createUserWithEmailAndPassword success ")
                onFirebaseUserCreated(auth.currentUser!!, userData, callback)
            }.addOnFailureListener {
                logger.e(TAG, "createUserWithEmailAndPassword failure", it)
                if (it is FirebaseAuthUserCollisionException) {
                    signin(auth, userData, callback)
                } else {
                    onError(it)
                }
            }
    }

    private fun signin(
        auth: FirebaseAuth,
        userData: UserDataWrapper,
        callback: IRegistrationCallback
    ) {
        auth.signInWithEmailAndPassword(userData.email, userData.password)
            .addOnSuccessListener {
                onFirebaseUserCreated(auth.currentUser!!, userData, callback)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    override fun onError(throwable: Throwable?) {
        if (throwable is FirebaseAuthWeakPasswordException) {
            view?.onError(resourceProvider.getString(R.string.error_weak_password))
        } else if (throwable is FirebaseAuthUserCollisionException) {
            view?.onError(resourceProvider.getString(R.string.error_user_exists))
        }
        if (throwable is FirebaseAuthInvalidCredentialsException) {
            view?.onError(resourceProvider.getString(R.string.invalid_mail_or_pass));
        } else {
            super.onError(throwable)
        }
    }

    override fun navigateToLoginScreen() {
        mainRouter.showLoginScreen()
    }

    override fun getOrganizationWithName(organizationName: String): Organization? {
        return organizationsManager.getOrganizationWithName(
            organizationName, registrationViewData?.organizationsData
        );
    }

    private fun onFirebaseUserCreated(
        user: FirebaseUser,
        userDataWrapper: UserDataWrapper,
        callback: IRegistrationCallback
    ) {
        user.getIdToken(/* forceRefresh= */ false)
            .addOnSuccessListener {
                val userToken: String = it.token!!
                val userData = userDataWrapperToUserData(user.uid, userDataWrapper)
                createApiUser(user, userToken, userData, callback)
            }
            .addOnFailureListener {
                onError(it)
            }
    }

    private fun createApiUser(
        user: FirebaseUser,
        userToken: String,
        userData: User,
        callback: IRegistrationCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                tiBroishRemoteRepository.createUser(userToken, userData)
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    onError(exception)
                }
            }
            withContext(Dispatchers.Main) {
                sendEmailVerification(user, callback)
            }
        }
    }

    private fun sendEmailVerification(
        user: FirebaseUser,
        callback: IRegistrationCallback
    ) {
        user.sendEmailVerification()
            .addOnSuccessListener {
                logger.d(TAG, "send email verification success")
                callback.onSuccess()
            }.addOnFailureListener {
                logger.e(
                    TAG, "send email verification error",
                    it
                )
                onError(it)
            }
    }

    private fun userDataWrapperToUserData(
        uid: String,
        userData: UserDataWrapper
    ): User {
        val user = User()
        user.firebaseUid = uid
        user.firstName = userData.firstName
        user.lastName = userData.lastName
        user.email = userData.email
        user.pin = userData.pin
        user.phone = userData.phone
        user.organization = userData.organization
        user.hasAgreedToKeepData = userData.hasAgreedToKeepData
        return user
    }

    override fun processRequiredField(text: String, callback: (Int) -> Unit): Boolean {
        return formValidator.processRequiredField(text, callback)
    }

    override fun processEmailField(email: String, callback: (Int) -> Unit): Boolean {
        return formValidator.processEmailField(email, callback)
    }

    override fun processPhoneNumberField(telephone: String, callback: (Int) -> Unit): Boolean {
        return formValidator.processPhoneNumberField(telephone, callback)
    }

    override fun processEgnLastFourDigits(egn: String, callback: (Int) -> Unit): Boolean {
        return formValidator.processEgnLastFourDigits(egn, callback)
    }

    override fun processRepeatPassword(
        password: String,
        repeatPassword: String,
        callback: (Int) -> Unit
    ): Boolean {
        if (!processRequiredField(repeatPassword, callback)) {
            return false
        }
        if (!formValidator.processConditionalField(
                password,
                predicate = { password == repeatPassword },
                errorCallback = callback,
                errorResponse = R.string.passwords_do_not_match
            )
        ) {
            return false
        }
        return true
    }

    override fun processPassword(password: String, callback: (Int) -> Unit): Boolean {
        if (!processRequiredField(password, callback)) {
            return false
        }
        if (!formValidator.processConditionalField(
                password,
                predicate = { it.length >= 6 },
                errorCallback = callback,
                errorResponse = R.string.invalid_password_too_short
            )
        ) {
            return false
        }
        return true
    }

    override fun processOrganization(organizationName: String, callback: (Int) -> Unit): Boolean {
        return formValidator.processOrganization(
            organizationName,
            registrationViewData?.organizationsData,
            callback
        )
    }

    override fun processRequiredCheckbox(
        checkBox: CheckBox, callback: (Int) -> Unit, errorCode: Int
    ): Boolean {
        if (!checkBox.isChecked) {
            callback(errorCode)
            return false
        }
        return true
    }

    override fun onRestoreData(bundle: Bundle?) {
        registrationViewData = bundle?.getParcelable(RegistrationConstants.VIEW_DATA_KEY)
            ?: RegistrationViewData(
                /* countryCodesData= */ mutableListOf(),
                /* organizationsData= */mutableListOf()
            )
    }

    override fun onSaveData(outState: Bundle) {
        outState.putParcelable(RegistrationConstants.VIEW_DATA_KEY, registrationViewData)
    }

    override fun loadData() {
        if (registrationViewData == null) {
            registrationViewData = RegistrationViewData(
                /* countryCodesData= */ mutableListOf(),
                /* organizationsData= */mutableListOf()
            )
        }
    }
}
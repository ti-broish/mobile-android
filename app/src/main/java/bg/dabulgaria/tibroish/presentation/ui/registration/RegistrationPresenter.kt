package bg.dabulgaria.tibroish.presentation.ui.registration

import android.content.Context
import android.os.Bundle
import android.widget.CheckBox
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.organisation.ITiBorishRemoteRepository
import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.ui.common.FormValidator
import bg.dabulgaria.tibroish.utils.AssetReader
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IRegistrationPresenter : IBasePresenter<IRegisterView> {
    fun getCountryCodes(context: Context, callback: (List<CountryCode>?) -> Unit)

    fun getOrganizations(callback: (List<RegistrationOrganization>?) -> Unit)

    fun register(email: String, password: String)

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

    fun processOrganization(organization: String, callback: (Int) -> Unit): Boolean

    fun processRequiredCheckbox(
        checkBox: CheckBox, callback: (Int) -> Unit, errorCode: Int
    ): Boolean
}

class RegistrationPresenter @Inject constructor(
    private val mainRouter: IMainRouter,
    disposableHandler: IDisposableHandler
) :
    BasePresenter<IRegisterView>(disposableHandler), IRegistrationPresenter {
    private var registrationViewData: RegistrationViewData? = null
    private var formValidator: FormValidator = FormValidator()

    @Inject
    lateinit var tiBorishRemoteRepository: ITiBorishRemoteRepository

    override fun getCountryCodes(context: Context, callback: (List<CountryCode>?) -> Unit) {
        if (registrationViewData?.countryCodesData.isNullOrEmpty()) {
            loadCountryCodesAsync(context, callback)
            return
        }
        callback.invoke(registrationViewData?.countryCodesData)
    }

    override fun getOrganizations(callback: (List<RegistrationOrganization>?) -> Unit) {
        if (registrationViewData?.organizationsData!!.isNullOrEmpty()) {
            loadOrganizationsAsync(callback);
            return
        }
        return callback(registrationViewData?.organizationsData)
    }

    private fun loadOrganizationsAsync(callback: (List<RegistrationOrganization>?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val organizations: List<RegistrationOrganization> =
                fetchOrganizations()
                    .map { organization ->
                        RegistrationOrganization(organization.name, organization.id)
                    }
                    .toList()
            callback(organizations)
            registrationViewData?.organizationsData?.clear()
            registrationViewData?.organizationsData?.addAll(organizations)
        }
    }

    private suspend fun fetchOrganizations(): List<Organization> {
        return withContext(Dispatchers.IO) {
            tiBorishRemoteRepository.getOrganisations()
        }
    }

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

    override fun register(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun processRequiredField(text: String, callback: (Int) -> Unit): Boolean {
        return formValidator.processRequiredField(text, callback)
    }

    override fun processEmailField(
        email: String,
        callback: (Int) -> Unit
    ): Boolean {
        return formValidator.processEmailField(email, callback)
    }

    override fun processPhoneNumberField(
        telephone: String,
        callback: (Int) -> Unit
    ): Boolean {
        if (!processRequiredField(telephone, callback)) {
            return false
        }
        return formValidator.processPhoneNumberField(telephone, callback)
    }

    override fun processEgnLastFourDigits(egn: String, callback: (Int) -> Unit): Boolean {
        if (!processRequiredField(egn, callback)) {
            return false
        }
        if (!formValidator.processConditionalField(
                egn,
                predicate = {
                    it.length == 4
                },
                callback = callback,
                errorResponse = R.string.egn_four_digits_required
            )
        ) {
            return false
        }
        return true
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
                callback = callback,
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
                callback = callback,
                errorResponse = R.string.invalid_password_too_short
            )
        ) {
            return false
        }
        return true
    }

    override fun processOrganization(organization: String, callback: (Int) -> Unit): Boolean {
        if (registrationViewData?.organizationsData.isNullOrEmpty()) {
            callback(R.string.invalid_organization)
            return false
        }
        if (!processRequiredField(organization, callback)) {
            return false
        }
        if (!formValidator.processConditionalField(
                organization,
                predicate = {
                    registrationViewData?.organizationsData?.filter {
                        it.name == organization
                    }?.any() == true
                },
                callback = callback,
                errorResponse = R.string.invalid_organization
            )
        ) {
            return false
        }
        return true
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
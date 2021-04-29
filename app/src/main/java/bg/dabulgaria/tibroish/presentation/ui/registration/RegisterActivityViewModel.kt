package bg.dabulgaria.tibroish.presentation.ui.registration

import android.app.Application

import android.widget.CheckBox
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.ui.common.FormValidator
import bg.dabulgaria.tibroish.utils.AssetReader
import com.google.gson.Gson

class RegisterActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var countryCodesData: MutableLiveData<List<CountryCode>> = MutableLiveData()
    private var organizationsData: MutableLiveData<List<Organization>> = MutableLiveData()
    private var formValidator: FormValidator = FormValidator()

    fun getCountryCodes(): MutableLiveData<List<CountryCode>> {
        if (countryCodesData.value == null) {
            loadCountryCodesAsync()
        }
        return countryCodesData
    }

    fun getOrganizations(): MutableLiveData<List<Organization>> {
        if (organizationsData.value == null) {
            loadOrganizationsAsync();
        }
        return organizationsData
    }

    private fun loadOrganizationsAsync() {
        // TODO: Change to a proper API call
        val organizations = ArrayList(listOf(
                Organization("Organization 1", "123"),
                Organization("Hello 2", "124"),
                Organization("Test 3", "125")))
        organizationsData.value = organizations
    }

    private fun loadCountryCodesAsync() {
        val json: String? = AssetReader().loadJsonFromAsset(
                "phone-codes.json",
                getApplication<Application>().assets)
        val fromJson = Gson().fromJson(json, Countries::class.java)
        val countryCodes = fromJson.countries
        countryCodesData.value = countryCodes
    }

    fun register(email: String, password: String) {
        TODO("Not yet implemented")
    }

    fun processRequiredField(text: String, callback: (Int) -> Unit): Boolean {
        return formValidator.processRequiredField(text, callback)
    }

    fun processEmailField(
            email: String,
            callback: (Int) -> Unit): Boolean {
        return formValidator.processEmailField(email, callback)
    }

    fun processPhoneNumberField(
            telephone: String,
            callback: (Int) -> Unit): Boolean {
        if (!processRequiredField(telephone, callback)) {
            return false
        }
        return formValidator.processPhoneNumberField(telephone, callback)
    }

    fun processEgnLastFourDigits(egn: String, callback: (Int) -> Unit): Boolean {
        if (!processRequiredField(egn, callback)) {
            return false
        }
        if (!formValidator.processConditionalField(
                        egn,
                        predicate = {
                            it.length == 4
                        },
                        callback = callback,
                        errorResponse = R.string.egn_four_digits_required)) {
            return false
        }
        return true
    }

    fun processRepeatPassword(password: String, repeatPassword: String, callback: (Int) -> Unit): Boolean {
        if (!processRequiredField(repeatPassword, callback)) {
            return false
        }
        if (!formValidator.processConditionalField(
                        password,
                        predicate = { password == repeatPassword },
                        callback = callback,
                        errorResponse = R.string.passwords_do_not_match)) {
            return false
        }
        return true
    }

    fun processPassword(password: String, callback: (Int) -> Unit): Boolean {
        if (!processRequiredField(password, callback)) {
            return false
        }
        if (!formValidator.processConditionalField(
                        password,
                        predicate = { it.length >= 6 },
                        callback = callback,
                        errorResponse = R.string.invalid_password_too_short)) {
            return false
        }
        return true
    }

    fun processOrganization(organization: String, callback: (Int) -> Unit): Boolean {
        if (getOrganizations().value.isNullOrEmpty()) {
            callback(R.string.invalid_organization)
        }
        if (!processRequiredField(organization, callback)) {
            return false
        }
        if (!formValidator.processConditionalField(
                        organization,
                        predicate = {
                            getOrganizations().value?.filter {
                                it.name == organization }?.any() == true
                        },
                        callback = callback,
                        errorResponse = R.string.invalid_organization)) {
            return false
        }
        return true
    }

    fun processRequiredCheckbox(
            checkBox: CheckBox, callback: (Int) -> Unit, errorCode: Int): Boolean {
        if (!checkBox.isChecked) {
            callback(errorCode)
            return false
        }
        return true
    }
}
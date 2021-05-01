package bg.dabulgaria.tibroish.presentation.ui.registration

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import bg.dabulgaria.tibroish.utils.AssetReader
import com.google.gson.Gson

class RegisterActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var countryCodesData: MutableLiveData<List<CountryCode>> = MutableLiveData()
    private var organizationsData: MutableLiveData<List<Organization>> = MutableLiveData()
    private val emailValidState: MutableLiveData<Boolean> = MutableLiveData(true);
    private val passwordValidState: MutableLiveData<Boolean> = MutableLiveData(true);

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

    fun getEmailValidState(): MutableLiveData<Boolean> {
        return emailValidState
    }

    fun getPasswordValidState(): MutableLiveData<Boolean> {
        return passwordValidState
    }

    fun processEmail(email: String) : Boolean {
        val isEmailValid = isEmailValid(email)
        emailValidState.value = isEmailValid;
        return isEmailValid
    }

    fun processPassword(password: String) : Boolean {
        val isPasswordValid = isPasswordValid(password)
        passwordValidState.value = isPasswordValid
        return isPasswordValid
    }

    private fun loadOrganizationsAsync() {
        // TODO: Change to a proper API call
        val organizations = ArrayList(listOf(
                Organization("Да България", "123"),
                Organization("Има такъв народ", "124"),
                Organization("Орг 3", "125")))
        organizationsData.value = organizations
    }

    private fun loadCountryCodesAsync() {
        val json:String? = AssetReader().loadJsonFromAsset(
                "phone-codes.json",
                getApplication<Application>().assets)
        val fromJson = Gson().fromJson(json, Countries::class.java)
        val countryCodes = fromJson.countries
        countryCodesData.value = countryCodes
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty()
    }

    private fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun register(email: String, password: String) {
        TODO("Not yet implemented")
    }
}
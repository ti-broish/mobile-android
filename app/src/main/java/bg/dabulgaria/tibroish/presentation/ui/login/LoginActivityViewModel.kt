package bg.dabulgaria.tibroish.presentation.ui.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginActivityViewModel : ViewModel() {

    private val emailValidState: MutableLiveData<Boolean> = MutableLiveData(true);
    private val passwordValidState: MutableLiveData<Boolean> = MutableLiveData(true);

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

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty()
    }

    private fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun login(email: String, password: String) {
        TODO("Not yet implemented")
    }
}
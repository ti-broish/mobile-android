package bg.dabulgaria.tibroish.presentation.ui.auth.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import com.google.firebase.auth.FirebaseAuth

class LoginFragmentViewModel : ViewModel() {

    private val emailValidState: MutableLiveData<Boolean> = MutableLiveData(true)
    private val passwordValidState: MutableLiveData<Boolean> = MutableLiveData(true)

    var auth: FirebaseAuth? = null
    var mMainRouter: IMainRouter? = null

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

        auth?.signInWithEmailAndPassword(email, password)
                ?.addOnFailureListener { throwable->

                    Log.e( TAG, throwable.message, throwable)
                }
                ?.addOnSuccessListener { authResult->

                    if( authResult.user.isEmailVerified) {

                        mMainRouter?.onAuthEvent()
                    }
                }
                ?.addOnCanceledListener {
                    Log.d(TAG, "Login Canceled")
                }
    }

    companion object{
        val TAG = LoginFragmentViewModel::class.java.simpleName
    }
}
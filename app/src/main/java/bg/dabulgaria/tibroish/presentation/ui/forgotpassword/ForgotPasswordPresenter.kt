package bg.dabulgaria.tibroish.presentation.ui.forgotpassword

import android.os.Bundle
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import bg.dabulgaria.tibroish.presentation.ui.common.FormValidator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import javax.inject.Inject

interface IForgotPasswordPresenter : IBasePresenter<IForgotPasswordView> {
    fun processEmailField(email: String, callback: (Int) -> Unit): Boolean

    fun sendPasswordResetEmail(email: String)

    fun showLoginScreen(email: String)
}

class ForgotPasswordPresenter @Inject constructor(
    private val mainRouter: IMainRouter,
    disposableHandler: IDisposableHandler,
    private val formValidator: FormValidator
) : BasePresenter<IForgotPasswordView>(disposableHandler),
    IForgotPasswordPresenter {

    override fun processEmailField(
        email: String,
        callback: (Int) -> Unit
    ): Boolean {
        return formValidator.processEmailField(email, callback)
    }

    override fun sendPasswordResetEmail(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnSuccessListener { view?.passwordResetSuccess(email) }
            .addOnFailureListener {
                val errorResId: Int = if (it is FirebaseAuthInvalidUserException) {
                    R.string.password_reset_invalid_user
                } else {
                    R.string.password_reset_generic_error_message
                }
                view?.passwordResetFail(errorResId)
            }
    }

    override fun showLoginScreen(email: String) {
        mainRouter.showLoginScreen(email)
    }

    override fun onRestoreData(bundle: Bundle?) {}

    override fun onSaveData(outState: Bundle) {}

    override fun loadData() {}
}

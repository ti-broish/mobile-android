package bg.dabulgaria.tibroish.presentation.ui.auth.login//package bg.dabulgaria.tibroish.presentation.ui.protocol.list


import android.os.Bundle
import android.util.Patterns
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.providers.ILogger
import bg.dabulgaria.tibroish.domain.user.IAuthRepository
import bg.dabulgaria.tibroish.domain.user.IUserLocalRepository
import bg.dabulgaria.tibroish.domain.user.IUserTypeAdapter
import bg.dabulgaria.tibroish.presentation.base.BasePresenter
import bg.dabulgaria.tibroish.presentation.base.IBasePresenter
import bg.dabulgaria.tibroish.presentation.base.IDisposableHandler
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import javax.inject.Inject

interface ILoginPresenter : IBasePresenter<ILoginView> {

    fun onRegisterButtonClicked(email:String,)

    fun onForgotPasswordButtonClicked(email:String,)

    fun onLoginButtonClicked(email:String, password:String)
}

class LoginPresenter @Inject constructor(private val userLocalRepo : IUserLocalRepository,
                                         private val userTypeAdapter : IUserTypeAdapter,
                                         private val authRepo : IAuthRepository,
                                         private val logger: ILogger,
                                         private val mainRouter: IMainRouter,
                                         dispHandler: IDisposableHandler,)
    : BasePresenter<ILoginView>(dispHandler), ILoginPresenter {

    var data :LoginViewData? = null

    override fun onRestoreData(bundle: Bundle?) {
        bundle?.let {
            data = (bundle.getSerializable(LoginConstants.VIEW_DATA_KEY) as LoginViewData?)
                    ?:LoginViewData()
        }
    }

    override fun onSaveData(outState: Bundle) {
        outState.putSerializable(LoginConstants.VIEW_DATA_KEY, data)
    }

    override fun loadData() {

        if( data == null )
            data = LoginViewData()

        data?.emailValid = true
        data?.passValid = true
    }

    override fun onForgotPasswordButtonClicked(email: String)
            = mainRouter.showForgotPasswordScreen(email)

    override fun onRegisterButtonClicked(email: String)
            = mainRouter.showRegisterScreen(email)

    override fun onLoginButtonClicked(email: String, password: String) {

        val viewData = data?: return

        viewData.emailValid = isEmailValid(email)
        viewData.passValid = isPasswordValid(password)

        view?.onDataLoaded(viewData)

        if(!viewData.emailValid || !viewData.passValid)
            return

        login(email, password)
    }

    fun login(email: String, password: String) {

        view?.onLoading(true)

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnFailureListener { throwable->

                    logger.e(LoginFragmentViewModel.TAG, throwable)
                    view?.onLoading(false)
                    onError(throwable)
                }
                .addOnSuccessListener { authResult->

                    onSingInSuccess(authResult)
                }
                .addOnCanceledListener {

                    view?.onLoading(false)

                    logger.d(LoginFragmentViewModel.TAG, "Login Canceled")
                }
    }

    private fun onSingInSuccess(authResult: AuthResult){

        val firebaseUser = authResult.user?: run {
            this.onError(null)
            view?.onLoading(false)
            return
        }

        if(firebaseUser.isEmailVerified) {

            val user = userTypeAdapter.toUser(firebaseUser)

            firebaseUser.getIdToken(false)
                    .addOnFailureListener { throwable->

                        logger.e(LoginFragmentViewModel.TAG, throwable)
                        view?.onLoading(false)
                        onError(throwable)
                    }
                    .addOnSuccessListener { getTokenResult ->

                        authRepo.token = getTokenResult.token?:""
                        userLocalRepo.user = user
                        mainRouter.onAuthEvent()
                    }
        }
        else{

            firebaseUser.sendEmailVerification()
                    .addOnFailureListener {
                        onError(it)
                    }
            view?.onLoading(false)
            view?.onError( resourceProvider.getString(R.string.to_continue_confirm_email))
        }
    }

    override fun onError(throwable:Throwable?){

//        if( throwable is FirebaseAuthWeakPasswordException) {
//            mTxtPassword.setError(getString(R.string.error_weak_password));
//            mTxtPassword.requestFocus();
//        }
        //        else if( throwable is FirebaseAuthUserCollisionException) {
//            view?.onError(resourceProvider.getString(R.string.error_user_exists));
//            mTxtEmail.requestFocus()
//        }
        if( throwable is FirebaseAuthInvalidCredentialsException) {
            view?.onError(resourceProvider.getString(R.string.invalid_mail_or_pass));
        }
        else {
            super.onError(throwable)
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty()
    }

    private fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    companion object {

        private val TAG = LoginPresenter::class.simpleName
    }
}

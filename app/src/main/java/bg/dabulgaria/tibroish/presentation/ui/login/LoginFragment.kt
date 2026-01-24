package bg.dabulgaria.tibroish.presentation.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.databinding.FragmentUserLoginBinding

interface ILoginView :IBaseView{

    fun onDataLoaded(data: LoginViewData)

    fun onLoading(loading:Boolean)
}

class LoginFragment : BasePresentableFragment<ILoginView, ILoginPresenter>(), ILoginView {

    private var _loginBinding: FragmentUserLoginBinding? = null
    private val loginBinding get() = _loginBinding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _loginBinding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.fragment_user_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _loginBinding = FragmentUserLoginBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupOnClickListeners()
    }

    private fun prefillEmail(email: String?) {
        if (email.isNullOrEmpty()) {
            loginBinding.inputUsernameEditText.text = null
            return
        }
        loginBinding.inputUsernameEditText.setText(email)
    }

    private fun setupOnClickListeners() {

        loginBinding.buttonLogin?.setOnClickListener { onLogin() }

        loginBinding.buttonRegister?.setOnClickListener {
            hideSoftKeyboard()
            presenter.onRegisterButtonClicked(loginBinding.inputUsernameEditText.text?.toString() ?:"" )
        }

        loginBinding.buttonForgotPassword.setOnClickListener {
            hideSoftKeyboard()
            presenter.onForgotPasswordButtonClicked(loginBinding.inputUsernameEditText.text?.toString() ?:"")
        }

        loginBinding.inputPasswordEditText.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onLogin()
            }
            false
        })
    }

    override fun onDataLoaded(data: LoginViewData){

        loginBinding.inputPassword.error = if(data.passValid) null else getString(R.string.invalid_password)

        if(!data.passValid)
            showSoftKeyboard(loginBinding.inputPasswordEditText)

        loginBinding.inputUsername.error = if (data.emailValid) null else getString(R.string.invalid_email)

        if(!data.emailValid)
            showSoftKeyboard(loginBinding.inputUsernameEditText)
    }

    override fun onLoading(loading: Boolean) {

        loginBinding.loginOverlayView.visibility = if(loading) View.VISIBLE else View.GONE
        loginBinding.loginProgressBar.visibility = if(loading) View.VISIBLE else View.GONE
    }

    private fun onLogin(){

        hideSoftKeyboard()

        presenter.onLoginButtonClicked(
            loginBinding.inputUsernameEditText.text?.toString() ?:"",
            loginBinding.inputPasswordEditText.text?.toString() ?:"")
    }

    fun refreshUi() {
        prefillEmail(arguments?.getString(KEY_EMAIL, null))
    }

    companion object {

        const val KEY_EMAIL: String = "email"
        val TAG = LoginFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(email: String?=null): LoginFragment = LoginFragment()
    }
}
package bg.dabulgaria.tibroish.presentation.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import kotlinx.android.synthetic.main.fragment_user_login.*


interface ILoginView :IBaseView{

    fun onDataLoaded(data: LoginViewData)

    fun onLoading(loading:Boolean)
}

class LoginFragment : BasePresentableFragment<ILoginView, ILoginPresenter>(), ILoginView {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.fragment_user_login, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupOnClickListeners()
    }

    private fun prefillEmail(email: String?) {
        if (email.isNullOrEmpty()) {
            input_username_edit_text.text = null
            return
        }
        input_username_edit_text.setText(email)
    }

    private fun setupOnClickListeners() {
        
        button_login?.setOnClickListener { onLogin() }

        button_register?.setOnClickListener {
            hideSoftKeyboard()
            presenter.onRegisterButtonClicked( input_username_edit_text?.text?.toString() ?:"" )
        }

        button_forgot_password?.setOnClickListener {
            hideSoftKeyboard()
            presenter.onForgotPasswordButtonClicked( input_username_edit_text?.text?.toString() ?:"")
        }

        input_password_edit_text.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onLogin()
            }
            false
        })
    }

    override fun onDataLoaded(data: LoginViewData){

        input_password?.error = if(data.passValid) null else getString(R.string.invalid_password)

        if(!data.passValid)
            showSoftKeyboard(input_password_edit_text)

        input_username?.error = if (data.emailValid) null else getString(R.string.invalid_email)

        if(!data.emailValid)
            showSoftKeyboard(input_username_edit_text)
    }

    override fun onLoading(loading: Boolean) {

        loginOverlayView?.visibility = if(loading) View.VISIBLE else View.GONE
        loginProgressBar?.visibility = if(loading) View.VISIBLE else View.GONE
    }

    private fun onLogin(){

        hideSoftKeyboard()

        presenter.onLoginButtonClicked(
                input_username_edit_text?.text?.toString() ?:"",
                input_password_edit_text?.text?.toString() ?:"")
    }

    fun refreshUi() {
        prefillEmail(arguments?.getString(KEY_EMAIL, null))
    }

    companion object {

        const val KEY_EMAIL: String = "email"
        val TAG = LoginFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
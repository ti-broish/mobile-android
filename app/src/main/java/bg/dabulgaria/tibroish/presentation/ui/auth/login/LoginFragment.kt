package bg.dabulgaria.tibroish.presentation.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BaseFragment
import bg.dabulgaria.tibroish.presentation.main.IMainNavigator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_user_login.*
import javax.inject.Inject


class LoginFragment : BaseFragment() {

    private lateinit var viewModel: LoginFragmentViewModel
    @Inject
    protected lateinit var mainNavigator: IMainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginFragmentViewModel::class.java)

        viewModel.auth = FirebaseAuth.getInstance()
        viewModel.mainNavigator = mainNavigator

        observeEmailValidState()
        observePasswordValidState()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        
        button_login?.setOnClickListener { onLoginButtonClicked() }
        button_register?.setOnClickListener { onRegisterButtonClicked() }
        button_forgot_password?.setOnClickListener { onForgotPasswordButtonClicked() }
    }

    private fun observePasswordValidState() {
        viewModel.getPasswordValidState().observe(this, Observer {
            val error: String? = if (it) {
                null
            } else {
                getString(R.string.invalid_password)
            }
            input_password?.error = error
        })
    }

    private fun observeEmailValidState() {
        viewModel.getEmailValidState().observe(this, Observer {
            val error: String? = if (it) {
                null
            } else {
                getString(R.string.invalid_email)
            }
            input_username?.error = error
        })
    }

    private fun onLoginButtonClicked() {

        val email = input_username_edit_text?.text?.toString() ?:""
        val isEmailValid = viewModel.processEmail(email)
        val password = input_password_edit_text?.text?.toString() ?:""
        val isPasswordValid = viewModel.processPassword(password)

        if (!isEmailValid || !isPasswordValid) {
            return
        }
        viewModel.login(email, password);
    }

    private fun onRegisterButtonClicked() {
        TODO("Not yet implemented")
    }

    private fun onForgotPasswordButtonClicked() {
        TODO("Not yet implemented")
    }

    companion object {

        val TAG = LoginFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
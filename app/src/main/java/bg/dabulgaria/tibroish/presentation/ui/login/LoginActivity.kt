package bg.dabulgaria.tibroish.presentation.ui.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import bg.dabulgaria.tibroish.R
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)
        observeEmailValidState()
        observePasswordValidState()

        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        findViewById<Button>(R.id.button_login).setOnClickListener {
            onLoginButtonClicked()
        }
        findViewById<Button>(R.id.button_register).setOnClickListener {
            onRegisterButtonClicked()
        }
        findViewById<Button>(R.id.button_forgot_password).setOnClickListener {
            onForgotPasswordButtonClicked()
        }
    }

    private fun observePasswordValidState() {
        viewModel.getPasswordValidState().observe(this, Observer {
            val error: String? = if (it) {
                null
            } else {
                getString(R.string.invalid_password)
            }
            findViewById<TextInputLayout>(R.id.input_password).error = error
        })
    }

    private fun observeEmailValidState() {
        viewModel.getEmailValidState().observe(this, Observer {
            val error: String? = if (it) {
                null
            } else {
                getString(R.string.invalid_email)
            }
            findViewById<TextInputLayout>(R.id.input_username).error = error
        })
    }

    private fun onLoginButtonClicked() {
        val email = findViewById<EditText>(R.id.input_username_edit_text).text.toString()
        val isEmailValid = viewModel.processEmail(email)
        val password = findViewById<EditText>(R.id.input_password_edit_text).text.toString()
        val isPasswordValid = viewModel.processPassword(password)
        if (!isEmailValid || !isPasswordValid) {
            return;
        }
        viewModel.login(email, password);
    }

    private fun onRegisterButtonClicked() {
        TODO("Not yet implemented")
    }

    private fun onForgotPasswordButtonClicked() {
        TODO("Not yet implemented")
    }
}
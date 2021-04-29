package bg.dabulgaria.tibroish.presentation.ui.registration

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import bg.dabulgaria.tibroish.R
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: RegisterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewModel = ViewModelProvider(this).get(RegisterActivityViewModel::class.java)

        viewModel.getCountryCodes().observe(this, Observer {
            val adapter = CountryCodesArrayAdapter(this, it)
            val dropdown = findViewById<AutoCompleteTextView>(R.id.input_area_code_dropdown)
            dropdown.setAdapter(adapter)
            dropdown.setText(adapter.getDefaultSelectedItem().code, /* filter= */ false)
            dropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                dropdown.setText(adapter.getItem(position)?.code, /* filter= */ false)
                adapter.filter.filter(null)
            }
        })

        viewModel.getOrganizations().observe(this, Observer {
            val adapter = OrganizationsAdapter(this, it)
            val dropdown = findViewById<AutoCompleteTextView>(R.id.input_organization_dropdown)
            dropdown.setAdapter(adapter)
            dropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                dropdown.setText(adapter.getItem(position)?.name, /* filter= */ false)
                adapter.filter.filter(null)
            }
        })

        findViewById<Button>(R.id.button_register).setOnClickListener(View.OnClickListener {
            if (validateFields()) {
                viewModel.register("", "")
            }
        })
    }

    private fun validateFields(): Boolean {
        var success = true
        if (!processRequiredField(R.id.input_first_name_edit_text, R.id.input_first_name)) {
            success = false
        }
        if (!processRequiredField(R.id.input_last_name_edit_text, R.id.input_last_name)) {
            success = false
        }
        if (!processEmailField()) {
            success = false
        }
        if (!processRequiredField(R.id.input_phone_number_edit_text, R.id.input_phone_number)) {
            success = false
        }
        if (!processEgnLastFourDigits()) {
            success = false
        }
        if (!processRequiredField(R.id.input_organization_dropdown, R.id.input_organization)) {
            success = false
        }
        if (!processPasswords()) {
            success = false
        }
        if (!processRequiredCheckbox(R.id.checkbox_over_18)) {
            success = false
        }
        return success
    }

    private fun processRequiredCheckbox(@IdRes checkboxResId: Int): Boolean {
        if (!findViewById<CheckBox>(checkboxResId).isChecked) {
            findViewById<CheckBox>(R.id.checkbox_over_18).error =
                    getString(R.string.confirm_over_18)
            return false
        }
        findViewById<CheckBox>(R.id.checkbox_over_18).error = null
        return true
    }

    private fun processPasswords(): Boolean {
        if (!processRequiredField(R.id.input_password_edit_text, R.id.input_password)) {
            return false
        }
        if (!processRequiredField(
                        R.id.input_repeat_password_edit_text, R.id.input_repeat_password)) {
            return false
        }
        val password = findViewById<EditText>(R.id.input_password_edit_text).text.toString()
        val repeatPassword =
                findViewById<EditText>(R.id.input_repeat_password_edit_text).text.toString()
        if (password.length < 6) {
            findViewById<TextInputLayout>(R.id.input_password).error =
                    getString(R.string.invalid_password_too_short)
            return false
        }
        if (password != repeatPassword) {
            findViewById<TextInputLayout>(R.id.input_password).error =
                    getString(R.string.passwords_do_not_match)
            return false
        }
        findViewById<TextInputLayout>(R.id.input_password).error = null
        return true
    }

    private fun processEgnLastFourDigits(): Boolean {
        if (!processRequiredField(R.id.input_egn_last_four_digits_edit_text,
                        R.id.input_egn_last_four_digits)) {
            return false
        }
        if (findViewById<EditText>(R.id.input_egn_last_four_digits_edit_text).text.length != 4) {
            findViewById<TextInputLayout>(R.id.input_egn_last_four_digits).error =
                    getString(R.string.egn_four_digits_required)
            return false
        }
        findViewById<TextInputLayout>(R.id.input_egn_last_four_digits).error = null
        return true
    }

    private fun processEmailField(): Boolean {
        if (!processRequiredField(R.id.input_email_edit_text, R.id.input_email)) {
            return false
        }
        val email: String = findViewById<EditText>(R.id.input_email_edit_text).text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            findViewById<TextInputLayout>(R.id.input_email).error =
                    getString(R.string.invalid_email)
            return false
        }
        findViewById<TextInputLayout>(R.id.input_email).error = null
        return true
    }

    private fun processRequiredField(
            @IdRes editTextResId: Int, @IdRes textLayoutResId: Int): Boolean {
        if (findViewById<EditText>(editTextResId).text.isBlank()) {
            findViewById<TextInputLayout>(textLayoutResId).error =
                    getString(R.string.required_field)
            return false
        }
        findViewById<TextInputLayout>(textLayoutResId).error = null
        return true
    }
}
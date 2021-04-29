package bg.dabulgaria.tibroish.presentation.ui.registration

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.IdRes
import androidx.annotation.StringRes
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

        setupCountryCodes()
        setupOrganizations()
        setupRegisterButton()
    }

    private fun setupRegisterButton() {
        findViewById<Button>(R.id.button_register).setOnClickListener(View.OnClickListener {
            if (validateFields()) {
                viewModel.register("", "")
            }
        })
    }

    private fun setupOrganizations() {
        viewModel.getOrganizations().observe(this, Observer {
            val adapter = OrganizationsAdapter(this, it)
            val dropdown = findViewById<AutoCompleteTextView>(R.id.input_organization_dropdown)
            dropdown.setAdapter(adapter)
            dropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                dropdown.setText(adapter.getItem(position)?.name, /* filter= */ false)
                adapter.filter.filter(null)
            }
        })
    }

    private fun setupCountryCodes() {
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
    }

    private fun validateFields(): Boolean {
        var success = true

        if (!processFirstName()) {
            success = false
        }

        if (!processLastName()) {
            success = false
        }

        if (!processEmail()) {
            success = false
        }

        if (!processPhoneNumber()) {
            success = false
        }

        if (!processEgnLastFourDigits()) {
            success = false
        }

        if (!processOrganization()) {
            success = false
        }

        if (!processPassword()) {
            success = false
        }

        if (!processRepeatPassword()) {
            success = false
        }

        if (!processOver18Checkbox()) {
            success = false
        }

        return success
    }

    private fun processOrganization(): Boolean {
        if (!viewModel.processOrganization(
                        getFieldText(R.id.input_organization_dropdown),
                        callback = {
                            setTextLayoutError(R.id.input_organization, it)
                        })) {
            return false
        }
        clearTextLayoutError(R.id.input_organization)
        return true
    }

    private fun processLastName(): Boolean {
        if (!viewModel.processRequiredField(
                        getFieldText(R.id.input_last_name_edit_text),
                        callback = {
                            setTextLayoutError(R.id.input_last_name, it)
                        })) {
            return false
        }
        clearTextLayoutError(R.id.input_last_name)
        return true
    }

    private fun processFirstName(): Boolean {
        if (!viewModel.processRequiredField(
                        getFieldText(R.id.input_first_name_edit_text),
                        callback = {
                            setTextLayoutError(R.id.input_first_name, it)
                        })) {
            return false
        }
        clearTextLayoutError(R.id.input_first_name)
        return true
    }

    private fun processPhoneNumber(): Boolean {
        val areaCode =
                findViewById<AutoCompleteTextView>(R.id.input_area_code_dropdown).text.toString()
        val localPhone = getFieldText(R.id.input_phone_number_edit_text)
        val fullPhoneNumber = areaCode + localPhone
        if (!viewModel.processPhoneNumberField(
                        fullPhoneNumber,
                        callback = {
                            setTextLayoutError(R.id.input_phone_number, it)
                        })) {
            return false
        }
        clearTextLayoutError(R.id.input_phone_number)
        return true
    }

    private fun processOver18Checkbox(): Boolean {
        val checkBox: CheckBox = findViewById(R.id.checkbox_over_18)
        if (!viewModel.processRequiredCheckbox(
                        checkBox,
                        callback = {
                            checkBox.error = getString(it)
                        },
                        errorCode = R.string.confirm_over_18)) {
            return false
        }
        checkBox.error = null
        return true
    }

    private fun processPassword(): Boolean {
        if (!viewModel.processPassword(
                        getFieldText(R.id.input_password_edit_text),
                        callback = {
                            setTextLayoutError(R.id.input_password, it)
                        })) {
            return false
        }
        clearTextLayoutError(R.id.input_password)
        return true
    }

    private fun processRepeatPassword(): Boolean {
        if (!viewModel.processRepeatPassword(
                        getFieldText(R.id.input_password_edit_text),
                        getFieldText(R.id.input_repeat_password_edit_text),
                        callback = {
                            setTextLayoutError(R.id.input_repeat_password, it)
                        })) {
            return false
        }
        clearTextLayoutError(R.id.input_repeat_password)
        return true
    }

    private fun processEgnLastFourDigits(): Boolean {
        if (!viewModel.processEgnLastFourDigits(
                        getFieldText(R.id.input_egn_last_four_digits_edit_text),
                        callback = {
                            setTextLayoutError(R.id.input_egn_last_four_digits, it)
                        })) {
            return false
        }
        clearTextLayoutError(R.id.input_egn_last_four_digits)
        return true
    }

    private fun processEmail(): Boolean {
        if (!viewModel.processEmailField(
                        getFieldText(R.id.input_email_edit_text),
                        callback = {
                            setTextLayoutError(R.id.input_email, it)
                        })) {
            return false
        }
        clearTextLayoutError(R.id.input_email)
        return true
    }

    private fun setTextLayoutError(@IdRes textLayoutId: Int, @StringRes stringRes: Int) {
        getInputTextLayout(textLayoutId).error = getString(stringRes)
    }

    private fun clearTextLayoutError(@IdRes textLayoutId: Int) {
        getInputTextLayout(textLayoutId).error = null
    }

    private fun getInputTextLayout(@IdRes resId: Int) = findViewById<TextInputLayout>(resId)

    private fun getFieldText(@IdRes resId: Int) = findViewById<EditText>(resId).text.toString()
}

package bg.dabulgaria.tibroish.presentation.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.presentation.ui.auth.login.LoginFragment
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_user_register.*

interface IRegisterView : IBaseView {

}

class RegistrationFragment : BasePresentableFragment<IRegisterView, IRegistrationPresenter>(), IRegisterView {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_user_register, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupCountryCodes()
        setupOrganizations()
        setupRegisterButton()
    }

    private fun setupRegisterButton() {
        button_register?.setOnClickListener {
            if (validateFields()) {
                presenter.register("", "")
            }
        }
    }

    private fun setupOrganizations() {
        presenter.getOrganizations { organizations: List<Organization>? ->
            if (organizations == null) {
                return@getOrganizations
            }
            val adapter = OrganizationsAdapter(requireContext(), organizations)
            val dropdown = input_organization_dropdown
            dropdown.setAdapter(adapter)
            dropdown.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                dropdown.setText(adapter.getItem(position)?.name, /* filter= */ false)
                adapter.filter.filter(null)
            }
        }
    }

    private fun setupCountryCodes() {
        presenter.getCountryCodes(requireContext(), callback = { countryCodes ->
            if (countryCodes == null) {
                return@getCountryCodes
            }
            val adapter = CountryCodesArrayAdapter(requireContext(), countryCodes)
            val dropdown = input_area_code_dropdown
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
        if (!presenter.processOrganization(
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
        if (!presenter.processRequiredField(
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
        if (!presenter.processRequiredField(
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
        val areaCode = input_area_code_dropdown?.text.toString()
        val localPhone = getFieldText(R.id.input_phone_number_edit_text)
        val fullPhoneNumber = areaCode + localPhone
        if (!presenter.processPhoneNumberField(
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
        val checkBox: CheckBox = checkbox_over_18
        if (!presenter.processRequiredCheckbox(
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
        if (!presenter.processPassword(
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
        if (!presenter.processRepeatPassword(
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
        if (!presenter.processEgnLastFourDigits(
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
        if (!presenter.processEmailField(
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
        getInputTextLayout(textLayoutId)?.error = getString(stringRes)
    }

    private fun clearTextLayoutError(@IdRes textLayoutId: Int) {
        getInputTextLayout(textLayoutId)?.error = null
    }

    private fun getInputTextLayout(@IdRes resId: Int) = view?.findViewById<TextInputLayout>(resId)

    private fun getFieldText(@IdRes resId: Int) = view?.findViewById<EditText>(resId)?.text.toString()

    companion object {
        val TAG = RegistrationFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = RegistrationFragment()
    }
}
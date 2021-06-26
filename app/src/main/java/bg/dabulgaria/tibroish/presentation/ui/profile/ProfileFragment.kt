package bg.dabulgaria.tibroish.presentation.ui.profile

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.domain.organisation.Organization
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.presentation.ui.common.DialogUtil
import bg.dabulgaria.tibroish.presentation.ui.common.IOrganizationsDropdownUtil
import bg.dabulgaria.tibroish.presentation.ui.common.IOrganizationsManager
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.checkbox_consent
import kotlinx.android.synthetic.main.fragment_profile.input_egn_last_four_digits_edit_text
import kotlinx.android.synthetic.main.fragment_profile.input_email_edit_text
import kotlinx.android.synthetic.main.fragment_profile.input_first_name_edit_text
import kotlinx.android.synthetic.main.fragment_profile.input_last_name_edit_text
import kotlinx.android.synthetic.main.fragment_profile.input_organization_dropdown
import kotlinx.android.synthetic.main.fragment_profile.input_phone_number_edit_text
import kotlinx.android.synthetic.main.fragment_user_register.*
import java.util.*
import javax.inject.Inject

interface IProfileView : IBaseView {
    fun onProfileFetchSuccess(user: User)

    fun onProfileFetchFail(@StringRes messageResId: Int)
}

class ProfileFragment : BasePresentableFragment<IProfileView,
        IProfilePresenter>
    (), IProfileView {

    @Inject
    lateinit var organizationsManager: IOrganizationsManager

    @Inject
    lateinit var organizationsDropdownUtil: IOrganizationsDropdownUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_profile, container, false
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupOrganizations()
        setupSaveButton()
        setupDeleteProfileButton()
        presenter.fetchUserDetails()
    }

    private fun setupDeleteProfileButton() {
        button_delete.setOnClickListener(onDeleteButtonClicked())
    }

    private fun onDeleteButtonClicked(): (v: View) -> Unit = {
        dialogUtil.showPromptDialog(
            requireContext(),
            R.string.dialog_generic_title,
            R.string.profile_delete_confirmation
        ) { _, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                deleteUser()
            }
        }
    }

    private fun deleteUser() {
        presenter.deleteUser(object : IDeleteUserCallback {
            override fun onSuccess() {
                onUserDeletionSuccess()
            }

            override fun onError(error: String) {
                onUserDeletionError(error)
            }
        })
    }

    private fun onUserDeletionSuccess() {
        dialogUtil.showDismissableDialog(
            requireActivity(),
            R.string.dialog_generic_title,
            R.string.profile_delete_success
        ) {
            presenter.navigateToLoginScreen()
        }
    }

    private fun onUserDeletionError(error: String) {
        dialogUtil.showDismissableDialog(
            requireActivity(),
            R.string.dialog_title_error,
            error
        ) {}
    }

    private fun setupOrganizations() {
        presenter.getOrganizations { organizations: List<Organization>? ->
            if (organizations == null) {
                return@getOrganizations
            }
            organizationsDropdownUtil.populateOrganizationsDropdown(
                requireContext(),
                input_organization_dropdown,
                organizations
            )
        }
    }

    private fun setupSaveButton() {
        button_save.isEnabled = false
        button_save.setOnClickListener {
            if (validateFields()) {
                send();
            }
        }
    }

    private fun validateFields(): Boolean {
        var success = true

        if (!processFirstName()) {
            success = false
        }

        if (!processLastName()) {
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

        return success
    }

    private fun send() {
        val user: User = presenter.createUserDetailsCopy()!!
        applyFieldsToUser(user)
        presenter.send(
            user,
            callback = object : IUpdateProfileCallback {
                override fun onSuccess() {
                    Log.i(TAG, "Profile update success!")
                    dialogUtil.showDismissableDialog(
                        requireActivity(),
                        R.string.dialog_generic_title,
                        R.string.profile_changes_saved
                    ) {}
                }
            })
    }

    private fun applyFieldsToUser(user: User) {
        user.firstName = input_first_name_edit_text.text.toString()
        user.lastName = input_last_name_edit_text.text.toString()
        user.phone = input_phone_number_edit_text.text.toString()
        user.pin = input_egn_last_four_digits_edit_text.text.toString()
        user.organization = getSelectedOrganization()!!
        user.hasAgreedToKeepData = checkbox_consent.isChecked
    }

    private fun getSelectedOrganization(): Organization? {
        return presenter.getOrganizationWithName(input_organization_dropdown?.text.toString())
    }

    override fun onProfileFetchSuccess(user: User) {
        input_first_name_edit_text.setText(user.firstName)
        input_last_name_edit_text.setText(user.lastName)
        input_email_edit_text.setText(user.email)
        input_phone_number_edit_text.setText(user.phone)
        input_egn_last_four_digits_edit_text.setText(user.pin)
        input_organization_dropdown.setText(user.organization?.name)
        checkbox_consent.isChecked = user.hasAgreedToKeepData
        button_save.isEnabled = true
    }

    override fun onProfileFetchFail(@StringRes messageResId: Int) {
        dialogUtil.showDismissableDialog(
            activity = requireActivity(),
            titleResId = R.string.dialog_title_error,
            messageResId = messageResId,
            dismissCallback = {})
    }

    private fun processLastName(): Boolean {
        if (!presenter.processRequiredField(
                getFieldText(R.id.input_last_name_edit_text),
                callback = {
                    setTextLayoutError(R.id.input_last_name, it)
                })
        ) {
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
                })
        ) {
            return false
        }
        clearTextLayoutError(R.id.input_first_name)
        return true
    }

    private fun processPhoneNumber(): Boolean {
        if (!presenter.processPhoneNumberField(
                input_phone_number_edit_text.text.toString(),
                callback = {
                    setTextLayoutError(R.id.input_phone_number, it)
                })
        ) {
            return false
        }
        clearTextLayoutError(R.id.input_phone_number)
        return true
    }

    private fun processEgnLastFourDigits(): Boolean {
        if (!presenter.processEgnLastFourDigits(
                getFieldText(R.id.input_egn_last_four_digits_edit_text),
                callback = {
                    setTextLayoutError(R.id.input_egn_last_four_digits, it)
                })
        ) {
            return false
        }
        clearTextLayoutError(R.id.input_egn_last_four_digits)
        return true
    }

    private fun processOrganization(): Boolean {
        if (!presenter.processOrganization(
                input_organization_dropdown?.text.toString()
            ) {
                setTextLayoutError(R.id.input_organization, it)
            }
        ) {
            return false
        }
        clearTextLayoutError(R.id.input_organization)
        return true
    }

    private fun setTextLayoutError(@IdRes textLayoutId: Int, @StringRes stringRes: Int) {
        getInputTextLayout(textLayoutId)?.error = getString(stringRes)
    }

    private fun clearTextLayoutError(@IdRes textLayoutId: Int) {
        getInputTextLayout(textLayoutId)?.error = null
    }

    private fun getInputTextLayout(@IdRes resId: Int) = view?.findViewById<TextInputLayout>(resId)

    private fun getFieldText(@IdRes resId: Int) =
        view?.findViewById<EditText>(resId)?.text.toString()

    companion object {
        val TAG = ProfileFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}
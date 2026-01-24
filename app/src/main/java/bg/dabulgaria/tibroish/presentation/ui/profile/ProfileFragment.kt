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
import bg.dabulgaria.tibroish.domain.user.User
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import com.google.android.material.textfield.TextInputLayout
import bg.dabulgaria.tibroish.databinding.FragmentProfileBinding

interface IProfileView : IBaseView {
    fun onProfileFetchSuccess(user: User)

    fun onProfileFetchFail(@StringRes messageResId: Int)
}

class ProfileFragment : BasePresentableFragment<IProfileView,
        IProfilePresenter>
    (), IProfileView {

    private var _profileBinding: FragmentProfileBinding? = null
    private val profileBinding get() = _profileBinding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _profileBinding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_profile, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _profileBinding = FragmentProfileBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupSaveButton()
        setupDeleteProfileButton()
        presenter.fetchUserDetails()
    }

    private fun setupDeleteProfileButton() {
        profileBinding.buttonDelete.setOnClickListener(onDeleteButtonClicked())
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

    private fun setupSaveButton() {
        profileBinding.buttonSave.isEnabled = false
        profileBinding.buttonSave.setOnClickListener {
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

        return success
    }

    private fun send() {
        val user: User = presenter.createUserDetailsCopy()!!
        applyFieldsToUser(user)
        hideSoftKeyboard()
        presenter.send(
            user,
            callback = object : IUpdateProfileCallback {
                override fun onSuccess() {
                    Log.i(TAG, "Profile update success!")
                    dialogUtil.showDismissableDialog(
                        requireActivity(),
                        R.string.dialog_generic_title,
                        R.string.profile_changes_saved
                    ) {
                        presenter.navigateToHomeScreen()
                    }
                }
            })
    }

    private fun applyFieldsToUser(user: User) {
        user.firstName = profileBinding.inputFirstNameEditText.text.toString()
        user.lastName = profileBinding.inputLastNameEditText.text.toString()
        user.phone = profileBinding.inputPhoneNumberEditText.text.toString()
        user.pin = profileBinding.inputEgnLastFourDigitsEditText.text.toString()
        user.hasAgreedToKeepData = profileBinding.checkboxConsent.isChecked
    }

    override fun onProfileFetchSuccess(user: User) {
        profileBinding.inputFirstNameEditText.setText(user.firstName)
        profileBinding.inputLastNameEditText.setText(user.lastName)
        profileBinding.inputEmailEditText.setText(user.email)
        profileBinding.inputPhoneNumberEditText.setText(user.phone)
        profileBinding.inputEgnLastFourDigitsEditText.setText(user.pin)
        profileBinding.inputOrganizationEditText.setText(user.organization?.name)
        profileBinding.checkboxConsent.isChecked = user.hasAgreedToKeepData
        profileBinding.buttonSave.isEnabled = true
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
                profileBinding.inputPhoneNumberEditText.text.toString(),
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
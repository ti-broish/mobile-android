package bg.dabulgaria.tibroish.presentation.ui.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.presentation.ui.common.DialogUtil
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import javax.inject.Inject

interface IForgotPasswordView : IBaseView {
    fun passwordResetSuccess()

    fun passwordResetFail(@StringRes messageResId: Int)
}

class ForgotPasswordFragment : BasePresentableFragment<IForgotPasswordView,
        IForgotPasswordPresenter>
    (), IForgotPasswordView {

    @Inject
    lateinit var dialogUtil: DialogUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_forgot_password, container, false
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        prefillEmail(arguments?.getString(KEY_EMAIL))
        setupSendButton()
    }

    private fun prefillEmail(email: String?) {
        if (email.isNullOrEmpty()) {
            return
        }
        input_email_edit_text.setText(email)
    }

    private fun setupSendButton() {
        button_send.setOnClickListener {
            onSendButtonClicked()
        }
    }

    private fun onSendButtonClicked() {
        val email = input_email_edit_text.text.toString()
        if (presenter.processEmailField(email) {
                input_email.error = getString(it)
            }) {
            presenter.sendPasswordResetEmail(email)
            input_email.error = null
        }
    }

    override fun passwordResetSuccess() {
        dialogUtil.showDismissableDialog(
            activity = requireActivity(),
            titleResId = R.string.password_reset_success_title,
            messageResId = R.string.password_reset_success_message,
            dismissCallback = {})
    }

    override fun passwordResetFail(@StringRes messageResId: Int) {
        dialogUtil.showDismissableDialog(
            activity = requireActivity(),
            titleResId = R.string.dialog_title_error,
            messageResId = messageResId,
            dismissCallback = {})
    }

    companion object {
        val TAG = ForgotPasswordFragment::class.java.simpleName
        val KEY_EMAIL = "email"

        @JvmStatic
        fun newInstance() = ForgotPasswordFragment()
    }
}
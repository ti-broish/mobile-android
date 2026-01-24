package bg.dabulgaria.tibroish.presentation.ui.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import bg.dabulgaria.tibroish.R
import bg.dabulgaria.tibroish.presentation.base.BasePresentableFragment
import bg.dabulgaria.tibroish.presentation.base.IBaseView
import bg.dabulgaria.tibroish.databinding.FragmentForgotPasswordBinding

interface IForgotPasswordView : IBaseView {
    fun passwordResetSuccess(email: String)

    fun passwordResetFail(@StringRes messageResId: Int)
}

class ForgotPasswordFragment : BasePresentableFragment<IForgotPasswordView,
        IForgotPasswordPresenter>
    (), IForgotPasswordView {


    private var _forgotPasswordBinding: FragmentForgotPasswordBinding? = null
    private val forgotPasswordBinding get() = _forgotPasswordBinding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _forgotPasswordBinding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_forgot_password, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _forgotPasswordBinding = FragmentForgotPasswordBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        prefillEmail(arguments?.getString(KEY_EMAIL))
        setupSendButton()
    }

    private fun prefillEmail(email: String?) {
        if (email.isNullOrEmpty()) {
            return
        }
        forgotPasswordBinding.inputEmailEditText.setText(email)
    }

    private fun setupSendButton() {
        forgotPasswordBinding.buttonSend.setOnClickListener {
            onSendButtonClicked()
        }
    }

    private fun onSendButtonClicked() {
        val email = forgotPasswordBinding.inputEmailEditText.text.toString()
        if (presenter.processEmailField(email) {
                forgotPasswordBinding.inputEmail.error = getString(it)
            }) {
            presenter.sendPasswordResetEmail(email)
            forgotPasswordBinding.inputEmail.error = null
        }
    }

    override fun passwordResetSuccess(email: String) {
        dialogUtil.showDismissableDialog(
            activity = requireActivity(),
            titleResId = R.string.password_reset_success_title,
            messageResId = R.string.password_reset_success_message,
            dismissCallback = {
                presenter.showLoginScreen(email)
            })
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
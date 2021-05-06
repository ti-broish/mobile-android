package bg.dabulgaria.tibroish.presentation.base

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment(): Fragment() {

    override fun onAttach(context: Context) {

        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    protected fun showSoftKeyboard( editText: EditText?) {

        if (activity == null) return

        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (imm == null || editText == null) return

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        editText.requestFocus()
        editText.setSelection(editText.text?.length?:0)
    }

    protected fun hideSoftKeyboard() {

        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)?.let {

            it.hideSoftInputFromWindow(activity?.currentFocus?.getWindowToken(), 0)
        }
    }
}
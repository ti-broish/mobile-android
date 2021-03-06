package bg.dabulgaria.tibroish.presentation.ui.common

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes

interface IDialogUtil {
    fun showDismissableDialog(
        activity: Activity,
        @StringRes titleResId: Int,
        @StringRes messageResId: Int,
        dismissCallback: () -> Unit
    )

    fun showDismissableDialog(
            activity: Activity,
            message: String,
            dismissCallback: () -> Unit
    )

    fun showDismissableDialog(
        activity: Activity,
        @StringRes titleResId: Int,
        message: String,
        dismissCallback: () -> Unit
    )

    fun showPromptDialog(
        context: Context,
        @StringRes titleResId: Int,
        @StringRes messageResId: Int,
        callback: DialogInterface.OnClickListener
    )
}
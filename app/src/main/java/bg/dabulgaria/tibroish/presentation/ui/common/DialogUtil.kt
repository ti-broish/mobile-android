package bg.dabulgaria.tibroish.presentation.ui.common

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import javax.inject.Inject

class DialogUtil @Inject constructor() : IDialogUtil{
    override fun showDismissableDialog(
        activity: Activity,
        @StringRes titleResId: Int,
        @StringRes messageResId: Int,
        dismissCallback: () -> Unit
    ) {
        AlertDialog.Builder(activity)
            .setTitle(activity.getString(titleResId))
            .setMessage(activity.getString(messageResId))
            .setOnCancelListener { dismissCallback.invoke() }
            .setPositiveButton(android.R.string.ok) { _, _ -> dismissCallback.invoke() }
            .show()
    }

    override fun showDismissableDialog(activity: Activity, message: String, dismissCallback: () -> Unit) {

        AlertDialog.Builder(activity)
                .setMessage(message)
                .setOnCancelListener { dismissCallback.invoke() }
                .setPositiveButton(android.R.string.ok) { _, _ -> dismissCallback.invoke() }
                .show()
    }

    override fun showDismissableDialog(
        activity: Activity,
        @StringRes titleResId: Int,
        message: String,
        dismissCallback: () -> Unit
    ) {
        AlertDialog.Builder(activity)
            .setTitle(titleResId)
            .setMessage(message)
            .setOnCancelListener { dismissCallback.invoke() }
            .setPositiveButton(android.R.string.ok) { _, _ -> dismissCallback.invoke() }
            .show()
    }

    override fun showPromptDialog(
        context: Context,
        @StringRes titleResId: Int,
        @StringRes messageResId: Int,
        callback: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(titleResId))
            .setMessage(context.getString(messageResId))
            .setPositiveButton(android.R.string.ok, callback)
            .setNegativeButton(android.R.string.cancel, callback)
            .show()
    }
}
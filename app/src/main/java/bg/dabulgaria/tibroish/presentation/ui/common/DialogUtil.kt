package bg.dabulgaria.tibroish.presentation.ui.common

import android.app.AlertDialog
import android.content.Context
import androidx.annotation.StringRes
import bg.dabulgaria.tibroish.R

class DialogUtil {
    fun showDismissableDialog(
        context: Context,
        @StringRes titleResId: Int,
        @StringRes messageResId: Int,
        dismissCallback: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(titleResId))
            .setMessage(context.getString(messageResId))
            .setPositiveButton(android.R.string.ok) { _, _ -> dismissCallback.invoke() }
            .show()
    }
}
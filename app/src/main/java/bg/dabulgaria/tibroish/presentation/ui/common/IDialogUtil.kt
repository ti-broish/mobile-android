package bg.dabulgaria.tibroish.presentation.ui.common

import android.app.Activity
import androidx.annotation.StringRes

interface IDialogUtil {
    fun showDismissableDialog(
        activity: Activity,
        @StringRes titleResId: Int,
        @StringRes messageResId: Int,
        dismissCallback: () -> Unit
    )
}
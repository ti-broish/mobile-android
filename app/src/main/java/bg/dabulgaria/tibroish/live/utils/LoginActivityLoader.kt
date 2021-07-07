package bg.dabulgaria.tibroish.live.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import bg.dabulgaria.tibroish.domain.user.IUserAuthenticator


class LauncherIntent(context: Context) :
        Intent(context.packageManager.getLaunchIntentForPackage(context.packageName))


private const val RESTARTED_TO_LOGIN_EXTRA =
        "bg.dabulgaria.tibroish.stream.util.LoginActivityLoader.RESTARTED_TO_LOGIN_EXTRA"

class LoginActivityLoader(private val context: Context, private val userAuthenticator: IUserAuthenticator) {
    fun logout() {

        val intent = LauncherIntent(context).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            putExtra(RESTARTED_TO_LOGIN_EXTRA, true)
        }

        context.startActivity(intent)
    }
}


fun Activity.isStartedFromLoginScreenLoader(): Boolean {
    return intent.extras?.getBoolean(RESTARTED_TO_LOGIN_EXTRA, false) ?: false
}

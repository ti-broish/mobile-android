package bg.dabulgaria.tibroish.live.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

@Suppress("DEPRECATION")
fun Activity.makeFullScreen() {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        registerActivityLifecycleCallbacks(FullScreenActivityCallback)
    } else {

        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}

@RequiresApi(Build.VERSION_CODES.R)
object FullScreenActivityCallback : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity) {
        // nothing to do
    }

    override fun onActivityStarted(activity: Activity) {
        activity.window?.insetsController?.hide(WindowInsets.Type.statusBars())
    }

    override fun onActivityDestroyed(activity: Activity) {
        // nothing to do
    }

    override fun onActivitySaveInstanceState(activity: Activity, intent: Bundle) {
        // nothing to do
    }

    override fun onActivityStopped(activity: Activity) {
        // nothing to do
    }

    override fun onActivityCreated(activity: Activity, intent: Bundle?) {
        // nothing to do
    }

    override fun onActivityResumed(activity: Activity) {
        // nothing to do
    }

}

fun Activity.isCallActive(): Boolean {
    val manager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    return manager.mode == AudioManager.MODE_IN_CALL || manager.mode == AudioManager.MODE_IN_COMMUNICATION
}

fun LifecycleOwner.isActive(): Boolean = lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)

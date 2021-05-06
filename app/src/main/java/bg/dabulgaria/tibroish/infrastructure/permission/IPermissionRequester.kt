package bg.dabulgaria.tibroish.infrastructure.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import bg.dabulgaria.tibroish.infrastructure.di.annotations.PerActivity
import javax.inject.Inject

interface IPermissionRequester {

    fun requestPermission(appPermission: AppPermission)

    fun hasPermission(appPermission: AppPermission): Boolean

    fun shouldShowRequestPermissionRationale(appPermission: AppPermission): Boolean
}

class PermissionRequester @Inject constructor(private val activity: AppCompatActivity) :IPermissionRequester{

    override fun requestPermission(appPermission: AppPermission) {

        // No explanation needed, we can request the permission.
        Log.i(TAG, "requestPermission ${appPermission.name}")
        ActivityCompat.requestPermissions(activity, arrayOf(appPermission.name),
                appPermission.code)
    }

    override fun hasPermission(appPermission: AppPermission): Boolean {

        val result = ContextCompat.checkSelfPermission(activity, appPermission.name) == PackageManager.PERMISSION_GRANTED
        Log.i(TAG, "hasPermission ${appPermission.name} result $result")
        return result
    }

    override fun shouldShowRequestPermissionRationale(appPermission: AppPermission): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, appPermission.name)
    }

    companion object{
        val TAG = PermissionRequester::class.java.simpleName
    }
}
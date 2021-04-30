package bg.dabulgaria.tibroish.infrastructure.permission

import android.content.pm.PackageManager
import bg.dabulgaria.tibroish.presentation.main.IMainNavigator
import javax.inject.Inject

interface IPermissionResponseHandler {

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
}

class PermissionResponseHandler @Inject constructor(private val mainNavigator: IMainNavigator) :IPermissionResponseHandler{

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        val granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED

        mainNavigator.onPermissionResult(requestCode, granted)
    }

}
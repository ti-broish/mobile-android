package bg.dabulgaria.tibroish.infrastructure.permission

import android.content.pm.PackageManager
import bg.dabulgaria.tibroish.presentation.main.IMainRouter
import javax.inject.Inject

interface IPermissionResponseHandler {

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
}

class PermissionResponseHandler @Inject constructor(private val mainRouter: IMainRouter) :IPermissionResponseHandler{

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        val granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED

        mainRouter.onPermissionResult(requestCode, granted)
    }

}
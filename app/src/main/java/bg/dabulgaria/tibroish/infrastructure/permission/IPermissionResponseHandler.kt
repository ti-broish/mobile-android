package bg.dabulgaria.tibroish.infrastructure.permission

import android.content.pm.PackageManager
import javax.inject.Inject

interface IPermissionResponseHandler {

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
}

class PermissionResponseHandler @Inject constructor() :IPermissionResponseHandler{

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        val granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED

        when(requestCode){

            PermissionCodes.READ_STORAGE.code ->{

            }

            PermissionCodes.CAMERA.code ->{
                TODO("Not yet implemented")
            }
        }
    }

}
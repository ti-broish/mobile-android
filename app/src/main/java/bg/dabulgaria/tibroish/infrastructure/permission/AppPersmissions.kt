package bg.dabulgaria.tibroish.infrastructure.permission

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat


class AppPermission(val code:Int, val name:String)

object PermissionCodes{

    val READ_STORAGE = AppPermission( 103,"android.permission.READ_EXTERNAL_STORAGE")
    val CAMERA = AppPermission( 104,"android.permission.CAMERA")
    val READ_MEDIA_IMAGES =  AppPermission( 105,"android.permission.READ_MEDIA_IMAGES")
}
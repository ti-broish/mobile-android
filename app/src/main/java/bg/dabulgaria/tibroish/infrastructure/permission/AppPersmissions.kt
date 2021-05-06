package bg.dabulgaria.tibroish.infrastructure.permission


class AppPermission(val code:Int, val name:String)

object PermissionCodes{

    val READ_STORAGE = AppPermission( 103,"android.permission.READ_EXTERNAL_STORAGE")
    val CAMERA = AppPermission( 104,"android.permission.CAMERA")
}
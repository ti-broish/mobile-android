package bg.dabulgaria.tibroish.presentation.main

interface IPermissionResponseListener {

    fun onPermissionResult(permissionCode:Int, granted:Boolean)
}
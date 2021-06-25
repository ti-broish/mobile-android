package bg.dabulgaria.tibroish.live.utils

import android.content.Context
import android.net.ConnectivityManager

class NetworkObserver(private val context: Context) {

    //TODO Implemented alternative non-deprecated method for checking connectivity
    @Deprecated("Current implementation uses deprecated methods")
    fun hasNetworkConnectivity(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}

package bg.dabulgaria.tibroish.presentation.providers

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

class NetworkInfoProvider(private val context: Context) : INetworkInfoProvider {

    override val isNetworkConnected: Boolean
        get() {

            var isConnected = false

            try {

                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = cm.activeNetworkInfo
                isConnected = networkInfo != null && networkInfo.isConnected
            }
            catch (ex: Exception) {

                Log.e(LOG_TAG, ex.message, ex)
            }

            return isConnected
        }

    companion object {

        private val LOG_TAG = NetworkInfoProvider::class.simpleName
    }
}

package bg.dabulgaria.tibroish.live.utils

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager


class ConnectionUtil(private val context: Context) {

    fun getNetworkClass(): NetworkClass {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        if (info == null || !info.isConnected) return NetworkClass.NONE // not connected
        if (info.type == ConnectivityManager.TYPE_WIFI) return NetworkClass.WIFI
        if (info.type == ConnectivityManager.TYPE_MOBILE) {
            return when (info.subtype) {
                TelephonyManager.NETWORK_TYPE_GPRS,
                TelephonyManager.NETWORK_TYPE_EDGE,
                TelephonyManager.NETWORK_TYPE_CDMA,
                TelephonyManager.NETWORK_TYPE_1xRTT,
                TelephonyManager.NETWORK_TYPE_IDEN,
                TelephonyManager.NETWORK_TYPE_GSM -> NetworkClass.MOBILE_2G
                TelephonyManager.NETWORK_TYPE_UMTS,
                TelephonyManager.NETWORK_TYPE_EVDO_0,
                TelephonyManager.NETWORK_TYPE_EVDO_A,
                TelephonyManager.NETWORK_TYPE_HSDPA,
                TelephonyManager.NETWORK_TYPE_HSUPA,
                TelephonyManager.NETWORK_TYPE_HSPA,
                TelephonyManager.NETWORK_TYPE_EVDO_B,
                TelephonyManager.NETWORK_TYPE_EHRPD,
                TelephonyManager.NETWORK_TYPE_HSPAP,
                TelephonyManager.NETWORK_TYPE_TD_SCDMA -> NetworkClass.MOBILE_3G
                TelephonyManager.NETWORK_TYPE_LTE,
                TelephonyManager.NETWORK_TYPE_IWLAN, 19 -> NetworkClass.MOBILE_4G
                TelephonyManager.NETWORK_TYPE_NR -> NetworkClass.MOBILE_5G
                else -> NetworkClass.NONE
            }
        }
        return NetworkClass.NONE
    }
}

enum class NetworkClass {
    WIFI, NONE, MOBILE_2G, MOBILE_3G, MOBILE_4G, MOBILE_5G
}

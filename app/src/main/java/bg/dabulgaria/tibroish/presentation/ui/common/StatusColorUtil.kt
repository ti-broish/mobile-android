package bg.dabulgaria.tibroish.presentation.ui.common

import android.graphics.Color

interface IStatusColorUtil {
    fun getColorForStatus(status: String): Int
}

class StatusColorUtil : IStatusColorUtil {
    override fun getColorForStatus(status: String): Int {
        return Color.parseColor(when (status) {
            "received" -> "#FF9900"
            "rejected" -> "#FF0000"
            "approved" -> "#4CAF50"
            "processed" -> "#4CAF50"
            else -> {
                "#666666"
            }
        })
    }
}

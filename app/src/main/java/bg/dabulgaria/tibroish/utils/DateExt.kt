package bg.dabulgaria.tibroish.utils

import java.text.SimpleDateFormat
import java.util.*


fun Date.toSimpleDayTimeString() : String {
    val format = SimpleDateFormat("dd/MM/yyyy hh:mm")
    return format.format(this)
}
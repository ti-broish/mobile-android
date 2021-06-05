package bg.dabulgaria.tibroish.domain.date

import androidx.room.TypeConverter
import java.util.*

class DateTypeConverter {
    @TypeConverter
    fun toDate(value: Long): Date? = Date(value)

    @TypeConverter
    fun fromDate(value: Date): Long = value.time
}
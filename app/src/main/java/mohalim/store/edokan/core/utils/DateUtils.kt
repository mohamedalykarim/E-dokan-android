package mohalim.store.edokan.core.utils

import java.math.BigInteger
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {
        @JvmStatic
        fun convertMilisToDate(timeInMillis : BigInteger): String {
            val formatter : DateFormat = SimpleDateFormat("yyyy/mm/dd")
            val calendar = Calendar.getInstance();
            calendar.timeInMillis = timeInMillis.toLong()

            return formatter.format(calendar.time)
        }

        @JvmStatic
        fun convertMilisToDate(timeInMillis : BigInteger, pattern : String): String {
            val formatter : DateFormat = SimpleDateFormat(pattern)
            val calendar = Calendar.getInstance();
            calendar.timeInMillis = timeInMillis.toLong()

            return formatter.format(calendar.time)
        }
    }
}
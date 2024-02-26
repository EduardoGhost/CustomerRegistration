package com.eduardo.customerregistration.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private const val DATE_FORMAT = "dd/MM/yyyy"
    @JvmStatic
    fun getTimestampFromDateString(dateString: String?): Long {
        return try {
            val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            val date = sdf.parse(dateString)
            date?.time ?: 0
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    @JvmStatic
    fun formatDateFromTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }
}
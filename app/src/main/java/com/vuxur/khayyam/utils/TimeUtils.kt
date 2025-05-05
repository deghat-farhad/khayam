package com.vuxur.khayyam.utils

import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import com.vuxur.khayyam.model.TimeOfDayItem
import java.text.SimpleDateFormat
import java.util.*

fun TimeOfDayItem.formatForLocale(context: Context): String {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
    }

    val is24Hour = DateFormat.is24HourFormat(context)

    val locale = if (Build.VERSION.SDK_INT >= 24) {
        context.resources.configuration.locales[0]
    } else {
        Locale.getDefault()
    }

    val pattern = if (is24Hour) "HH:mm" else "h:mm a"
    val formatter = SimpleDateFormat(pattern, locale)

    return formatter.format(calendar.time)
}
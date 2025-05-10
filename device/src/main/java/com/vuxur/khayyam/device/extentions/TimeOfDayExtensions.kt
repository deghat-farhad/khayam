package com.vuxur.khayyam.device.extentions

import com.vuxur.khayyam.device.model.TimeOfDayDeviceModel
import java.util.Calendar

fun TimeOfDayDeviceModel.toNextTriggerMillis(): Long {
    val now = Calendar.getInstance()
    val target = now.clone() as Calendar
    target.set(Calendar.HOUR_OF_DAY, hour)
    target.set(Calendar.MINUTE, minute)
    target.set(Calendar.SECOND, 0)
    target.set(Calendar.MILLISECOND, 0)

    if (target.before(now)) {
        target.add(Calendar.DAY_OF_YEAR, 1)
    }

    return target.timeInMillis
}
package com.vuxur.khayyam.device.mapper

import com.vuxur.khayyam.device.model.TimeOfDayDeviceModel
import com.vuxur.khayyam.domain.model.TimeOfDay
import javax.inject.Inject

class TimeOfDayDeviceModelMapper @Inject constructor() {
    fun mapToDevice(timeOfDay: TimeOfDay) = TimeOfDayDeviceModel(
        hour = timeOfDay.hour,
        minute = timeOfDay.minute,
    )
}
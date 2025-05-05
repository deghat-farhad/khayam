package com.vuxur.khayyam.mapper

import com.vuxur.khayyam.domain.model.TimeOfDay
import com.vuxur.khayyam.model.TimeOfDayItem
import javax.inject.Inject

class TimeOfDayItemMapper @Inject constructor() {
    fun mapToDomain(timeOfDayItem: TimeOfDayItem) = TimeOfDay(
        timeOfDayItem.hour,
        timeOfDayItem.minute,
    )

    fun mapToPresentation(timeOfDay: TimeOfDay) = TimeOfDayItem(
        timeOfDay.hour,
        timeOfDay.minute,
    )
}
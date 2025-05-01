package com.vuxur.khayyam.data.mapper

import com.vuxur.khayyam.data.entity.TimeOfDayEntity
import com.vuxur.khayyam.domain.model.TimeOfDay
import javax.inject.Inject

class TimeOfDayEntityMapper @Inject constructor() {
    fun mapToData(timeOfDay: TimeOfDay) = TimeOfDayEntity(
        hours = timeOfDay.hours,
        minutes = timeOfDay.minutes,
    )

    fun mapToDomain(timeOfDayEntity: TimeOfDayEntity) = TimeOfDay(
        hours = timeOfDayEntity.hours,
        minutes = timeOfDayEntity.minutes,
    )
}
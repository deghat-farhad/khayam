package com.vuxur.khayyam.data.utils

import com.vuxur.khayyam.data.entity.TimeOfDayEntity

fun TimeOfDayEntity.toMinutes() = hours * 60 + minutes
fun Int.toTimeOfDayEntity() = TimeOfDayEntity(
    hours = this / 60,
    minutes = this % 60,
).also {
    require(it.hours in 0..23) { "Invalid hour: ${it.hours}" }
    require(it.minutes in 0..59) { "Invalid minute: ${it.minutes}" }
}
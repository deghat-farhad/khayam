package com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.time.setRandomPoemNotificationTime

import com.vuxur.khayyam.domain.model.TimeOfDay

data class SetRandomPoemNotificationTimeParams(
    val timeOfDay: TimeOfDay,
)
package com.vuxur.khayyam.domain.repository

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.model.TimeOfDay

interface NotificationRepository {
    suspend fun rescheduleNotification(timeOfDay: TimeOfDay)
    suspend fun showRandomPoemNotification(poem: Poem)
    suspend fun cancelRandomPoemNotification()
}
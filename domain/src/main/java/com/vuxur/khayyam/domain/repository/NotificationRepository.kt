package com.vuxur.khayyam.domain.repository

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.model.TimeOfDay

interface NotificationRepository {
    suspend fun rescheduleNotification(timeOfDay: TimeOfDay, uniqueRequestCode: Int)
    suspend fun showRandomPoemNotification(poem: Poem)
    suspend fun cancelRandomPoemNotification(uniqueRequestCode: Int)
}
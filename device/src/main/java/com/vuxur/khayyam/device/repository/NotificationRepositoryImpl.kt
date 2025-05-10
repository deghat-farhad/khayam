package com.vuxur.khayyam.device.repository

import android.util.Log
import com.vuxur.khayyam.device.mapper.PoemDevicePoemMapper
import com.vuxur.khayyam.device.mapper.TimeOfDayDeviceModelMapper
import com.vuxur.khayyam.device.notification.NotificationScheduler
import com.vuxur.khayyam.device.notification.PoemNotificationManager
import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.model.TimeOfDay
import com.vuxur.khayyam.domain.repository.NotificationRepository

class NotificationRepositoryImpl(
    private val notificationScheduler: NotificationScheduler,
    private val timeOfDayDeviceModelMapper: TimeOfDayDeviceModelMapper,
    private val poemNotificationManager: PoemNotificationManager,
    private val poemDeviceModelMapper: PoemDevicePoemMapper,
) : NotificationRepository {
    override suspend fun rescheduleNotification(timeOfDay: TimeOfDay) {
        notificationScheduler.scheduleAt(timeOfDayDeviceModelMapper.mapToDevice(timeOfDay))
    }

    override suspend fun showRandomPoemNotification(poem: Poem) {
        Log.i("NotificationRepository", poem.toString())
        poemNotificationManager.show(PoemDevicePoemMapper.mapToDevice(poem))
    }
}
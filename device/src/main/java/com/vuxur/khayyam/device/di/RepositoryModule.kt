package com.vuxur.khayyam.device.di

import com.vuxur.khayyam.device.mapper.PoemDevicePoemMapper
import com.vuxur.khayyam.device.mapper.TimeOfDayDeviceModelMapper
import com.vuxur.khayyam.device.notification.NotificationScheduler
import com.vuxur.khayyam.device.notification.PoemNotificationManager
import com.vuxur.khayyam.device.repository.NotificationRepositoryImpl
import com.vuxur.khayyam.domain.repository.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun provideNotificationRepository(
        notificationScheduler: NotificationScheduler,
        timeOfDayDeviceModelMapper: TimeOfDayDeviceModelMapper,
        poemNotificationManager: PoemNotificationManager,
        poemDevicePoemMapper: PoemDevicePoemMapper,
    ): NotificationRepository = NotificationRepositoryImpl(
        notificationScheduler,
        timeOfDayDeviceModelMapper,
        poemNotificationManager,
        poemDevicePoemMapper,
    )
}
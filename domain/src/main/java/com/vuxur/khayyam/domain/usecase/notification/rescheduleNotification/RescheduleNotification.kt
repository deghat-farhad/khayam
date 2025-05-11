package com.vuxur.khayyam.domain.usecase.notification.rescheduleNotification

import com.vuxur.khayyam.domain.repository.NotificationRepository
import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.first

class RescheduleNotification(
    private val settingRepository: SettingRepository,
    private val notificationRepository: NotificationRepository,
) : UseCase<Unit> {
    override suspend fun invoke() {
        val isEnable = settingRepository.isRandomPoemNotificationEnabled.first()
        if (isEnable) {
            settingRepository.randomPoemNotificationTime.first()?.let { timeOfDay ->
                notificationRepository.rescheduleNotification(timeOfDay)
            }
        } else {
            notificationRepository.cancelRandomPoemNotification()
        }
    }
}
package com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.time.getRandomPoemNotificationTime

import com.vuxur.khayyam.domain.model.TimeOfDay
import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow

class GetRandomPoemNotificationTime(
    private val settingRepository: SettingRepository,
) : UseCase<Flow<TimeOfDay?>> {
    override suspend fun invoke(): Flow<TimeOfDay?> {
        return settingRepository.randomPoemNotificationTime
    }
}
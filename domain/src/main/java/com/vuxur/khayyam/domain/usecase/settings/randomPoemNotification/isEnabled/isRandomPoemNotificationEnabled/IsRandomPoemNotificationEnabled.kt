package com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.isEnabled.isRandomPoemNotificationEnabled

import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow

class IsRandomPoemNotificationEnabled(
    private val settingRepository: SettingRepository,
) : UseCase<Flow<Boolean>> {
    override suspend operator fun invoke(): Flow<Boolean> {
        return settingRepository.isRandomPoemNotificationEnabled
    }
}
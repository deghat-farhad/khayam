package com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.isEnabled.setRandomPoemNotificationsEnabled

import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams

class SetRandomPoemNotificationEnabled(
    private val settingRepository: SettingRepository,
) : UseCaseWithParams<Unit, SetRandomPoemNotificationEnabledParams> {
    override suspend fun invoke(params: SetRandomPoemNotificationEnabledParams) {
        return settingRepository.setRandomPoemNotificationEnabled(
            params.isEnabled
        )
    }
}
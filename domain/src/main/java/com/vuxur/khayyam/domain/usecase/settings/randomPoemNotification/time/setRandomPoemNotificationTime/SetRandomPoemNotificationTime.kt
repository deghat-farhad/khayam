package com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.time.setRandomPoemNotificationTime

import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams

class SetRandomPoemNotificationTime(
    private val settingRepository: SettingRepository,
) : UseCaseWithParams<Unit, SetRandomPoemNotificationTimeParams> {
    override suspend fun invoke(params: SetRandomPoemNotificationTimeParams) {
        settingRepository.setRandomPoemNotificationTime(params.timeOfDay)
    }
}
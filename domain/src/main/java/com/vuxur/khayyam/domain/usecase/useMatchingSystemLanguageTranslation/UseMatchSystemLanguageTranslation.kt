package com.vuxur.khayyam.domain.usecase.useMatchingSystemLanguageTranslation

import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.base.UseCase

class UseMatchSystemLanguageTranslation(
    private val settingRepository: SettingRepository
) : UseCase<Unit> {
    override suspend fun invoke() {
        settingRepository.useMatchSystemLanguageTranslation()
    }
}
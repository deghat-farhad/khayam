package com.vuxur.khayyam.domain.usecase.useSpecificTranslation

import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams

class UseSpecificTranslation(
    private val settingRepository: SettingRepository
) : UseCaseWithParams<Unit, UseSpecificTranslationParams> {
    override suspend fun invoke(params: UseSpecificTranslationParams) {
        settingRepository.useSpecificTranslation(params.translationOption)
    }
}
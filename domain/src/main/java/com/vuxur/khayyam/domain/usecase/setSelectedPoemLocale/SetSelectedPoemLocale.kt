package com.vuxur.khayyam.domain.usecase.setSelectedPoemLocale

import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams

class SetSelectedPoemLocale(
    val settingRepository: SettingRepository
) : UseCaseWithParams<Unit, SetSelectedPoemLocaleParams> {
    override suspend fun invoke(params: SetSelectedPoemLocaleParams) {
        settingRepository.setSelectedPoemLocale(params.locale)
    }
}
package com.vuxur.khayyam.domain.usecase.setSelectedPoemLocale

import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SetSelectedPoemLocale(
    val settingRepository: SettingRepository
) : UseCaseWithParams<Unit, SetSelectedPoemLocaleParams> {
    override suspend fun invoke(params: SetSelectedPoemLocaleParams): Flow<Unit> {
        return flowOf(settingRepository.setSelectedPoemLocale(params.locale))
    }
}
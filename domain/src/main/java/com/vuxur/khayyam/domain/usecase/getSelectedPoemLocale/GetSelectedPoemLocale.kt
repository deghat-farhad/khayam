package com.vuxur.khayyam.domain.usecase.getSelectedPoemLocale

import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow

class GetSelectedPoemLocale(
    private val settingRepository: SettingRepository
) : UseCase<Flow<Locale>> {
    override suspend fun invoke(): Flow<Locale> {
        return settingRepository.selectedPoemLocale
    }
}
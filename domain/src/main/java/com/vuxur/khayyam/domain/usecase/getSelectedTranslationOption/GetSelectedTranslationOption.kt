package com.vuxur.khayyam.domain.usecase.getSelectedTranslationOption

import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow

class GetSelectedTranslationOption(
    private val settingRepository: SettingRepository
) : UseCase<Flow<TranslationOptions>> {
    override suspend fun invoke(): Flow<TranslationOptions> {
        return settingRepository.selectedTranslationOption
    }
}
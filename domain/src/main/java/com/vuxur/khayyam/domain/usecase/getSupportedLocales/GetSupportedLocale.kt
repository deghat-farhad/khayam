package com.vuxur.khayyam.domain.usecase.getSupportedLocales

import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.repository.LocaleRepository
import com.vuxur.khayyam.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetSupportedLocale(
    private val localeRepository: LocaleRepository
) : UseCase<List<Locale>> {
    override suspend fun invoke(): Flow<List<Locale>> {
        return flowOf(localeRepository.getSupportedLanguages())
    }
}
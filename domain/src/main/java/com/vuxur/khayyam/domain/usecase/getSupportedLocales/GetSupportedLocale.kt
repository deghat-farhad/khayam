package com.vuxur.khayyam.domain.usecase.getSupportedLocales

import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.repository.LocaleRepository
import com.vuxur.khayyam.domain.usecase.base.UseCase

class GetSupportedLocale(
    private val localeRepository: LocaleRepository
) : UseCase<List<Locale.CustomLocale>> {
    override suspend fun invoke(): List<Locale.CustomLocale> {
        val supportedLocales = localeRepository.getSupportedLanguages().toMutableList()
        return supportedLocales
    }
}
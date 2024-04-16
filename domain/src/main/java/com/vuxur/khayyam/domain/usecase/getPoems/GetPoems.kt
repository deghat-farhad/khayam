package com.vuxur.khayyam.domain.usecase.getPoems

import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams
import com.vuxur.khayyam.domain.usecase.getSupportedLocales.GetSupportedLocale

class GetPoems(
    private val poemRepository: PoemRepository,
    private val getSupportedLocale: GetSupportedLocale,
) : UseCaseWithParams<List<Poem>, GetPoemsParams> {
    override suspend fun invoke(params: GetPoemsParams): List<Poem> {
        val supportedLocales = getSupportedLocale()
        val locale: Locale.CustomLocale =
            supportedLocales.filterIsInstance<Locale.CustomLocale>()
                .firstOrNull { it == params.locale }
                ?: supportedLocales.filterIsInstance<Locale.CustomLocale>().first()
        return poemRepository.getPoems(locale)
    }
}

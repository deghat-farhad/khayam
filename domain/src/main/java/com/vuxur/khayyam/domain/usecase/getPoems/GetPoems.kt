package com.vuxur.khayyam.domain.usecase.getPoems

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.model.Translation
import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams
import com.vuxur.khayyam.domain.usecase.getTranslations.GetAvailableTranslations

const val UNTRANSLATED_LANGUAGE_TAG = "fa-IR"
const val FALLBACK_LANGUAGE_TAG = "en-US"

class GetPoems(
    private val poemRepository: PoemRepository,
    private val getAvailableTranslations: GetAvailableTranslations,
) : UseCaseWithParams<List<Poem>, GetPoemsParams> {
    override suspend fun invoke(params: GetPoemsParams): List<Poem> {
        val translations = getAvailableTranslations()
        val fallBackTranslation =
            translations.firstOrNull { it.languageTag == FALLBACK_LANGUAGE_TAG }
                ?: throw NoSuchElementException("Fallback translation not found.")
        val untranslated = translations.firstOrNull { it.languageTag == UNTRANSLATED_LANGUAGE_TAG }
            ?: throw NoSuchElementException("Untranslated option not found.")

        return (
            poemRepository.getPoems(
                when (params.translationOptions) {
                    is TranslationOptions.MatchDeviceLanguage ->
                        if (params.translationOptions.translation.isAvailable())
                            params.translationOptions.translation
                        else
                            fallBackTranslation

                    TranslationOptions.None ->
                        throw IllegalArgumentException("\"params.translationOption\" should not be \"TranslationOptions.None\".")

                    is TranslationOptions.Specific ->
                        if (params.translationOptions.translation.isAvailable())
                            params.translationOptions.translation
                        else
                            throw NoSuchElementException("the translation ${params.translationOptions.translation} is not available.")

                    TranslationOptions.Untranslated ->
                        untranslated
                }
            )
            )
    }

    private suspend fun Translation.isAvailable() =
        getAvailableTranslations().contains(this)
}

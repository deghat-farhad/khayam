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
) : UseCaseWithParams<Result<List<Poem>>, GetPoemsParams> {
    override suspend fun invoke(params: GetPoemsParams): Result<List<Poem>> {
        val translations = getAvailableTranslations()
        val fallBackTranslation =
            translations.first { it.languageTag == FALLBACK_LANGUAGE_TAG }

        val untranslated = translations.first { it.languageTag == UNTRANSLATED_LANGUAGE_TAG }

        return (
            when (params.translationOptions) {
                is TranslationOptions.MatchDeviceLanguage ->
                    Result.success(
                        poemRepository.getPoems(
                            if (params.translationOptions.translation.isAvailable())
                                params.translationOptions.translation
                            else
                                fallBackTranslation
                        )
                    )

                TranslationOptions.None ->
                    Result.failure(
                        exception = IllegalArgumentException(
                            "\"params.translationOption\" should not be \"TranslationOptions.None\"."
                        )
                    )

                is TranslationOptions.Specific ->
                    if (params.translationOptions.translation.isAvailable())
                        Result.success(
                            poemRepository.getPoems(
                                params.translationOptions.translation
                            )
                        )
                    else
                        Result.failure(
                            exception = NoSuchElementException(
                                "the translation ${params.translationOptions.translation} is not available."
                            )
                        )

                TranslationOptions.Untranslated ->
                    Result.success(
                        poemRepository.getPoems(
                            untranslated
                        )
                    )
            }
            )
    }

    private suspend fun Translation.isAvailable() =
        getAvailableTranslations().contains(this)
}

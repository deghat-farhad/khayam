package com.vuxur.khayyam.domain.usecase.getPoems

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams

class GetPoems(
    private val poemRepository: PoemRepository,
) : UseCaseWithParams<List<Poem>, GetPoemsParams> {
    override suspend fun invoke(params: GetPoemsParams): List<Poem> {
        return (
            poemRepository.getPoems(
                when (params.translationOptions) {
                    is TranslationOptions.MatchDeviceLanguage.Available ->
                        params.translationOptions.translation

                    is TranslationOptions.MatchDeviceLanguage.Unavailable ->
                        params.translationOptions.fallBackTranslation

                    TranslationOptions.None ->
                        throw IllegalArgumentException("\"params.translationOption\" should not be \"TranslationOptions.None\".")

                    is TranslationOptions.Specific ->
                        params.translationOptions.translation

                    is TranslationOptions.Untranslated ->
                        params.translationOptions.translation
                }
            )
            )
    }
}

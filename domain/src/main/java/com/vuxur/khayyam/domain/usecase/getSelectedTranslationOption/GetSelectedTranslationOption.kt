package com.vuxur.khayyam.domain.usecase.getSelectedTranslationOption

import com.vuxur.khayyam.domain.model.Translation
import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.domain.model.TranslationPreferences
import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.repository.TranslationRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val UNTRANSLATED_LANGUAGE_TAG = "fa-IR"
const val FALLBACK_LANGUAGE_TAG = "en-US"

class GetSelectedTranslationOption(
    private val settingRepository: SettingRepository,
    private val translationRepository: TranslationRepository,
) : UseCaseWithParams<Flow<TranslationOptions>, GetSelectedTranslationOptionParams> {
    override suspend fun invoke(params: GetSelectedTranslationOptionParams): Flow<TranslationOptions> {
        return settingRepository.translationPreferences.map { translationPreferences ->
            when (translationPreferences) {
                TranslationPreferences.MatchDeviceLanguageTranslation -> {
                    val systemLanguageTag =
                        params.deviceLocale.toLanguageTag()
                    TranslationOptions.MatchDeviceLanguage.Available(
                        translationRepository.getTranslationsWithLanguageTag(systemLanguageTag)
                            .firstOrNull()
                            ?: getFallBackTranslation(),
                    )
                }

                TranslationPreferences.None ->
                    TranslationOptions.None

                is TranslationPreferences.SpecificTranslation ->
                    TranslationOptions.Specific(
                        translationRepository.getTranslationWithId(translationPreferences.id)
                    )

                is TranslationPreferences.Untranslated ->
                    TranslationOptions.Untranslated(getUntranslatedTranslation())
            }
        }
    }

    private suspend fun getFallBackTranslation(): Translation {
        val translations = translationRepository.getAvailableTranslations()
        return translations.firstOrNull { it.languageTag == FALLBACK_LANGUAGE_TAG }
            ?: throw FallbackTranslationNotFoundException("Fallback translation not found.")
    }

    private suspend fun getUntranslatedTranslation(): Translation {
        val translations = translationRepository.getAvailableTranslations()
        return translations.firstOrNull { it.languageTag == UNTRANSLATED_LANGUAGE_TAG }
            ?: throw UntranslatedTranslationNotFoundException("Untranslated translation          not found.")
    }
}

class FallbackTranslationNotFoundException(message: String) : Exception(message)
class UntranslatedTranslationNotFoundException(message: String) : Exception(message)
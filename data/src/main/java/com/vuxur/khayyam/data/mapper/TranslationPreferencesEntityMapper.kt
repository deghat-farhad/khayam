package com.vuxur.khayyam.data.mapper

import com.vuxur.khayyam.data.entity.TranslationPreferencesEntity
import com.vuxur.khayyam.domain.model.TranslationPreferences
import javax.inject.Inject

class TranslationPreferencesEntityMapper @Inject constructor() {
    fun mapToDomain(translationPreferencesEntity: TranslationPreferencesEntity) =
        when (translationPreferencesEntity) {
            TranslationPreferencesEntity.MatchDeviceLanguageTranslation -> TranslationPreferences.MatchDeviceLanguageTranslation
            TranslationPreferencesEntity.None -> TranslationPreferences.None
            is TranslationPreferencesEntity.SpecificTranslation -> TranslationPreferences.SpecificTranslation(
                id = translationPreferencesEntity.id
            )

            TranslationPreferencesEntity.Untranslated -> TranslationPreferences.Untranslated
        }
}
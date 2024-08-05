package com.vuxur.khayyam.data.entity

sealed class TranslationPreferencesEntity {
    data object Untranslated : TranslationPreferencesEntity()
    data object MatchDeviceLanguageTranslation : TranslationPreferencesEntity()
    data object None : TranslationPreferencesEntity()
    data class SpecificTranslation(
        val id: Int
    ) : TranslationPreferencesEntity()
}
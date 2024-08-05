package com.vuxur.khayyam.domain.model

sealed class TranslationPreferences {
    data object Untranslated : TranslationPreferences()
    data object MatchDeviceLanguageTranslation : TranslationPreferences()
    data object None : TranslationPreferences()
    data class SpecificTranslation(
        val id: Int
    ) : TranslationPreferences()
}
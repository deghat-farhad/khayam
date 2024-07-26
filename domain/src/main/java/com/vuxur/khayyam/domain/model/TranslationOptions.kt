package com.vuxur.khayyam.domain.model

sealed class TranslationOptions {

    data object None : TranslationOptions()

    data object Untranslated : TranslationOptions()

    data class MatchDeviceLanguage(
        val translation: Translation
    ) : TranslationOptions()

    data class Specific(
        val translation: Translation
    ) : TranslationOptions()
}
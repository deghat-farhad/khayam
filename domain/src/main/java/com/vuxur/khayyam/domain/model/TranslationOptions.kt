package com.vuxur.khayyam.domain.model

sealed class TranslationOptions {

    data object None : TranslationOptions()

    data class Untranslated(
        val translation: Translation
    ) : TranslationOptions()

    sealed class MatchDeviceLanguage : TranslationOptions() {
        data class Available(
            val translation: Translation
        ) : MatchDeviceLanguage()

        data class Unavailable(
            val fallBackTranslation: Translation
        ) : MatchDeviceLanguage()
    }

    data class Specific(
        val translation: Translation
    ) : TranslationOptions()
}
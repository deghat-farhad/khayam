package com.vuxur.khayyam.model

sealed class TranslationOptionsItem {
    data object None : TranslationOptionsItem()

    data class Untranslated(
        val translation: TranslationItem
    ) : TranslationOptionsItem()

    sealed class MatchDeviceLanguage : TranslationOptionsItem() {
        data class Available(
            val translation: TranslationItem
        ) : MatchDeviceLanguage()

        data class Unavailable(
            val fallBackTranslation: TranslationItem
        ) : MatchDeviceLanguage()
    }

    data class Specific(
        val translation: TranslationItem
    ) : TranslationOptionsItem()
}
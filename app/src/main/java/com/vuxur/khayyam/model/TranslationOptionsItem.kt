package com.vuxur.khayyam.model

sealed class TranslationOptionsItem {
    data object None : TranslationOptionsItem()

    data object Untranslated : TranslationOptionsItem()

    data class MatchDeviceLanguage(
        val translation: TranslationItem
    ) : TranslationOptionsItem()

    data class Specific(
        val translation: TranslationItem
    ) : TranslationOptionsItem()
}
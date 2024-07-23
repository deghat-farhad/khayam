package com.vuxur.khayyam.data.entity

sealed class TranslationOptionsEntity {
    data object None : TranslationOptionsEntity()

    data object Untranslated : TranslationOptionsEntity()

    data class MatchDeviceLanguage(
        val translationEntity: TranslationEntity
    ) : TranslationOptionsEntity()

    data class Specific(
        val translationEntity: TranslationEntity
    ) : TranslationOptionsEntity()
}

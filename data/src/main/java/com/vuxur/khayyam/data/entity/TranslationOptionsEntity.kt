package com.vuxur.khayyam.data.entity

sealed class TranslationOptionsEntity {
    data object None : TranslationOptionsEntity()

    data class Untranslated(
        val translationEntity: TranslationEntity
    ) : TranslationOptionsEntity()

    sealed class MatchDeviceLanguage : TranslationOptionsEntity() {
        data class Available(
            val translationEntity: TranslationEntity
        ) : MatchDeviceLanguage()

        data class Unavailable(
            val fallBackTranslationEntity: TranslationEntity
        ) : MatchDeviceLanguage()
    }

    data class Specific(
        val translationEntity: TranslationEntity
    ) : TranslationOptionsEntity()
}

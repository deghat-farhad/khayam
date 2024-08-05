package com.vuxur.khayyam.data.mapper

import com.vuxur.khayyam.data.entity.TranslationOptionsEntity
import com.vuxur.khayyam.domain.model.TranslationOptions
import javax.inject.Inject

class TranslationOptionsEntityMapper @Inject constructor(
    private val translationEntityMapper: TranslationEntityMapper,
) {
    fun mapToDomain(translationOptionsEntity: TranslationOptionsEntity) =
        when (translationOptionsEntity) {
            TranslationOptionsEntity.None -> TranslationOptions.None
            is TranslationOptionsEntity.Untranslated -> mapToDomain(translationOptionsEntity)
            is TranslationOptionsEntity.Specific -> mapToDomain(translationOptionsEntity)
            is TranslationOptionsEntity.MatchDeviceLanguage -> mapToDomain(translationOptionsEntity)
        }

    private fun mapToDomain(untranslated: TranslationOptionsEntity.Untranslated) =
        TranslationOptions.Untranslated(
            translationEntityMapper.mapToDomain(
                untranslated.translationEntity
            )
        )

    fun mapToDomain(specificTranslationList: List<TranslationOptionsEntity.Specific>) =
        specificTranslationList.map { mapToDomain(it) }

    fun mapToDomain(specificTranslation: TranslationOptionsEntity.Specific) =
        TranslationOptions.Specific(
            translationEntityMapper.mapToDomain(
                specificTranslation.translationEntity
            )
        )

    fun mapToDomain(matchDeviceLanguageTranslation: TranslationOptionsEntity.MatchDeviceLanguage) =
        when (matchDeviceLanguageTranslation) {
            is TranslationOptionsEntity.MatchDeviceLanguage.Available -> TranslationOptions.MatchDeviceLanguage.Available(
                translationEntityMapper.mapToDomain(
                    matchDeviceLanguageTranslation.translationEntity
                )
            )

            is TranslationOptionsEntity.MatchDeviceLanguage.Unavailable -> TranslationOptions.MatchDeviceLanguage.Unavailable(
                translationEntityMapper.mapToDomain(
                    matchDeviceLanguageTranslation.fallBackTranslationEntity
                )
            )
        }

    fun mapToData(specificTranslation: TranslationOptions.Specific) =
        TranslationOptionsEntity.Specific(
            translationEntityMapper.mapToData(
                specificTranslation.translation
            )
        )
}
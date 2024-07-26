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
            TranslationOptionsEntity.Untranslated -> TranslationOptions.Untranslated
            is TranslationOptionsEntity.Specific -> mapToDomain(translationOptionsEntity)
            is TranslationOptionsEntity.MatchDeviceLanguage -> mapToDomain(translationOptionsEntity)
        }

    fun mapToDomain(specificTranslationList: List<TranslationOptionsEntity.Specific>) =
        specificTranslationList.map { mapToDomain(it) }

    fun mapToDomain(specificTranslation: TranslationOptionsEntity.Specific) =
        TranslationOptions.Specific(
            translationEntityMapper.mapToDomain(
                specificTranslation.translationEntity
            )
        )

    fun mapToDomain(matchDeviceLanguageTranslation: TranslationOptionsEntity.MatchDeviceLanguage) =
        TranslationOptions.MatchDeviceLanguage(
            translationEntityMapper.mapToDomain(
                matchDeviceLanguageTranslation.translationEntity
            )
        )

    fun mapToData(specificTranslation: TranslationOptions.Specific) =
        TranslationOptionsEntity.Specific(
            translationEntityMapper.mapToData(
                specificTranslation.translation
            )
        )
}
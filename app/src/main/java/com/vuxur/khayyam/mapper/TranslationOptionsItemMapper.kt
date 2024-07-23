package com.vuxur.khayyam.mapper

import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.model.TranslationOptionsItem
import javax.inject.Inject

class TranslationOptionsItemMapper @Inject constructor(
    private val translationItemMapper: TranslationItemMapper
) {
    fun mapToPresentation(translationOptions: TranslationOptions) =
        when (translationOptions) {
            TranslationOptions.None -> TranslationOptionsItem.None
            TranslationOptions.Untranslated -> TranslationOptionsItem.Untranslated
            is TranslationOptions.Specific -> mapToPresentation(translationOptions)
            is TranslationOptions.MatchDeviceLanguage -> mapToPresentation(translationOptions)
        }

    fun mapToPresentation(specificTranslation: TranslationOptions.Specific) =
        TranslationOptionsItem.Specific(
            translationItemMapper.mapToPresentation(specificTranslation.translation)
        )

    fun mapToPresentation(matchDeviseLanguageTranslation: TranslationOptions.MatchDeviceLanguage) =
        TranslationOptionsItem.MatchDeviceLanguage(
            translationItemMapper.mapToPresentation(matchDeviseLanguageTranslation.translation)
        )

    fun mapToPresentation(translationOptionsList: List<TranslationOptions>) =
        translationOptionsList.map { translationOption -> mapToPresentation(translationOption) }

    fun mapToDomain(translationOptionsItem: TranslationOptionsItem) =
        when (translationOptionsItem) {
            TranslationOptionsItem.None -> TranslationOptions.None
            TranslationOptionsItem.Untranslated -> TranslationOptions.Untranslated
            is TranslationOptionsItem.Specific -> mapToDomain(translationOptionsItem)
            is TranslationOptionsItem.MatchDeviceLanguage -> mapToDomain(translationOptionsItem)
        }

    fun mapToDomain(specificTranslationItem: TranslationOptionsItem.Specific) =
        TranslationOptions.Specific(
            translationItemMapper.mapToDomain(specificTranslationItem.translation)
        )

    fun mapToDomain(matchDeviseLanguageTranslationItem: TranslationOptionsItem.MatchDeviceLanguage) =
        TranslationOptions.MatchDeviceLanguage(
            translationItemMapper.mapToDomain(matchDeviseLanguageTranslationItem.translation)
        )

    fun mapToDomain(translationOptionsItems: List<TranslationOptionsItem>) =
        translationOptionsItems.map { translationOptionsItem -> mapToDomain(translationOptionsItem) }
}
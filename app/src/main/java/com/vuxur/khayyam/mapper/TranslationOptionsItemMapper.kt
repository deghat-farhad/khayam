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
            is TranslationOptions.Untranslated -> mapToPresentation(translationOptions)
            is TranslationOptions.Specific -> mapToPresentation(translationOptions)
            is TranslationOptions.MatchDeviceLanguage -> mapToPresentation(translationOptions)
        }

    private fun mapToPresentation(untranslated: TranslationOptions.Untranslated) =
        TranslationOptionsItem.Untranslated(
            translationItemMapper.mapToPresentation(
                untranslated.translation
            )
        )

    private fun mapToPresentation(specificTranslation: TranslationOptions.Specific) =
        TranslationOptionsItem.Specific(
            translationItemMapper.mapToPresentation(specificTranslation.translation)
        )

    private fun mapToPresentation(matchDeviseLanguageTranslation: TranslationOptions.MatchDeviceLanguage) =
        when (matchDeviseLanguageTranslation) {
            is TranslationOptions.MatchDeviceLanguage.Available ->
                TranslationOptionsItem.MatchDeviceLanguage.Available(
                    translationItemMapper.mapToPresentation(matchDeviseLanguageTranslation.translation)
                )

            is TranslationOptions.MatchDeviceLanguage.Unavailable ->
                TranslationOptionsItem.MatchDeviceLanguage.Unavailable(
                    translationItemMapper.mapToPresentation(matchDeviseLanguageTranslation.fallBackTranslation)
                )
        }

    fun mapToDomain(translationOptionsItem: TranslationOptionsItem) =
        when (translationOptionsItem) {
            TranslationOptionsItem.None -> TranslationOptions.None
            is TranslationOptionsItem.Untranslated -> mapToDomain(translationOptionsItem)
            is TranslationOptionsItem.Specific -> mapToDomain(translationOptionsItem)
            is TranslationOptionsItem.MatchDeviceLanguage -> mapToDomain(translationOptionsItem)
        }

    private fun mapToDomain(untranslated: TranslationOptionsItem.Untranslated) =
        TranslationOptions.Untranslated(
            translationItemMapper.mapToDomain(
                untranslated.translation
            )
        )

    private fun mapToDomain(specificTranslationItem: TranslationOptionsItem.Specific) =
        TranslationOptions.Specific(
            translationItemMapper.mapToDomain(specificTranslationItem.translation)
        )

    private fun mapToDomain(matchDeviseLanguageTranslationItem: TranslationOptionsItem.MatchDeviceLanguage) =
        when (matchDeviseLanguageTranslationItem) {
            is TranslationOptionsItem.MatchDeviceLanguage.Available ->
                TranslationOptions.MatchDeviceLanguage.Available(
                    translationItemMapper.mapToDomain(matchDeviseLanguageTranslationItem.translation)
                )

            is TranslationOptionsItem.MatchDeviceLanguage.Unavailable ->
                TranslationOptions.MatchDeviceLanguage.Unavailable(
                    translationItemMapper.mapToDomain(matchDeviseLanguageTranslationItem.fallBackTranslation)
                )
        }
}
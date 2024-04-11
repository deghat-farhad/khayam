package com.vuxur.khayyam.mapper

import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.model.LocaleItem
import javax.inject.Inject

class LocaleItemMapper @Inject constructor() {
    fun mapToPresentation(locale: Locale) = when (locale) {
        is Locale.CustomLocale -> mapToPresentation(locale)
        Locale.SystemLocale -> LocaleItem.SystemLocale
        Locale.NoLocale -> LocaleItem.NoLocale
    }

    fun mapToDomain(localeItem: LocaleItem) = when (localeItem) {
        is LocaleItem.CustomLocale -> mapToDomain(localeItem)
        LocaleItem.SystemLocale -> Locale.SystemLocale
        LocaleItem.NoLocale -> Locale.NoLocale
    }

    fun mapToDomain(customLocaleItem: LocaleItem.CustomLocale) =
        Locale.CustomLocale(customLocaleItem.locale)

    fun mapToPresentation(customLocale: Locale.CustomLocale) =
        LocaleItem.CustomLocale(customLocale.locale)

    fun mapToPresentation(locales: List<Locale>) =
        locales.map { locale ->
            when (locale) {
                is Locale.CustomLocale -> LocaleItem.CustomLocale(locale.locale)
                Locale.NoLocale -> LocaleItem.NoLocale
                Locale.SystemLocale -> LocaleItem.SystemLocale
            }
        }
}
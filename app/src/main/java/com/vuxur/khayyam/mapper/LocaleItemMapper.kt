package com.vuxur.khayyam.mapper

import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.model.LocaleItem
import javax.inject.Inject

class LocaleItemMapper @Inject constructor() {
    fun mapToPresentation(locale: Locale) = when (locale) {
        is Locale.SelectedLocale -> mapToPresentation(locale)
        Locale.SystemLocale -> LocaleItem.SystemLocale
        Locale.NoLocale -> LocaleItem.NoLocale
    }

    fun mapToDomain(localeItem: LocaleItem) = when (localeItem) {
        is LocaleItem.SelectedLocale -> mapToDomain(localeItem)
        LocaleItem.SystemLocale -> Locale.SystemLocale
        LocaleItem.NoLocale -> Locale.NoLocale
    }

    fun mapToDomain(selectedLocaleItem: LocaleItem.SelectedLocale) =
        Locale.SelectedLocale(selectedLocaleItem.locale)

    fun mapToPresentation(selectedLocale: Locale.SelectedLocale) =
        LocaleItem.SelectedLocale(selectedLocale.locale)
}
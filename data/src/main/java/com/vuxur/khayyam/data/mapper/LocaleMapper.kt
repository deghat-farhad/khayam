package com.vuxur.khayyam.data.mapper

import com.vuxur.khayyam.data.entity.LocaleEntity
import com.vuxur.khayyam.domain.model.Locale
import javax.inject.Inject

class LocaleMapper @Inject constructor() {
    fun mapToDomain(localeEntity: LocaleEntity) = when (localeEntity) {
        is LocaleEntity.SelectedLocale -> Locale.SelectedLocale(localeEntity.locale)
        LocaleEntity.SystemLocale -> Locale.SystemLocale
        LocaleEntity.NoLocale -> Locale.NoLocale
    }

    fun mapToDomain(localeEntities: List<LocaleEntity>) =
        localeEntities.map { mapToDomain(it) }

    fun mapToData(locale: Locale) = when (locale) {
        is Locale.SelectedLocale -> mapToData(locale)
        Locale.SystemLocale -> LocaleEntity.SystemLocale
        Locale.NoLocale -> LocaleEntity.NoLocale
    }

    fun mapToData(locale: Locale.SelectedLocale) = LocaleEntity.SelectedLocale(locale.locale)
}
package com.vuxur.khayyam.data.mapper

import com.vuxur.khayyam.data.entity.LanguageTagEntity
import com.vuxur.khayyam.data.entity.LocaleEntity
import javax.inject.Inject

class LanguageTagEntityMapper @Inject constructor() {
    fun mapToLocaleEntity(languageTagEntity: LanguageTagEntity) =
        LocaleEntity.SelectedLocale(
            locale = java.util.Locale.forLanguageTag(languageTagEntity.languageTag)
        )

    fun mapToLocaleEntity(languageTagEntities: List<LanguageTagEntity>) =
        languageTagEntities.map { mapToLocaleEntity(it) }
}
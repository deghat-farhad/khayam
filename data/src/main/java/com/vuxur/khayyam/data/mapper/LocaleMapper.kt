package com.vuxur.khayyam.data.mapper

import com.vuxur.khayyam.data.entity.LocaleEntity
import com.vuxur.khayyam.domain.model.Locale
import javax.inject.Inject

class LocaleMapper @Inject constructor() {
    fun mapToDomain(localeEntity: LocaleEntity) =
        Locale(
            localeEntity.locale
        )

    fun mapToDomain(localeEntities: List<LocaleEntity>) =
        localeEntities.map { mapToDomain(it) }
}
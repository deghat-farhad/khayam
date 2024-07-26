package com.vuxur.khayyam.data.mapper

import com.vuxur.khayyam.data.entity.TranslationEntity
import com.vuxur.khayyam.domain.model.Translation
import javax.inject.Inject

class TranslationEntityMapper @Inject constructor() {
    fun mapToDomain(translationEntityList: List<TranslationEntity>) =
        translationEntityList.map { translationEntity ->
            mapToDomain(translationEntity)
        }

    fun mapToDomain(translationEntity: TranslationEntity) =
        Translation(
            translationEntity.id,
            translationEntity.languageTag,
            translationEntity.translator,
        )

    fun mapToData(translation: Translation) =
        TranslationEntity(
            translation.id,
            translation.languageTag,
            translation.translator,
        )
}
package com.vuxur.khayyam.data.mapper

import com.vuxur.khayyam.data.entity.PoemEntity
import com.vuxur.khayyam.data.entity.PoemWithTranslationEntity
import com.vuxur.khayyam.domain.model.Poem
import javax.inject.Inject

class PoemMapper @Inject constructor(
    private val translationEntityMapper: TranslationEntityMapper
) {
    fun mapToDomain(poemWithTranslationEntity: PoemWithTranslationEntity) =
        Poem(
            poemWithTranslationEntity.poem.id,
            poemWithTranslationEntity.poem.index,
            poemWithTranslationEntity.poem.hemistich1,
            poemWithTranslationEntity.poem.hemistich2,
            poemWithTranslationEntity.poem.hemistich3,
            poemWithTranslationEntity.poem.hemistich4,
            poemWithTranslationEntity.poem.isSuspicious,
            translationEntityMapper.mapToDomain(poemWithTranslationEntity.translation),
        )

    fun mapToDomain(poemWithTranslationEntityList: List<PoemWithTranslationEntity>) =
        poemWithTranslationEntityList.map { mapToDomain(it) }

    fun mapToData(poem: Poem) =
        PoemWithTranslationEntity(
            PoemEntity(
                poem.id,
                poem.index,
                poem.hemistich1,
                poem.hemistich2,
                poem.hemistich3,
                poem.hemistich4,
                poem.isSuspicious,
                poem.translation.id,
            ),
            translationEntityMapper.mapToData(poem.translation)
        )
}
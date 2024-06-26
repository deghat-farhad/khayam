package com.vuxur.khayyam.data.mapper

import com.vuxur.khayyam.data.entity.PoemEntity
import com.vuxur.khayyam.domain.model.Poem
import javax.inject.Inject

class PoemMapper @Inject constructor() {
    fun mapToDomain(poemEntity: PoemEntity) =
        Poem(
            poemEntity.id,
            poemEntity.index,
            poemEntity.hemistich1,
            poemEntity.hemistich2,
            poemEntity.hemistich3,
            poemEntity.hemistich4,
            poemEntity.isSuspicious,
            poemEntity.language
        )

    fun mapToDomain(poemEntityList: List<PoemEntity>) = poemEntityList.map { mapToDomain(it) }

    fun mapToData(poem: Poem) =
        PoemEntity(
            poem.id,
            poem.index,
            poem.hemistich1,
            poem.hemistich2,
            poem.hemistich3,
            poem.hemistich4,
            poem.isSuspicious,
            poem.language,
        )
}
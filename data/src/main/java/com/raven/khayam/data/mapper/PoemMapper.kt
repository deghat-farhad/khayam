package com.raven.khayam.data.mapper

import com.raven.khayam.data.entity.PoemEntity
import com.raven.khayam.domain.model.Poem

class PoemMapper {
    fun mapToDomain(poemEntity: PoemEntity) =
        Poem(
            poemEntity.id,
            poemEntity.hemistich1,
            poemEntity.hemistich2,
            poemEntity.hemistich3,
            poemEntity.hemistich4,
            poemEntity.isSuspicious,
            poemEntity.category
        )

    fun mapToDomain(poemEntityList: List<PoemEntity>) = poemEntityList.map { mapToDomain(it) }
}
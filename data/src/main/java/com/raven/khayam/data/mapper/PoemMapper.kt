package com.raven.khayam.data.mapper

import com.raven.khayam.data.entity.PoemEntity
import com.raven.khayam.domain.model.Poem

class PoemMapper {
    fun mapToDomain(poemEntity: PoemEntity) =
        Poem(poemEntity.id, poemEntity.text, poemEntity.isSuspicious, poemEntity.category)

    fun mapToDomain(poemEntityList: List<PoemEntity>) = poemEntityList.map { mapToDomain(it) }
}
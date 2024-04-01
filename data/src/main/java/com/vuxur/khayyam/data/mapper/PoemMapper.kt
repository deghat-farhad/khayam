package com.vuxur.khayyam.data.mapper

import com.vuxur.khayyam.data.entity.PoemEntity
import com.vuxur.khayyam.domain.model.Poem

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
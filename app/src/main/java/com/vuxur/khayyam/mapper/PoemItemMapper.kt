package com.vuxur.khayyam.mapper

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.model.PoemItem
import javax.inject.Inject

class PoemItemMapper @Inject constructor(
    private val translationItemMapper: TranslationItemMapper
) {
    fun mapToPresentation(poem: Poem) = PoemItem(
        poem.id,
        poem.index,
        poem.hemistich1,
        poem.hemistich2,
        poem.hemistich3,
        poem.hemistich4,
        poem.isSuspicious,
        translationItemMapper.mapToPresentation(poem.translation),
    )

    fun mapToPresentation(poemList: List<Poem>) = poemList.map { mapToPresentation(it) }
    fun mapToDomain(poemItem: PoemItem) = Poem(
        poemItem.id,
        poemItem.index,
        poemItem.hemistich1,
        poemItem.hemistich2,
        poemItem.hemistich3,
        poemItem.hemistich4,
        poemItem.isSuspicious,
        translationItemMapper.mapToDomain(poemItem.translation)
    )
}
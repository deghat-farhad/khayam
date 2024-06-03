package com.vuxur.khayyam.mapper

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.model.PoemItem
import javax.inject.Inject

class PoemItemMapper @Inject constructor() {
    fun mapToPresentation(poem: Poem) = PoemItem(
        poem.id,
        poem.index,
        poem.hemistich1,
        poem.hemistich2,
        poem.hemistich3,
        poem.hemistich4,
        poem.isSuspicious,
        poem.category
    )

    fun mapToPresentation(poemList: List<Poem>) = poemList.map { mapToPresentation(it) }
}
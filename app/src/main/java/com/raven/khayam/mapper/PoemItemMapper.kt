package com.raven.khayam.mapper

import com.raven.khayam.domain.model.Poem
import com.raven.khayam.model.PoemItem
import javax.inject.Inject

class PoemItemMapper @Inject constructor() {
    fun mapToPresentation(poem: Poem) = PoemItem(poem.id, poem.text, poem.isSuspicious, poem.category)
    fun mapToPresentation(poemList: List<Poem>) = poemList.map { mapToPresentation(it) }
}
package com.vuxur.khayyam.domain.repository

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.model.Translation

interface PoemRepository {
    suspend fun getPoems(translation: Translation): List<Poem>
    suspend fun findPoems(searchPhrase: String, translation: Translation): List<Poem>
    suspend fun getRandomPoem(translation: Translation): Poem
}
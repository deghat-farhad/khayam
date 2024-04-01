package com.vuxur.khayyam.domain.repository

import com.vuxur.khayyam.domain.model.Poem
import kotlinx.coroutines.flow.Flow

interface PoemRepository {
    fun getPoems(): Flow<List<Poem>>
    fun findPoems(searchPhrase: String): Flow<List<Poem>>
}
package com.raven.khayam.domain.repository

import com.raven.khayam.domain.model.Poem
import kotlinx.coroutines.flow.Flow

interface PoemRepository {
    fun getPoems(): Flow<List<Poem>>
    fun findPoems(searchPhrase: String): Flow<List<Poem>>
}
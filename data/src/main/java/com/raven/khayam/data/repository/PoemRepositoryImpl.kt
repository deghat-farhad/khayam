package com.raven.khayam.data.repository

import com.raven.khayam.data.local.Local
import com.raven.khayam.data.mapper.PoemMapper
import com.raven.khayam.domain.model.Poem
import com.raven.khayam.domain.repository.PoemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PoemRepositoryImpl(
    private val local: Local,
    private val poemMapper: PoemMapper
) :
    PoemRepository {
    override fun getPoems(): Flow<List<Poem>> {
        return local.getPoems().map { poemMapper.mapToDomain(it) }
    }

    override fun findPoems(searchPhrase: String): Flow<List<Poem>> {
        return local.findPoems(searchPhrase).map {poemMapper.mapToDomain(it)}
    }
}
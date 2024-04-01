package com.vuxur.khayyam.data.repository

import com.vuxur.khayyam.data.local.Local
import com.vuxur.khayyam.data.mapper.PoemMapper
import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.repository.PoemRepository
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
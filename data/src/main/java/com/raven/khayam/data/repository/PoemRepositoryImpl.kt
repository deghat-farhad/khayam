package com.raven.khayam.data.repository

import com.raven.khayam.data.local.Local
import com.raven.khayam.data.mapper.PoemMapper
import com.raven.khayam.domain.model.Poem
import com.raven.khayam.domain.repository.PoemRepository
import io.reactivex.Observable

class PoemRepositoryImpl(
    private val local: Local,
    private val poemMapper: PoemMapper
) :
    PoemRepository {
    override fun getPoems(): Observable<List<Poem>> {
        return local.getPoems().map { poemMapper.mapToDomain(it) }
    }
}
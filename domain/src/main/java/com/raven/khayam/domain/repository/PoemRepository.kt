package com.raven.khayam.domain.repository

import com.raven.khayam.domain.model.Poem
import io.reactivex.Observable

interface PoemRepository {
    fun getPoems(): Observable<List<Poem>>
    fun findPoems(searchPhrase: String): Observable<List<Poem>>
}
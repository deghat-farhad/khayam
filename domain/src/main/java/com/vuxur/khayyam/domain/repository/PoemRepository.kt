package com.vuxur.khayyam.domain.repository

import com.vuxur.khayyam.domain.model.Poem
import kotlinx.coroutines.flow.Flow
import java.util.Locale

interface PoemRepository {
    fun getPoems(locale: Locale): Flow<List<Poem>>
    fun findPoems(searchPhrase: String): Flow<List<Poem>>
}
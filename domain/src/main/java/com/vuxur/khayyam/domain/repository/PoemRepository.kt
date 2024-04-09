package com.vuxur.khayyam.domain.repository

import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.model.Poem
import kotlinx.coroutines.flow.Flow

interface PoemRepository {
    fun getPoems(locale: Locale.SelectedLocale): Flow<List<Poem>>
    fun findPoems(searchPhrase: String): Flow<List<Poem>>
}
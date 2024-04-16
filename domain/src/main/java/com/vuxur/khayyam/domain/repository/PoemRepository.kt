package com.vuxur.khayyam.domain.repository

import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.model.Poem

interface PoemRepository {
    suspend fun getPoems(locale: Locale.CustomLocale): List<Poem>
    suspend fun findPoems(searchPhrase: String, locale: Locale.CustomLocale): List<Poem>
}
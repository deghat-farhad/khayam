package com.vuxur.khayyam.data.repository

import com.vuxur.khayyam.data.local.Local
import com.vuxur.khayyam.data.mapper.LocaleMapper
import com.vuxur.khayyam.data.mapper.PoemMapper
import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.repository.PoemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PoemRepositoryImpl @Inject constructor(
    private val local: Local,
    private val poemMapper: PoemMapper,
    private val localeMapper: LocaleMapper,
) :
    PoemRepository {
    override fun getPoems(locale: Locale.SelectedLocale): Flow<List<Poem>> {
        return local.getPoems(localeMapper.mapToData(locale)).map { poemMapper.mapToDomain(it) }
    }

    override fun findPoems(searchPhrase: String): Flow<List<Poem>> {
        return local.findPoems(searchPhrase).map {poemMapper.mapToDomain(it)}
    }
}
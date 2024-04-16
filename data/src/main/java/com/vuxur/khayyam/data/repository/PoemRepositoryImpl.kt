package com.vuxur.khayyam.data.repository

import com.vuxur.khayyam.data.local.Local
import com.vuxur.khayyam.data.mapper.LocaleMapper
import com.vuxur.khayyam.data.mapper.PoemMapper
import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.repository.PoemRepository
import javax.inject.Inject

class PoemRepositoryImpl @Inject constructor(
    private val local: Local,
    private val poemMapper: PoemMapper,
    private val localeMapper: LocaleMapper,
) :
    PoemRepository {
    override suspend fun getPoems(locale: Locale.CustomLocale): List<Poem> {
        return local.getPoems(localeMapper.mapToData(locale)).map { poemMapper.mapToDomain(it) }
    }

    override suspend fun findPoems(searchPhrase: String, locale: Locale.CustomLocale): List<Poem> {
        return local.findPoems(searchPhrase, localeMapper.mapToData(locale))
            .map { poemMapper.mapToDomain(it) }
    }
}
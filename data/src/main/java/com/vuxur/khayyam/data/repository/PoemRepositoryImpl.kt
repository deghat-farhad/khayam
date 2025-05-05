package com.vuxur.khayyam.data.repository

import com.vuxur.khayyam.data.local.Local
import com.vuxur.khayyam.data.mapper.PoemMapper
import com.vuxur.khayyam.data.mapper.TranslationEntityMapper
import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.model.Translation
import com.vuxur.khayyam.domain.repository.PoemRepository
import javax.inject.Inject

class PoemRepositoryImpl @Inject constructor(
    private val local: Local,
    private val poemMapper: PoemMapper,
    private val translationEntityMapper: TranslationEntityMapper,
) : PoemRepository {
    override suspend fun getPoems(translation: Translation): List<Poem> {
        return local.getPoems(translationEntityMapper.mapToData(translation))
            .map { poemMapper.mapToDomain(it) }
    }

    override suspend fun findPoems(searchPhrase: String, translation: Translation): List<Poem> {
        return local.findPoems(
            searchPhrase,
            translationEntityMapper.mapToData(translation)
        )
            .map { poemMapper.mapToDomain(it) }
    }

    override suspend fun getRandomPoem(translation: Translation): Poem {
        return poemMapper.mapToDomain(
            local.getRandomPoem(translationEntityMapper.mapToData(translation))
        )
    }
}
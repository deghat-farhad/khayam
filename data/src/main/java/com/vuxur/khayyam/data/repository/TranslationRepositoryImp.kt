package com.vuxur.khayyam.data.repository

import com.vuxur.khayyam.data.local.Local
import com.vuxur.khayyam.data.mapper.TranslationEntityMapper
import com.vuxur.khayyam.domain.model.Translation
import com.vuxur.khayyam.domain.repository.TranslationRepository
import javax.inject.Inject

class TranslationRepositoryImp @Inject constructor(
    private val local: Local,
    private val translationEntityMapper: TranslationEntityMapper,
) : TranslationRepository {
    override suspend fun getAvailableTranslations(): List<Translation> {
        return translationEntityMapper.mapToDomain(local.getAvailableTranslations())
    }
}
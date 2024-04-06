package com.vuxur.khayyam.data.repository

import com.vuxur.khayyam.data.local.Local
import com.vuxur.khayyam.data.mapper.LocaleMapper
import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.repository.LocaleRepository
import javax.inject.Inject

class LocaleRepositoryImpl @Inject constructor(
    private val local: Local,
    private val localeMapper: LocaleMapper,
) : LocaleRepository {
    override suspend fun getSupportedLanguages(): List<Locale> {
        return localeMapper.mapToDomain(local.getLocales())
    }
}
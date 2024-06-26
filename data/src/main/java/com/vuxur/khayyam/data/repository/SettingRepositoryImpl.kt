package com.vuxur.khayyam.data.repository

import com.vuxur.khayyam.data.local.Local
import com.vuxur.khayyam.data.mapper.LocaleMapper
import com.vuxur.khayyam.data.mapper.PoemMapper
import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.repository.SettingRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val local: Local,
    private val localeMapper: LocaleMapper,
    private val poemMapper: PoemMapper,
) : SettingRepository {
    override val lastVisitedPoem = local.lastVisitedPoem.map { lastVisitedPoemEntity ->
        lastVisitedPoemEntity?.let {
            poemMapper.mapToDomain(lastVisitedPoemEntity)
        }
    }
    override val selectedPoemLocale = local.selectedLanguageLocale.map { localeEntity ->
        localeEntity.let { localeMapper.mapToDomain(it) }
    }

    override suspend fun setSelectedPoemLocale(locale: Locale) {
        local.setSelectedPoemLocale(localeMapper.mapToData(locale))
    }

    override suspend fun setLastVisitedPoem(lastVisitedPoem: Poem) {
        local.setLastVisitedPoem(poemMapper.mapToData(lastVisitedPoem))
    }
}
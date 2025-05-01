package com.vuxur.khayyam.data.repository

import com.vuxur.khayyam.data.local.Local
import com.vuxur.khayyam.data.mapper.PoemMapper
import com.vuxur.khayyam.data.mapper.TimeOfDayEntityMapper
import com.vuxur.khayyam.data.mapper.TranslationOptionsEntityMapper
import com.vuxur.khayyam.data.mapper.TranslationPreferencesEntityMapper
import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.model.TimeOfDay
import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.domain.model.TranslationPreferences
import com.vuxur.khayyam.domain.repository.SettingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingRepositoryImpl @Inject constructor(
    private val local: Local,
    private val translationOptionsEntityMapper: TranslationOptionsEntityMapper,
    private val poemMapper: PoemMapper,
    private val translationPreferencesEntityMapper: TranslationPreferencesEntityMapper,
    private val timeOfDayEntityMapper: TimeOfDayEntityMapper,
) : SettingRepository {
    override val lastVisitedPoem = local.lastVisitedPoem.map { lastVisitedPoemEntity ->
        lastVisitedPoemEntity?.let {
            poemMapper.mapToDomain(lastVisitedPoemEntity)
        }
    }

    override val translationPreferences: Flow<TranslationPreferences> =
        local.selectedTranslationOption.map { translationPreferences ->
            translationPreferencesEntityMapper.mapToDomain(translationPreferences)
        }

    override val randomPoemNotificationTime: Flow<TimeOfDay> =
        local.randomPoemNotificationTime.map { timeOfDayEntity ->
            timeOfDayEntityMapper.mapToDomain(timeOfDayEntity)
        }

    override suspend fun useUntranslated() {
        local.useUntranslated()
    }

    override suspend fun useMatchSystemLanguageTranslation() {
        local.useMatchSystemLanguageTranslation()
    }

    override suspend fun useSpecificTranslation(specificTranslation: TranslationOptions.Specific) {
        local.useSpecificTranslation(translationOptionsEntityMapper.mapToData(specificTranslation))
    }

    override suspend fun setLastVisitedPoem(lastVisitedPoem: Poem) {
        local.setLastVisitedPoem(poemMapper.mapToData(lastVisitedPoem))
    }

    override suspend fun setRandomPoemNotificationTime(timeOfDay: TimeOfDay) {
        local.setRandomPoemNotificationTime(
            timeOfDayEntityMapper.mapToData(timeOfDay)
        )
    }
}
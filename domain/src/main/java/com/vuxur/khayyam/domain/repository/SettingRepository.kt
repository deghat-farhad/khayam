package com.vuxur.khayyam.domain.repository

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.model.TimeOfDay
import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.domain.model.TranslationPreferences
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    val lastVisitedPoem: Flow<Poem?>
    val translationPreferences: Flow<TranslationPreferences>
    val randomPoemNotificationTime: Flow<TimeOfDay>
    val isRandomPoemNotificationEnabled: Flow<Boolean>
    suspend fun useUntranslated()
    suspend fun useMatchSystemLanguageTranslation()
    suspend fun useSpecificTranslation(specificTranslation: TranslationOptions.Specific)
    suspend fun setLastVisitedPoem(lastVisitedPoem: Poem)
    suspend fun setRandomPoemNotificationTime(timeOfDay: TimeOfDay)
    suspend fun setRandomPoemNotificationEnabled(isEnabled: Boolean)
}
package com.vuxur.khayyam.domain.repository

import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.model.Poem
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    val lastVisitedPoem: Flow<Poem?>
    val selectedPoemLocale: Flow<Locale>
    suspend fun setSelectedPoemLocale(locale: Locale)
    suspend fun setLastVisitedPoem(lastVisitedPoem: Poem)
}
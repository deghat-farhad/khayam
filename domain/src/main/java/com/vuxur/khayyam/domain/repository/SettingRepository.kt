package com.vuxur.khayyam.domain.repository

import com.vuxur.khayyam.domain.model.Locale
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    val selectedPoemLocale: Flow<Locale>
    suspend fun setSelectedPoemLocale(locale: Locale)
}
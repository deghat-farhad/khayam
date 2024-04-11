package com.vuxur.khayyam.domain.repository

import com.vuxur.khayyam.domain.model.Locale

interface LocaleRepository {
    suspend fun getSupportedLanguages(): List<Locale>
}
package com.vuxur.khayyam.domain.repository

import com.vuxur.khayyam.domain.model.Translation

interface TranslationRepository {
    suspend fun getAvailableTranslations(): List<Translation>
}
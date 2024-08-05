package com.vuxur.khayyam.domain.repository

import com.vuxur.khayyam.domain.model.Translation

interface TranslationRepository {
    suspend fun getAvailableTranslations(): List<Translation>
    suspend fun getTranslationsWithLanguageTag(languageTag: String): List<Translation>
    suspend fun getTranslationWithId(translationId: Int): Translation
}
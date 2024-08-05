package com.vuxur.khayyam.data.local

import com.vuxur.khayyam.data.entity.PoemWithTranslationEntity
import com.vuxur.khayyam.data.entity.TranslationEntity
import com.vuxur.khayyam.data.entity.TranslationOptionsEntity
import com.vuxur.khayyam.data.entity.TranslationPreferencesEntity
import com.vuxur.khayyam.data.local.database.PoemDatabaseDao
import com.vuxur.khayyam.data.local.sharedPreferences.PreferencesDataSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Local @Inject constructor(
    private val database: PoemDatabaseDao,
    private val preferencesDataSource: PreferencesDataSource,
) {
    suspend fun getPoems(translationEntity: TranslationEntity) =
        database.getPoems(translationEntity.languageTag)

    suspend fun findPoems(
        searchPhrase: String,
        translationEntity: TranslationEntity
    ) = database.findPoems(
        searchPhrase,
        translationEntity.languageTag
    )

    suspend fun getAvailableTranslations() = database.getAvailableTranslations()

    suspend fun useUntranslated() {
        preferencesDataSource.useUntranslated()
    }

    suspend fun useMatchSystemLanguageTranslation() {
        preferencesDataSource.useMatchDeviceLanguageTranslation()
    }

    suspend fun useSpecificTranslation(specificTranslationOptionsEntity: TranslationOptionsEntity.Specific) {
        preferencesDataSource.setSpecificTranslation(
            specificTranslationOptionsEntity.translationEntity
        )
    }

    suspend fun setLastVisitedPoem(poemWithTranslationEntity: PoemWithTranslationEntity) {
        preferencesDataSource.setLastVisitedPoem(poemWithTranslationEntity)
    }

    suspend fun getTranslationsWithLanguageTag(languageTag: String): List<TranslationEntity> {
        return database.getTranslationsWithLanguageTag(languageTag)
    }

    suspend fun getTranslationWithId(translationId: Int): TranslationEntity {
        return database.getTranslationsWithId(translationId)
    }

    val lastVisitedPoem: Flow<PoemWithTranslationEntity?> =
        preferencesDataSource.lastVisitedPoem.map { lastVisitedPoemId ->
            database.getPoemById(lastVisitedPoemId)
        }

    val selectedTranslationOption: Flow<TranslationPreferencesEntity> =
        preferencesDataSource.transactionState
}
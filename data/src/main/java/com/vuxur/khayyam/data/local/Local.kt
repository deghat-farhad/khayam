package com.vuxur.khayyam.data.local

import android.content.res.Resources
import android.os.Build
import com.vuxur.khayyam.data.entity.PoemWithTranslationEntity
import com.vuxur.khayyam.data.entity.TranslationEntity
import com.vuxur.khayyam.data.entity.TranslationOptionsEntity
import com.vuxur.khayyam.data.local.database.PoemDatabaseDao
import com.vuxur.khayyam.data.local.sharedPreferences.PreferencesDataSource
import com.vuxur.khayyam.data.local.sharedPreferences.TranslationPreferences
import java.util.Locale
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

    val lastVisitedPoem: Flow<PoemWithTranslationEntity?> =
        preferencesDataSource.lastVisitedPoem.map { lastVisitedPoemId ->
            database.getPoemById(lastVisitedPoemId)
        }

    val selectedTranslationOption: Flow<TranslationOptionsEntity> =
        preferencesDataSource.transactionState
            .map { translationPreferences ->
                when (translationPreferences) {
                    TranslationPreferences.MatchDeviceLanguageTranslation ->
                        TranslationOptionsEntity.MatchDeviceLanguage(
                            database.getTranslationsWithLanguageTag(
                                getCurrentLocale(Resources.getSystem()).toLanguageTag()
                            ).first(),
                        )

                    TranslationPreferences.None ->
                        TranslationOptionsEntity.None

                    is TranslationPreferences.SpecificTranslation ->
                        TranslationOptionsEntity.Specific(
                            database.getTranslationsWithId(translationPreferences.id)
                        )

                    TranslationPreferences.Untranslated ->
                        TranslationOptionsEntity.Untranslated
                }
            }

    private fun getCurrentLocale(resources: Resources): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            resources.configuration.locale
        }
    }
}
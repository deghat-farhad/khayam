package com.vuxur.khayyam.data.local.sharedPreferences

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vuxur.khayyam.data.entity.PoemWithTranslationEntity
import com.vuxur.khayyam.data.entity.TranslationEntity
import com.vuxur.khayyam.data.entity.TranslationOptionsEntity
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

private const val DATA_STORE_NAME = "settingSharedPreferences"
private val SPECIFIC_TRANSLATION_ID_KEY = intPreferencesKey("specificTranslationId")
private val LAST_VISITED_POEM_ID_KEY = intPreferencesKey("lastVisitedPoemIdKey")
private val IS_USING_MATCH_DEVICE_LANGUAGE_TRANSLATION_KEY =
    booleanPreferencesKey("isUsingMatchDeviceLanguageTranslation")
private val IS_USING_UNTRANSLATED_KEY = booleanPreferencesKey("isUsingUntranslated")
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

class PreferencesDataSource @Inject constructor(
    private val application: Application
) {
    val translationOption = MutableSharedFlow<TranslationOptionsEntity>()
    suspend fun setSpecificTranslation(translationEntity: TranslationEntity) {
        application.dataStore.edit { preferences ->
            preferences[SPECIFIC_TRANSLATION_ID_KEY] = translationEntity.id
            preferences[IS_USING_MATCH_DEVICE_LANGUAGE_TRANSLATION_KEY] = false
            preferences[IS_USING_UNTRANSLATED_KEY] = false
        }
    }

    suspend fun useMatchDeviceLanguageTranslation() {
        application.dataStore.edit { preferences ->
            preferences[SPECIFIC_TRANSLATION_ID_KEY] = -1
            preferences[IS_USING_MATCH_DEVICE_LANGUAGE_TRANSLATION_KEY] = true
            preferences[IS_USING_UNTRANSLATED_KEY] = false
        }
    }

    suspend fun useUntranslated() {
        application.dataStore.edit { preferences ->
            preferences[SPECIFIC_TRANSLATION_ID_KEY] = -1
            preferences[IS_USING_MATCH_DEVICE_LANGUAGE_TRANSLATION_KEY] = false
            preferences[IS_USING_UNTRANSLATED_KEY] = true
        }
    }

    suspend fun setLastVisitedPoem(poemWithTranslationEntity: PoemWithTranslationEntity) {
        application.dataStore.edit { preferences ->
            preferences[LAST_VISITED_POEM_ID_KEY] = poemWithTranslationEntity.poem.id
        }
    }

    val lastVisitedPoem: Flow<Int> = application.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[LAST_VISITED_POEM_ID_KEY] ?: -1
        }
        .distinctUntilChanged()

    val transactionState: Flow<TranslationPreferences> = application.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .mapNotNull { preferences ->
            val specificTranslationId = preferences[SPECIFIC_TRANSLATION_ID_KEY] ?: -1
            val isUsingUntranslated = preferences[IS_USING_UNTRANSLATED_KEY] ?: false
            val isUsingMatchDeviceLanguageTranslation =
                preferences[IS_USING_MATCH_DEVICE_LANGUAGE_TRANSLATION_KEY] ?: false
            when {
                specificTranslationId > 0 -> TranslationPreferences.SpecificTranslation(
                    specificTranslationId
                )

                isUsingUntranslated -> TranslationPreferences.Untranslated

                isUsingMatchDeviceLanguageTranslation -> TranslationPreferences.MatchDeviceLanguageTranslation

                preferences[IS_USING_MATCH_DEVICE_LANGUAGE_TRANSLATION_KEY] == null &&
                    preferences[SPECIFIC_TRANSLATION_ID_KEY] == null &&
                    preferences[IS_USING_MATCH_DEVICE_LANGUAGE_TRANSLATION_KEY] == null
                -> TranslationPreferences.None

                else -> null
            }
        }
        .distinctUntilChanged()
}

sealed class TranslationPreferences {
    data object Untranslated : TranslationPreferences()
    data object MatchDeviceLanguageTranslation : TranslationPreferences()
    data object None : TranslationPreferences()
    data class SpecificTranslation(
        val id: Int
    ) : TranslationPreferences()
}

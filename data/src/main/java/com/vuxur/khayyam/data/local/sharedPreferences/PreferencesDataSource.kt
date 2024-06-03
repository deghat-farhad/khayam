package com.vuxur.khayyam.data.local.sharedPreferences

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vuxur.khayyam.data.entity.PoemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val DATA_STORE_NAME = "settingSharedPreferences"
private val SELECTED_POEM_LANGUAGE_TAG_KEY = stringPreferencesKey("selectedPoemLanguageTagKey")
private val LAST_VISITED_POEM_ID_KEY = intPreferencesKey("lastVisitedPoemIdKey")
internal const val SYSTEM_DEFAULT_LANGUAGE = "systemDefaultLanguage"
internal const val NO_LANGUAGE_SELECTED = "noLanguageSelected"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

class PreferencesDataSource @Inject constructor(
    private val application: Application
) {
    suspend fun setSelectedPoemLanguageTag(value: String) {
        application.dataStore.edit { preferences ->
            preferences[SELECTED_POEM_LANGUAGE_TAG_KEY] = value
        }
    }

    suspend fun setLastVisitedPoem(poemEntity: PoemEntity) {
        application.dataStore.edit { preferences ->
            preferences[LAST_VISITED_POEM_ID_KEY] = poemEntity.id
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

    val selectedPoemLanguageTag: Flow<String> = application.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[SELECTED_POEM_LANGUAGE_TAG_KEY] ?: NO_LANGUAGE_SELECTED
        }
        .distinctUntilChanged()
}

package com.vuxur.khayyam.data.local

import com.vuxur.khayyam.data.entity.LocaleEntity
import com.vuxur.khayyam.data.entity.PoemEntity
import com.vuxur.khayyam.data.local.database.PoemDatabaseDao
import com.vuxur.khayyam.data.local.sharedPreferences.NO_LANGUAGE_SELECTED
import com.vuxur.khayyam.data.local.sharedPreferences.PreferencesDataSource
import com.vuxur.khayyam.data.local.sharedPreferences.SYSTEM_DEFAULT_LANGUAGE
import com.vuxur.khayyam.data.mapper.LanguageTagEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject

class Local @Inject constructor(
    private val database: PoemDatabaseDao,
    private val languageTagEntityMapper: LanguageTagEntityMapper,
    private val preferencesDataSource: PreferencesDataSource,
) {
    suspend fun getPoems(localeEntity: LocaleEntity.CustomLocale) =
        database.getPoems(localeEntity.locale.toLanguageTag())

    suspend fun findPoems(
        searchPhrase: String,
        localeEntity: LocaleEntity.CustomLocale
    ) = database.findPoems(searchPhrase, localeEntity.locale.toLanguageTag())

    suspend fun getLocales() = languageTagEntityMapper.mapToLocaleEntity(database.getLocales())

    suspend fun setSelectedPoemLocale(localeEntity: LocaleEntity) =
        when (localeEntity) {
            LocaleEntity.NoLocale -> preferencesDataSource.setSelectedPoemLanguageTag(
                NO_LANGUAGE_SELECTED
            )

            is LocaleEntity.CustomLocale -> preferencesDataSource.setSelectedPoemLanguageTag(
                localeEntity.locale.toLanguageTag()
            )

            LocaleEntity.SystemLocale -> preferencesDataSource.setSelectedPoemLanguageTag(
                SYSTEM_DEFAULT_LANGUAGE
            )
        }

    suspend fun setLastVisitedPoem(poemEntity: PoemEntity) {
        preferencesDataSource.setLastVisitedPoem(poemEntity)
    }

    val lastVisitedPoem: Flow<PoemEntity?> =
        preferencesDataSource.lastVisitedPoem.map { lastVisitedPoemId ->
        database.getPoemById(lastVisitedPoemId)
    }
    val selectedLanguageLocale = preferencesDataSource.selectedPoemLanguageTag.map { languageTag ->
        when (languageTag) {
            SYSTEM_DEFAULT_LANGUAGE -> LocaleEntity.SystemLocale
            NO_LANGUAGE_SELECTED -> LocaleEntity.NoLocale
            else -> LocaleEntity.CustomLocale(
                locale = Locale.forLanguageTag(languageTag)
            )
        }
    }
}
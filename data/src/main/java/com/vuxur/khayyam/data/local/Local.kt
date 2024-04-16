package com.vuxur.khayyam.data.local

import com.vuxur.khayyam.data.entity.LocaleEntity
import com.vuxur.khayyam.data.local.database.PoemDatabaseDao
import com.vuxur.khayyam.data.local.sharedPreferences.NO_LANGUAGE_SELECTED
import com.vuxur.khayyam.data.local.sharedPreferences.PreferencesDataSource
import com.vuxur.khayyam.data.local.sharedPreferences.SYSTEM_DEFAULT_LANGUAGE
import com.vuxur.khayyam.data.mapper.LanguageTagEntityMapper
import kotlinx.coroutines.flow.map
import java.util.Locale

class Local(
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
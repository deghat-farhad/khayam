package com.vuxur.khayyam.data.di

import android.app.Application
import com.vuxur.khayyam.data.local.Local
import com.vuxur.khayyam.data.local.database.PoemDatabase
import com.vuxur.khayyam.data.local.database.PoemDatabaseDao
import com.vuxur.khayyam.data.local.sharedPreferences.PreferencesDataSource
import com.vuxur.khayyam.data.mapper.LanguageTagEntityMapper
import com.vuxur.khayyam.data.mapper.LocaleMapper
import com.vuxur.khayyam.data.mapper.PoemMapper
import com.vuxur.khayyam.data.repository.LocaleRepositoryImpl
import com.vuxur.khayyam.data.repository.PoemRepositoryImpl
import com.vuxur.khayyam.data.repository.SettingRepositoryImpl
import com.vuxur.khayyam.domain.repository.LocaleRepository
import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.repository.SettingRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun database(application: Application) = PoemDatabase.getInstance(application).poemDatabaseDao

    @Provides
    fun local(
        database: PoemDatabaseDao,
        languageTagEntityMapper: LanguageTagEntityMapper,
        preferencesDataSource: PreferencesDataSource,
    ) = Local(database, languageTagEntityMapper, preferencesDataSource)

    @Provides
    fun poemMapper() = PoemMapper()

    @Provides
    fun poemRepository(
        local: Local,
        poemMapper: PoemMapper,
        localeMapper: LocaleMapper
    ): PoemRepository =
        PoemRepositoryImpl(local, poemMapper, localeMapper)

    @Provides
    fun localeRepository(local: Local, localeMapper: LocaleMapper): LocaleRepository =
        LocaleRepositoryImpl(local, localeMapper)

    @Provides
    fun settingRepository(local: Local, localeMapper: LocaleMapper): SettingRepository =
        SettingRepositoryImpl(local, localeMapper)
}
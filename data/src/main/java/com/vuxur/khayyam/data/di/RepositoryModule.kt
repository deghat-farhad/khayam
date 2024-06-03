package com.vuxur.khayyam.data.di

import android.content.Context
import com.vuxur.khayyam.data.local.Local
import com.vuxur.khayyam.data.local.database.PoemDatabase
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
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    fun database(
        @ApplicationContext applicationContext: Context
    ) = PoemDatabase.getInstance(applicationContext).poemDatabaseDao

    @Provides
    fun providePoemRepository(
        local: Local,
        poemMapper: PoemMapper,
        localeMapper: LocaleMapper,
    ): PoemRepository =
        PoemRepositoryImpl(
            local,
            poemMapper,
            localeMapper,
        )

    @Provides
    fun provideSettingRepository(
        local: Local,
        localeMapper: LocaleMapper,
        poemMapper: PoemMapper,
    ): SettingRepository =
        SettingRepositoryImpl(
            local,
            localeMapper,
            poemMapper,
        )

    @Provides
    fun provideLocaleRepository(
        local: Local,
        localeMapper: LocaleMapper,
    ):LocaleRepository =
        LocaleRepositoryImpl(
            local,
            localeMapper,
        )
}
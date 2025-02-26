package com.vuxur.khayyam.data.di

import android.content.Context
import com.vuxur.khayyam.data.local.Local
import com.vuxur.khayyam.data.local.database.PoemDatabase
import com.vuxur.khayyam.data.mapper.PoemMapper
import com.vuxur.khayyam.data.mapper.TranslationEntityMapper
import com.vuxur.khayyam.data.mapper.TranslationOptionsEntityMapper
import com.vuxur.khayyam.data.mapper.TranslationPreferencesEntityMapper
import com.vuxur.khayyam.data.repository.PoemRepositoryImpl
import com.vuxur.khayyam.data.repository.SettingRepositoryImpl
import com.vuxur.khayyam.data.repository.TranslationRepositoryImp
import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.repository.TranslationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun database(
        @ApplicationContext applicationContext: Context
    ) = PoemDatabase.getInstance(applicationContext).poemDatabaseDao

    @Provides
    fun providePoemRepository(
        local: Local,
        poemMapper: PoemMapper,
        translationEntityMapper: TranslationEntityMapper,
    ): PoemRepository =
        PoemRepositoryImpl(
            local,
            poemMapper,
            translationEntityMapper,
        )

    @Provides
    fun provideSettingRepository(
        local: Local,
        translationOptionsEntityMapper: TranslationOptionsEntityMapper,
        poemMapper: PoemMapper,
        translationPreferencesEntityMapper: TranslationPreferencesEntityMapper,
    ): SettingRepository =
        SettingRepositoryImpl(
            local,
            translationOptionsEntityMapper,
            poemMapper,
            translationPreferencesEntityMapper,
        )

    @Provides
    fun provideTranslationRepository(
        local: Local,
        translationEntityMapper: TranslationEntityMapper,
    ): TranslationRepository =
        TranslationRepositoryImp(
            local,
            translationEntityMapper,
        )
}
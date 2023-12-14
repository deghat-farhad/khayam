package com.raven.khayam.di

import android.app.Application
import com.raven.khayam.data.di.DaggerDataComponent
import com.raven.khayam.data.local.Local
import com.raven.khayam.data.local.database.PoemDatabase
import com.raven.khayam.data.local.database.PoemDatabaseDao
import com.raven.khayam.data.mapper.PoemMapper
import com.raven.khayam.data.repository.PoemRepositoryImpl
import com.raven.khayam.domain.repository.PoemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    fun poemRepository(application: Application) =
        DaggerDataComponent.builder().application(application).build().poemRepository()
}
package com.raven.khayam.data.di

import android.app.Application
import com.raven.khayam.data.local.Local
import com.raven.khayam.data.local.database.PoemDatabase
import com.raven.khayam.data.local.database.PoemDatabaseDao
import com.raven.khayam.data.mapper.PoemMapper
import com.raven.khayam.data.repository.PoemRepositoryImpl
import com.raven.khayam.domain.repository.PoemRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
    @Provides
    fun database(application: Application) = PoemDatabase.getInstance(application).poemDatabaseDao

    @Provides
    fun local(database: PoemDatabaseDao) = Local(database)

    @Provides
    fun poemMapper() = PoemMapper()

    @Provides
    fun poemRepository(local: Local, poemMapper: PoemMapper): PoemRepository =
        PoemRepositoryImpl(local, poemMapper)
}
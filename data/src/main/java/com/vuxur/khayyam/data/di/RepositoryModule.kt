package com.vuxur.khayyam.data.di

import android.app.Application
import com.vuxur.khayyam.data.local.Local
import com.vuxur.khayyam.data.local.database.PoemDatabase
import com.vuxur.khayyam.data.local.database.PoemDatabaseDao
import com.vuxur.khayyam.data.mapper.PoemMapper
import com.vuxur.khayyam.data.repository.PoemRepositoryImpl
import com.vuxur.khayyam.domain.repository.PoemRepository
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
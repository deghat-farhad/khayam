package com.vuxur.khayyam.di

import android.app.Application
import com.vuxur.khayyam.data.di.DaggerDataComponent
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

    @Provides
    fun localeRepository(application: Application) =
        DaggerDataComponent.builder().application(application).build().localeRepository()
}
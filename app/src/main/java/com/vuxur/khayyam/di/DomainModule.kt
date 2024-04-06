package com.vuxur.khayyam.di

import com.vuxur.khayyam.domain.repository.LocaleRepository
import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.usecase.findPoems.FindPoems
import com.vuxur.khayyam.domain.usecase.getPoems.GetPoems
import com.vuxur.khayyam.domain.usecase.getSupportedLocales.GetSupportedLocale
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    fun getPoems(
        poemRepository: PoemRepository,
   ) = GetPoems(poemRepository)

    @Provides
    fun findPoems(
        poemRepository: PoemRepository,
    ) = FindPoems(poemRepository)

    @Provides
    fun getSupportedLocale(
        localeRepository: LocaleRepository
    ) = GetSupportedLocale(localeRepository)
}
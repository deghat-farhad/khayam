package com.vuxur.khayyam.di

import com.vuxur.khayyam.domain.repository.LocaleRepository
import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.usecase.findPoems.FindPoems
import com.vuxur.khayyam.domain.usecase.getLastVisitedPoem.GetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.getPoems.GetPoems
import com.vuxur.khayyam.domain.usecase.getSelectedPoemLocale.GetSelectedPoemLocale
import com.vuxur.khayyam.domain.usecase.getSupportedLocales.GetSupportedLocale
import com.vuxur.khayyam.domain.usecase.setLastVisitedPoem.SetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.setSelectedPoemLocale.SetSelectedPoemLocale
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    fun provideGetPoems(
        poemRepository: PoemRepository,
        getSupportedLocale: GetSupportedLocale,
    ) = GetPoems(poemRepository, getSupportedLocale)

    @Provides
    fun provideFindPoems(
        poemRepository: PoemRepository,
    ) = FindPoems(poemRepository)

    @Provides
    fun provideGetSupportedLocale(
        localeRepository: LocaleRepository
    ) = GetSupportedLocale(localeRepository)

    @Provides
    fun provideGetSelectedPoemLocale(
        settingRepository: SettingRepository
    ) = GetSelectedPoemLocale(settingRepository)

    @Provides
    fun provideSetSelectedPoemLocale(
        settingRepository: SettingRepository
    ) = SetSelectedPoemLocale(settingRepository)

    @Provides
    fun provideSetLastVisitedPoem(
        settingRepository: SettingRepository,
    ) = SetLastVisitedPoem(settingRepository)

    @Provides
    fun provideGetLastVisitedPoem(
        settingRepository: SettingRepository,
    ) = GetLastVisitedPoem(settingRepository)
}
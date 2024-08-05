package com.vuxur.khayyam.di

import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.repository.TranslationRepository
import com.vuxur.khayyam.domain.usecase.findPoems.FindPoems
import com.vuxur.khayyam.domain.usecase.getLastVisitedPoem.GetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.getPoems.GetPoems
import com.vuxur.khayyam.domain.usecase.getSelectedTranslationOption.GetSelectedTranslationOption
import com.vuxur.khayyam.domain.usecase.getTranslations.GetAvailableTranslations
import com.vuxur.khayyam.domain.usecase.setLastVisitedPoem.SetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.useMatchingSystemLanguageTranslation.UseMatchSystemLanguageTranslation
import com.vuxur.khayyam.domain.usecase.useSpecificTranslation.UseSpecificTranslation
import com.vuxur.khayyam.domain.usecase.useUntranslated.UseUntranslated
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
    ) = GetPoems(poemRepository)

    @Provides
    fun provideFindPoems(
        poemRepository: PoemRepository,
    ) = FindPoems(poemRepository)

    @Provides
    fun provideUseMatchSystemLanguageTranslation(
        settingRepository: SettingRepository
    ) = UseMatchSystemLanguageTranslation(settingRepository)

    @Provides
    fun provideUseUntranslated(
        settingRepository: SettingRepository
    ) = UseUntranslated(settingRepository)

    @Provides
    fun provideGetAvailableTranslations(
        translationRepository: TranslationRepository
    ) = GetAvailableTranslations(translationRepository)

    @Provides
    fun provideGetSelectedTranslationOption(
        settingRepository: SettingRepository,
        translationRepository: TranslationRepository,
    ) = GetSelectedTranslationOption(settingRepository, translationRepository)

    @Provides
    fun provideUseSpecificTranslation(
        settingRepository: SettingRepository
    ) = UseSpecificTranslation(settingRepository)

    @Provides
    fun provideSetLastVisitedPoem(
        settingRepository: SettingRepository,
    ) = SetLastVisitedPoem(settingRepository)

    @Provides
    fun provideGetLastVisitedPoem(
        settingRepository: SettingRepository,
    ) = GetLastVisitedPoem(settingRepository)
}
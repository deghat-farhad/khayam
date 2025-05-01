package com.vuxur.khayyam.di

import com.vuxur.khayyam.domain.repository.PoemRepository
import com.vuxur.khayyam.domain.repository.SettingRepository
import com.vuxur.khayyam.domain.repository.TranslationRepository
import com.vuxur.khayyam.domain.usecase.poems.findPoems.FindPoems
import com.vuxur.khayyam.domain.usecase.poems.getPoems.GetPoems
import com.vuxur.khayyam.domain.usecase.settings.lastVositedPoem.getLastVisitedPoem.GetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.settings.lastVositedPoem.setLastVisitedPoem.SetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.isEnabled.isRandomPoemNotificationEnabled.IsRandomPoemNotificationEnabled
import com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.isEnabled.setRandomPoemNotificationsEnabled.SetRandomPoemNotificationEnabled
import com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.time.getRandomPoemNotificationTime.GetRandomPoemNotificationTime
import com.vuxur.khayyam.domain.usecase.settings.randomPoemNotification.time.setRandomPoemNotificationTime.SetRandomPoemNotificationTime
import com.vuxur.khayyam.domain.usecase.settings.translation.getSelectedTranslationOption.GetSelectedTranslationOption
import com.vuxur.khayyam.domain.usecase.settings.translation.getTranslations.GetAvailableTranslations
import com.vuxur.khayyam.domain.usecase.settings.translation.useMatchingSystemLanguageTranslation.UseMatchSystemLanguageTranslation
import com.vuxur.khayyam.domain.usecase.settings.translation.useSpecificTranslation.UseSpecificTranslation
import com.vuxur.khayyam.domain.usecase.settings.translation.useUntranslated.UseUntranslated
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

    @Provides
    fun provideSetRandomPoemNotificationTime(
        settingRepository: SettingRepository,
    ) = SetRandomPoemNotificationTime(settingRepository)

    @Provides
    fun provideGetRandomPoemNotificationTime(
        settingRepository: SettingRepository,
    ) = GetRandomPoemNotificationTime(settingRepository)

    @Provides
    fun provideSetRandomPoemNotificationEnabled(
        settingRepository: SettingRepository,
    ) = SetRandomPoemNotificationEnabled(settingRepository)

    @Provides
    fun provideIsRandomPoemNotificationEnabled(
        settingRepository: SettingRepository,
    ) = IsRandomPoemNotificationEnabled(settingRepository)
}
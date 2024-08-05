package com.vuxur.khayyam.pages.setting

import com.vuxur.khayyam.domain.model.Translation
import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.domain.usecase.getSelectedTranslationOption.GetSelectedTranslationOption
import com.vuxur.khayyam.domain.usecase.getSelectedTranslationOption.UNTRANSLATED_LANGUAGE_TAG
import com.vuxur.khayyam.domain.usecase.getTranslations.GetAvailableTranslations
import com.vuxur.khayyam.domain.usecase.useMatchingSystemLanguageTranslation.UseMatchSystemLanguageTranslation
import com.vuxur.khayyam.domain.usecase.useSpecificTranslation.UseSpecificTranslation
import com.vuxur.khayyam.domain.usecase.useSpecificTranslation.UseSpecificTranslationParams
import com.vuxur.khayyam.domain.usecase.useUntranslated.UseUntranslated
import com.vuxur.khayyam.mapper.TranslationItemMapper
import com.vuxur.khayyam.mapper.TranslationOptionsItemMapper
import com.vuxur.khayyam.model.TranslationItem
import com.vuxur.khayyam.model.TranslationOptionsItem
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SettingViewModelTest {

    private lateinit var settingViewModel: SettingViewModel
    private val getSelectedTranslationOption: GetSelectedTranslationOption = mockk()
    private val getAvailableTranslations: GetAvailableTranslations = mockk()
    private val translationOptionsItemMapper: TranslationOptionsItemMapper = mockk()
    private val useSpecificTranslation: UseSpecificTranslation = mockk()
    private val translationOptions: TranslationOptions = mockk()
    private val poemTranslationOptionsItem: TranslationOptionsItem = mockk()
    private val untranslatedTranslation =
        Translation(
            1,
            UNTRANSLATED_LANGUAGE_TAG,
            "translator"
        )
    private val untranslatedTranslationItem =
        TranslationItem(
            1,
            UNTRANSLATED_LANGUAGE_TAG,
            "translator"
        )
    private val matchDeviceTranslation =
        Translation(
            2,
            java.util.Locale.ENGLISH.toLanguageTag(),
            "translator"
        )
    private val specificTranslation = Translation(
        3,
        java.util.Locale.FRANCE.toLanguageTag(),
        "translator"
    )
    private val specificTranslationItem = TranslationItem(
        3,
        java.util.Locale.FRANCE.toLanguageTag(),
        "translator"
    )
    private val specificTranslationOption = TranslationOptions.Specific(
        specificTranslation
    )
    private val specificTranslationOptionItem = TranslationOptionsItem.Specific(
        specificTranslationItem
    )
    private val availableTranslations: List<Translation> = listOf(
        untranslatedTranslation,
        matchDeviceTranslation,
        specificTranslation,
    )
    private val availableTranslationItems: List<TranslationItem> =
        availableTranslations.map { translationItem ->
            TranslationItem(
                translationItem.id,
                translationItem.languageTag,
                translationItem.translator,
            )
        }
    private val useSpecificTranslationParamsSlot = slot<UseSpecificTranslationParams>()
    private val translationItemMapper: TranslationItemMapper = mockk()
    private val useMatchSystemLanguageTranslation: UseMatchSystemLanguageTranslation = mockk()
    private val useUntranslated: UseUntranslated = mockk()
    private val deviceLocale: Locale = mockk()

    @BeforeEach
    fun setUp() {
        settingViewModel = SettingViewModel(
            getSelectedTranslationOption,
            getAvailableTranslations,
            translationOptionsItemMapper,
            useSpecificTranslation,
            translationItemMapper,
            useMatchSystemLanguageTranslation,
            useUntranslated,
        )

        coEvery { getSelectedTranslationOption(any()) } returns flowOf(translationOptions)
        coEvery { getAvailableTranslations() } returns availableTranslations
        every { translationOptionsItemMapper.mapToPresentation(translationOptions) } returns poemTranslationOptionsItem
        every { translationItemMapper.mapToPresentation(availableTranslations) } returns availableTranslationItems
        every { translationOptionsItemMapper.mapToDomain(specificTranslationOptionItem) } returns specificTranslationOption
        coEvery { useSpecificTranslation(capture(useSpecificTranslationParamsSlot)) } just Runs
        coEvery { useMatchSystemLanguageTranslation() } just Runs
        coEvery { useUntranslated() } just Runs
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewIsReady normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        settingViewModel.viewIsReady(deviceLocale)

        assertTrue(settingViewModel.uiState.value is SettingViewModel.UiState.Loaded)
        val uiState = settingViewModel.uiState.value as SettingViewModel.UiState.Loaded

        coVerify { getAvailableTranslations() }
        verify { translationItemMapper.mapToPresentation(availableTranslations) }
        assertEquals(
            uiState.availableTranslations,
            availableTranslationItems.filterNot { it == untranslatedTranslationItem })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setSpecificTranslation normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        settingViewModel.viewIsReady(deviceLocale)
        settingViewModel.setSpecificTranslation(specificTranslationOptionItem)

        coVerify { useSpecificTranslation(capture(useSpecificTranslationParamsSlot)) }
        assertEquals(
            specificTranslationOption,
            useSpecificTranslationParamsSlot.captured.translationOption
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onEventConsumed normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val event = SettingViewModel.Event.PopBack
        settingViewModel.viewIsReady(deviceLocale)
        settingViewModel.popBack()
        settingViewModel.onEventConsumed(event)
        val uiState = settingViewModel.uiState.value as SettingViewModel.UiState.Loaded

        assertFalse(uiState.events.contains(event))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `popBack normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        settingViewModel.viewIsReady(deviceLocale)
        settingViewModel.popBack()

        val uiState = settingViewModel.uiState.value as SettingViewModel.UiState.Loaded

        assertTrue(uiState.events.contains(SettingViewModel.Event.PopBack))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setToUseUntranslated normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        settingViewModel.viewIsReady(deviceLocale)
        settingViewModel.setToUseUntranslated()

        coVerify {
            useUntranslated()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setToUseMatchSystemLanguageTranslation normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        settingViewModel.viewIsReady(deviceLocale)
        settingViewModel.setToUseMatchSystemLanguageTranslation()

        coVerify {
            useMatchSystemLanguageTranslation()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
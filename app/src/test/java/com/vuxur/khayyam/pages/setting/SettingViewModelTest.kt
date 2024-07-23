package com.vuxur.khayyam.pages.setting

import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.domain.usecase.getSelectedTranslationOption.GetSelectedTranslationOption
import com.vuxur.khayyam.domain.usecase.getTranslations.GetAvailableTranslations
import com.vuxur.khayyam.domain.usecase.useSpecificTranslation.UseSpecificTranslation
import com.vuxur.khayyam.domain.usecase.useSpecificTranslation.UseSpecificTranslationParams
import com.vuxur.khayyam.mapper.TranslationOptionsItemMapper
import com.vuxur.khayyam.model.TranslationOptionsItem
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
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

class SettingViewModelTest() {

    private lateinit var settingViewModel: SettingViewModel
    private val getSelectedTranslationOption: GetSelectedTranslationOption = mockk()
    private val getAvailableTranslations: GetAvailableTranslations = mockk()
    private val translationOptionsItemMapper: TranslationOptionsItemMapper = mockk()
    private val useSpecificTranslation: UseSpecificTranslation = mockk()
    private val poemLocale: TranslationOptions = mockk()
    private val poemTranslationOptionsItem: TranslationOptionsItem = mockk()
    private val originalLocale =
        TranslationOptions.Specific(java.util.Locale.forLanguageTag("fa-IR"))
    private val originalTranslationOptionsItem =
        TranslationOptionsItem.CustomTranslationOptions(java.util.Locale.forLanguageTag("fa-IR"))
    private val supportedLocale: List<TranslationOptions.Specific> = listOf(
        originalLocale,
        TranslationOptions.Specific(java.util.Locale.ENGLISH),
        TranslationOptions.Specific(java.util.Locale.FRANCE),
    )
    private val supportedTranslationOptionsItems: List<TranslationOptionsItem> = listOf(
        originalTranslationOptionsItem,
        TranslationOptionsItem.CustomTranslationOptions(java.util.Locale.ENGLISH),
        TranslationOptionsItem.CustomTranslationOptions(java.util.Locale.FRANCE),
    )
    private val useSpecificTranslationParamsSlot = slot<UseSpecificTranslationParams>()

    @BeforeEach
    fun setUp() {
        settingViewModel = SettingViewModel(
            getSelectedTranslationOption,
            getAvailableTranslations,
            translationOptionsItemMapper,
            useSpecificTranslation,
        )

        coEvery { getSelectedTranslationOption() } returns flowOf(poemLocale)
        coEvery { getAvailableTranslations() } returns supportedLocale
        every { translationOptionsItemMapper.mapToPresentation(poemLocale) } returns poemTranslationOptionsItem
        every { translationOptionsItemMapper.mapToPresentation(supportedLocale) } returns supportedTranslationOptionsItems
        every { translationOptionsItemMapper.mapToDomain(poemTranslationOptionsItem) } returns poemLocale
        every { translationOptionsItemMapper.mapToDomain(originalTranslationOptionsItem) } returns originalLocale
        every { translationOptionsItemMapper.mapToDomain(TranslationOptionsItem.SystemTranslationOptions) } returns TranslationOptions.SystemLocale
        coEvery { useSpecificTranslation(capture(useSpecificTranslationParamsSlot)) } just Runs
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewIsReady normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        settingViewModel.viewIsReady()

        assertTrue(settingViewModel.uiState.value is SettingViewModel.UiState.Loaded)
        val uiState = settingViewModel.uiState.value as SettingViewModel.UiState.Loaded

        coVerify { getAvailableTranslations() }
        verify { translationOptionsItemMapper.mapToPresentation(supportedLocale) }
        assertEquals(
            uiState.availableTranslations,
            supportedTranslationOptionsItems.filterNot { it == originalTranslationOptionsItem })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setSelectedPoemLocale normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        settingViewModel.viewIsReady()
        settingViewModel.setSpecificTranslation(poemTranslationOptionsItem)

        coVerify { useSpecificTranslation(capture(useSpecificTranslationParamsSlot)) }
        assertEquals(poemLocale, useSpecificTranslationParamsSlot.captured.translationOptions)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onEventConsumed normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val event = SettingViewModel.Event.PopBack
        settingViewModel.viewIsReady()
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

        settingViewModel.viewIsReady()
        settingViewModel.popBack()

        val uiState = settingViewModel.uiState.value as SettingViewModel.UiState.Loaded

        assertTrue(uiState.events.contains(SettingViewModel.Event.PopBack))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `selectOriginalLanguage normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        settingViewModel.viewIsReady()
        settingViewModel.setToUseUntranslated()

        coVerify {
            useSpecificTranslation(
                UseSpecificTranslationParams(
                    originalLocale
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `selectSystemLanguage normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        settingViewModel.viewIsReady()
        settingViewModel.setToUseMatchSystemLanguageTranslation()

        coVerify {
            useSpecificTranslation(
                UseSpecificTranslationParams(
                    TranslationOptions.SystemLocale
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
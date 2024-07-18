package com.vuxur.khayyam.pages.setting

import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.usecase.getSelectedPoemLocale.GetSelectedPoemLocale
import com.vuxur.khayyam.domain.usecase.getSupportedLocales.GetSupportedLocale
import com.vuxur.khayyam.domain.usecase.setSelectedPoemLocale.SetSelectedPoemLocale
import com.vuxur.khayyam.domain.usecase.setSelectedPoemLocale.SetSelectedPoemLocaleParams
import com.vuxur.khayyam.mapper.LocaleItemMapper
import com.vuxur.khayyam.model.LocaleItem
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
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
    private val getSelectedPoemLocale: GetSelectedPoemLocale = mockk()
    private val getSupportedLocale: GetSupportedLocale = mockk()
    private val localeItemMapper: LocaleItemMapper = mockk()
    private val setSelectedPoemLocale: SetSelectedPoemLocale = mockk()
    private val poemLocale: Locale = mockk()
    private val poemLocaleItem: LocaleItem = mockk()
    private val supportedLocale: List<Locale> = mockk()
    private val supportedLocaleItem: List<LocaleItem> = mockk()
    private val setSelectedPoemLocaleParamsSlot = slot<SetSelectedPoemLocaleParams>()

    @BeforeEach
    fun setUp() {
        settingViewModel = SettingViewModel(
            getSelectedPoemLocale,
            getSupportedLocale,
            localeItemMapper,
            setSelectedPoemLocale,
        )

        coEvery { getSelectedPoemLocale() } returns flowOf(poemLocale)
        coEvery { getSupportedLocale() } returns supportedLocale
        coEvery { localeItemMapper.mapToPresentation(poemLocale) } returns poemLocaleItem
        coEvery { localeItemMapper.mapToPresentation(any<List<Locale>>()) } returns supportedLocaleItem
        coEvery { localeItemMapper.mapToDomain(poemLocaleItem) } returns poemLocale
        coEvery { setSelectedPoemLocale(capture(setSelectedPoemLocaleParamsSlot)) } just Runs
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewIsReady normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        settingViewModel.viewIsReady()

        assertTrue(settingViewModel.uiState.value is SettingViewModel.UiState.Loaded)
        val uiState = settingViewModel.uiState.value as SettingViewModel.UiState.Loaded

        coVerify { getSupportedLocale() }
        verify { localeItemMapper.mapToPresentation(supportedLocale) }
        assertEquals(uiState.supportedLocales, supportedLocaleItem)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setSelectedPoemLocale normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        settingViewModel.viewIsReady()
        settingViewModel.setSelectedPoemLocale(poemLocaleItem)
        val uiState = settingViewModel.uiState.value as SettingViewModel.UiState.Loaded

        coVerify { setSelectedPoemLocale(capture(setSelectedPoemLocaleParamsSlot)) }
        assertEquals(poemLocale, setSelectedPoemLocaleParamsSlot.captured.locale)
        assertTrue(uiState.events.contains(SettingViewModel.Event.popBack))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onEventConsumed normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val event = SettingViewModel.Event.popBack
        settingViewModel.viewIsReady()
        settingViewModel.setSelectedPoemLocale(poemLocaleItem)
        settingViewModel.onEventConsumed(event)
        val uiState = settingViewModel.uiState.value as SettingViewModel.UiState.Loaded

        assertFalse(uiState.events.contains(event))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
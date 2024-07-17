package com.vuxur.khayyam.pages.splashScreen

import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.usecase.getSelectedPoemLocale.GetSelectedPoemLocale
import com.vuxur.khayyam.mapper.LocaleItemMapper
import com.vuxur.khayyam.model.LocaleItem
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SplashScreenViewModelTest {
    private lateinit var viewModel: SplashScreenViewModel
    private val getSelectedPoemLocale: GetSelectedPoemLocale = mockk()
    private val localeItemMapper: LocaleItemMapper = mockk()

    @BeforeEach
    fun setUp() {
        viewModel = SplashScreenViewModel(
            getSelectedPoemLocale,
            localeItemMapper,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigate to setting (NoLocale)`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val mockSelectedPoemLocale: Locale.NoLocale = mockk()
        val mockSelectedPoemLocaleItem: LocaleItem.NoLocale = mockk()
        every { localeItemMapper.mapToPresentation(mockSelectedPoemLocale) } returns mockSelectedPoemLocaleItem
        coEvery { getSelectedPoemLocale() } returns flowOf(Locale.NoLocale)

        viewModel.viewIsReady()

        val uiState = (viewModel.uiState.value as SplashScreenViewModel.UiState.Initialized)
        assertEquals(SplashScreenViewModel.Event.Navigate.ToLanguageSetting, uiState.events.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigate to poemList (CustomLocale)`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val mockSelectedPoemLocale: Locale.CustomLocale = mockk()
        val mockSelectedPoemLocaleItem: LocaleItem.CustomLocale = mockk()
        every { localeItemMapper.mapToPresentation(mockSelectedPoemLocale) } returns mockSelectedPoemLocaleItem
        coEvery { getSelectedPoemLocale() } returns flowOf(mockSelectedPoemLocale)

        viewModel.viewIsReady()

        val uiState = (viewModel.uiState.value as SplashScreenViewModel.UiState.Initialized)
        assertEquals(SplashScreenViewModel.Event.Navigate.ToPoemList, uiState.events.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigate to poemList (SystemLocale)`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val mockSelectedPoemLocale: Locale.SystemLocale = mockk()
        val mockSelectedPoemLocaleItem: LocaleItem.SystemLocale = mockk()
        every { localeItemMapper.mapToPresentation(mockSelectedPoemLocale) } returns mockSelectedPoemLocaleItem
        coEvery { getSelectedPoemLocale() } returns flowOf(mockSelectedPoemLocale)

        viewModel.viewIsReady()

        val uiState = (viewModel.uiState.value as SplashScreenViewModel.UiState.Initialized)
        assertEquals(SplashScreenViewModel.Event.Navigate.ToPoemList, uiState.events.first())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onEventConsumed Test`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val mockSelectedPoemLocale: Locale.CustomLocale = mockk()
        val mockSelectedPoemLocaleItem: LocaleItem.CustomLocale = mockk()
        every { localeItemMapper.mapToPresentation(mockSelectedPoemLocale) } returns mockSelectedPoemLocaleItem
        coEvery { getSelectedPoemLocale() } returns flowOf(mockSelectedPoemLocale)

        viewModel.viewIsReady()

        val consumedEvent =
            (viewModel.uiState.value as SplashScreenViewModel.UiState.Initialized).events.first()
        viewModel.onEventConsumed(consumedEvent)
        val uiState = viewModel.uiState.value as SplashScreenViewModel.UiState.Initialized
        assertFalse(uiState.events.contains(consumedEvent))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
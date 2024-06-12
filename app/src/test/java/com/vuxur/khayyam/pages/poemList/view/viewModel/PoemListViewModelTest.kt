package com.vuxur.khayyam.pages.poemList.view.viewModel

import android.content.res.Resources
import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.usecase.getLastVisitedPoem.GetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.getPoems.GetPoems
import com.vuxur.khayyam.domain.usecase.getPoems.GetPoemsParams
import com.vuxur.khayyam.domain.usecase.getSelectedPoemLocale.GetSelectedPoemLocale
import com.vuxur.khayyam.domain.usecase.setLastVisitedPoem.SetLastVisitedPoem
import com.vuxur.khayyam.mapper.LocaleItemMapper
import com.vuxur.khayyam.mapper.PoemItemMapper
import com.vuxur.khayyam.model.LocaleItem
import com.vuxur.khayyam.model.PoemItem
import com.vuxur.khayyam.utils.getCurrentLocale
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PoemListViewModelTest {

    private lateinit var viewModel: PoemListViewModel
    private val getPoems: GetPoems = mockk()
    private val poemItemMapper: PoemItemMapper = mockk()
    private val getSelectedPoemLocale: GetSelectedPoemLocale = mockk()
    private val localeItemMapper: LocaleItemMapper = mockk()
    private val searchManager: SearchManager = mockk()
    private val setLastVisitedPoem: SetLastVisitedPoem = mockk()
    private val getLastVisitedPoem: GetLastVisitedPoem = mockk()

    @BeforeEach
    fun setUp() {
        viewModel = PoemListViewModel(
            getPoems,
            poemItemMapper,
            getSelectedPoemLocale,
            localeItemMapper,
            searchManager,
            setLastVisitedPoem,
            getLastVisitedPoem
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewIsReady normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Mock data
        val poems = listOf(
            mockk<Poem>(relaxed = true),
            mockk<Poem>(relaxed = true),
            mockk<Poem>(relaxed = true),
        )
        val poemItems = poems.map { mockk<PoemItem>(relaxed = true) }
        val selectedPoemLocale: Locale.CustomLocale = mockk()
        val selectedPoemLocaleItem: LocaleItem.CustomLocale = mockk()

        // Mock interactions
        coEvery { getSelectedPoemLocale() } returns flowOf(selectedPoemLocale)
        coEvery { getPoems(any()) } returns poems
        every { poemItemMapper.mapToPresentation(any<List<Poem>>()) } returns poemItems
        coEvery { getLastVisitedPoem() } returns flowOf(poems.first())
        every { poemItemMapper.mapToPresentation(poems.first()) } returns poemItems.first()
        every { localeItemMapper.mapToPresentation(selectedPoemLocale) } returns selectedPoemLocaleItem
        every { localeItemMapper.mapToDomain(selectedPoemLocaleItem) } returns selectedPoemLocale
        every { searchManager.checkSearchState(0) } returns mockk()

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        // Check the UiState content
        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded
        assertEquals(poemItems, uiState.poems)
        assertEquals(0, uiState.currentItemIndex)
        assertEquals(selectedPoemLocaleItem, uiState.selectedLocaleItem)

        // Verify that the correct functions were called
        coVerify { getSelectedPoemLocale() }
        coVerify { getPoems(any()) }
        coVerify { getLastVisitedPoem() }
        verify { poemItemMapper.mapToPresentation(any<List<Poem>>()) }
        verify { poemItemMapper.mapToPresentation(poems.first()) }
        verify { localeItemMapper.mapToPresentation(selectedPoemLocale) }
        verify { localeItemMapper.mapToDomain(selectedPoemLocaleItem) }
        verify { searchManager.checkSearchState(0) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigate to setting if selectedLocaleItem was NoLocale`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val selectedPoemLocale: Locale = Locale.NoLocale
        val selectedPoemLocaleItem: LocaleItem = LocaleItem.NoLocale

        // Mock interactions
        coEvery { getSelectedPoemLocale() } returns flowOf(selectedPoemLocale)
        coEvery { getLastVisitedPoem() } returns flowOf(null)
        every { localeItemMapper.mapToPresentation(selectedPoemLocale) } returns selectedPoemLocaleItem

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loading)

        // Check the UiState content
        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loading
        assertEquals(listOf(PoemListViewModel.Event.NavigateToLanguageSetting), uiState.events)

        // Verify that the correct functions were called
        coVerify { getSelectedPoemLocale() }
        verify { localeItemMapper.mapToPresentation(selectedPoemLocale) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewIsReady using SystemLocale`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Mock data
        val poems = listOf(
            mockk<Poem>(relaxed = true),
            mockk<Poem>(relaxed = true),
            mockk<Poem>(relaxed = true),
        )
        val poemItems = poems.map { mockk<PoemItem>(relaxed = true) }
        val selectedPoemLocale: Locale = Locale.SystemLocale
        val selectedPoemLocaleItem: LocaleItem = LocaleItem.SystemLocale
        val systemLocale: Locale = Locale.CustomLocale(java.util.Locale.US)
        val systemLocaleItem: LocaleItem = LocaleItem.CustomLocale(java.util.Locale.US)
        val resources = mockk<Resources>(relaxed = true)

        mockkStatic(Resources::class)
        mockkStatic(::getCurrentLocale)

        // Mock interactions
        coEvery { getSelectedPoemLocale() } returns flowOf(selectedPoemLocale)
        coEvery { getPoems(any()) } returns poems
        every { poemItemMapper.mapToPresentation(any<List<Poem>>()) } returns poemItems
        coEvery { getLastVisitedPoem() } returns flowOf(poems.first())
        every { poemItemMapper.mapToPresentation(poems.first()) } returns poemItems.first()
        every { localeItemMapper.mapToPresentation(selectedPoemLocale) } returns selectedPoemLocaleItem
        every { localeItemMapper.mapToDomain(systemLocaleItem) } returns systemLocale
        every { searchManager.checkSearchState(0) } returns mockk()
        every { getCurrentLocale(resources) } returns (systemLocale as Locale.CustomLocale).locale
        every { Resources.getSystem() } returns resources

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        // Check the UiState content
        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded
        assertEquals(poemItems, uiState.poems)
        assertEquals(0, uiState.currentItemIndex)
        assertEquals(systemLocaleItem, uiState.selectedLocaleItem)

        // Verify that the correct functions were called
        coVerify { getSelectedPoemLocale() }
        coVerify { getPoems(GetPoemsParams(locale = systemLocale)) }
        coVerify { getLastVisitedPoem() }
        verify { poemItemMapper.mapToPresentation(any<List<Poem>>()) }
        verify { poemItemMapper.mapToPresentation(poems.first()) }
        verify { localeItemMapper.mapToPresentation(selectedPoemLocale) }
        verify { localeItemMapper.mapToDomain(systemLocaleItem) }
        verify { searchManager.checkSearchState(0) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown(){
        Dispatchers.resetMain()
    }
}
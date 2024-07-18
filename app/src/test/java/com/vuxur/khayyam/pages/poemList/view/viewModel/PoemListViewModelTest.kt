package com.vuxur.khayyam.pages.poemList.view.viewModel

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.usecase.getLastVisitedPoem.GetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.getPoems.GetPoems
import com.vuxur.khayyam.domain.usecase.getPoems.GetPoemsParams
import com.vuxur.khayyam.domain.usecase.getSelectedPoemLocale.GetSelectedPoemLocale
import com.vuxur.khayyam.domain.usecase.setLastVisitedPoem.SetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.setLastVisitedPoem.SetLastVisitedPoemParams
import com.vuxur.khayyam.domain.usecase.setSelectedPoemLocale.SetSelectedPoemLocale
import com.vuxur.khayyam.domain.usecase.setSelectedPoemLocale.SetSelectedPoemLocaleParams
import com.vuxur.khayyam.mapper.LocaleItemMapper
import com.vuxur.khayyam.mapper.PoemItemMapper
import com.vuxur.khayyam.model.LocaleItem
import com.vuxur.khayyam.model.PoemItem
import com.vuxur.khayyam.utils.getCurrentLocale
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val setSelectedPoemLocale: SetSelectedPoemLocale = mockk()
    private val localeItemMapper: LocaleItemMapper = mockk()
    private val searchManager: SearchManager = mockk()
    private val setLastVisitedPoem: SetLastVisitedPoem = mockk()
    private val getLastVisitedPoem: GetLastVisitedPoem = mockk()

    // Mock data
    private val poems = listOf(
        mockk<Poem>(relaxed = true),
        mockk<Poem>(relaxed = true),
        mockk<Poem>(relaxed = true),
    )
    private val poemItems = poems.map { mockk<PoemItem>(relaxed = true) }
    private val selectedPoemLocale: Locale.CustomLocale = mockk()
    private val selectedPoemLocaleItem: LocaleItem.CustomLocale = mockk()
    private val imageFileOutputStreamProvider: ImageFileOutputStreamProviderImpl = mockk()
    private val imageFile: File = mockk()
    private val shareIntentProvider: ShareIntentProvider = mockk()
    private val mockIntent = mockk<Intent>(relaxed = true)

    @BeforeEach
    fun setUp() {
        viewModel = PoemListViewModel(
            getPoems,
            poemItemMapper,
            getSelectedPoemLocale,
            localeItemMapper,
            searchManager,
            setLastVisitedPoem,
            getLastVisitedPoem,
            imageFileOutputStreamProvider,
            imageFile,
            shareIntentProvider,
            setSelectedPoemLocale,
        )

        // Mock interactions
        coEvery { getSelectedPoemLocale() } returns flowOf(selectedPoemLocale)
        coEvery { getPoems(any()) } returns poems
        every { poemItemMapper.mapToPresentation(any<List<Poem>>()) } returns poemItems
        poemItems.forEachIndexed { index, poemItem ->
            every { poemItemMapper.mapToDomain(poemItem) } returns poems[index]
        }
        poems.forEachIndexed { index, poem ->
            every { poemItemMapper.mapToPresentation(poem) } returns poemItems[index]
        }
        coEvery { getLastVisitedPoem() } returns flowOf(poems.first())
        every { localeItemMapper.mapToPresentation(selectedPoemLocale) } returns selectedPoemLocaleItem
        every { localeItemMapper.mapToDomain(selectedPoemLocaleItem) } returns selectedPoemLocale
        every { localeItemMapper.mapToDomain(LocaleItem.SystemLocale) } returns Locale.SystemLocale
        every { searchManager.checkSearchState(any()) } returns mockk()
        coEvery { setLastVisitedPoem(any()) } just Runs
        every { shareIntentProvider.getShareTextIntent() } returns mockIntent
        every { shareIntentProvider.getShareImageIntent() } returns mockIntent
        every { mockIntent.setType(any()) } returns mockIntent
        every { mockIntent.putExtra(any(), any<String>()) } returns mockIntent
        every { mockIntent.putExtra(any(), any<Uri>()) } returns mockIntent
        every { mockIntent.setData(any()) } returns mockIntent
        coEvery { setSelectedPoemLocale(any()) } just Runs
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewIsReady normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

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
    fun `show language setting dialog if selectedLocaleItem was NoLocale`() = runTest {
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
        assertTrue(uiState.showLanguageSettingDialog)

        // Verify that the correct functions were called
        coVerify { getSelectedPoemLocale() }
        verify { localeItemMapper.mapToPresentation(selectedPoemLocale) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewIsReady using SystemLocale`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val selectedPoemLocale: Locale = Locale.SystemLocale
        val selectedPoemLocaleItem: LocaleItem = LocaleItem.SystemLocale
        val systemLocale: Locale = Locale.CustomLocale(java.util.Locale.US)
        val systemLocaleItem: LocaleItem = LocaleItem.CustomLocale(java.util.Locale.US)
        val resources = mockk<Resources>(relaxed = true)

        mockkStatic(Resources::class)
        mockkStatic(::getCurrentLocale)

        // Mock interactions
        coEvery { getSelectedPoemLocale() } returns flowOf(selectedPoemLocale)
        every { localeItemMapper.mapToPresentation(selectedPoemLocale) } returns selectedPoemLocaleItem
        every { localeItemMapper.mapToDomain(systemLocaleItem) } returns systemLocale
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
    @Test
    fun `setCurrentPoemIndex normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        viewModel.setCurrentPoemIndex(poemItems.indexOf(poemItems.last()))

        // Verify that the correct functions were called
        coVerify {
            setLastVisitedPoem(
                SetLastVisitedPoemParams(
                    lastVisitedPoem = poems.last()
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onLastVisitedPoemChanged normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        //MockData
        val lastVisitedPoemFlow = MutableStateFlow(poems.first())

        // Mock interactions
        coEvery { getLastVisitedPoem() } returns lastVisitedPoemFlow

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        lastVisitedPoemFlow.emit(poems.last())

        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded
        assertEquals(poemItems.indexOf(poemItems.last()), uiState.currentItemIndex)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigateToNearestResult normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Mock data
        val searchPhrase = "searchPhrase"
        val mockSearchState1 = PoemListViewModel.SearchState(
            Random.nextBoolean(),
            Random.nextBoolean(),
            Random.nextBoolean(),
        )
        val mockSearchState2 = PoemListViewModel.SearchState(
            mockSearchState1.hasResult.not(),
            mockSearchState1.hasNext.not(),
            mockSearchState1.hasPrevious.not(),
        )

        val lambdaCapture = slot<(PoemItem) -> Int>()
        coEvery {
            searchManager.nearestSearchResultIndex(
                any(),
                any(),
                any(),
                capture(lambdaCapture)
            )
        } returns poems.indexOf(poems.last())
        every { searchManager.checkSearchState(0) } returns mockSearchState1 andThen mockSearchState2

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        viewModel.navigateToNearestResult(searchPhrase)

        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded

        // Check lambda behavior
        poemItems.forEachIndexed { index, poemItem ->
            assertEquals(index, lambdaCapture.captured(poemItem))
        }

        // Verify that the correct functions were called
        coVerify {
            setLastVisitedPoem(
                SetLastVisitedPoemParams(
                    lastVisitedPoem = poems.last()
                )
            )
        }
        coVerifySequence {
            searchManager.checkSearchState(0)
            searchManager.nearestSearchResultIndex(
                searchPhrase,
                uiState.selectedLocaleItem,
                uiState.currentItemIndex,
                any()
            )
            searchManager.checkSearchState(0)
        }
        assertEquals(mockSearchState2, uiState.searchState)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigateToNearestResult when the result is null`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Mock data
        val searchPhrase = "searchPhrase"
        val mockSearchState1 = PoemListViewModel.SearchState(
            Random.nextBoolean(),
            Random.nextBoolean(),
            Random.nextBoolean(),
        )
        val mockSearchState2 = PoemListViewModel.SearchState(
            mockSearchState1.hasResult.not(),
            mockSearchState1.hasNext.not(),
            mockSearchState1.hasPrevious.not(),
        )

        val lambdaCapture = slot<(PoemItem) -> Int>()
        coEvery {
            searchManager.nearestSearchResultIndex(
                any(),
                any(),
                any(),
                any(),
            )
        } returns null
        every { searchManager.checkSearchState(0) } returns mockSearchState1 andThen mockSearchState2

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        viewModel.navigateToNearestResult(searchPhrase)

        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded

        // Verify that the correct functions were called
        coVerify(exactly = 0) {
            setLastVisitedPoem(
                any()
            )
        }
        coVerifySequence {
            searchManager.checkSearchState(0)
            searchManager.nearestSearchResultIndex(
                searchPhrase,
                uiState.selectedLocaleItem,
                uiState.currentItemIndex,
                any()
            )
            searchManager.checkSearchState(0)
        }
        assertEquals(mockSearchState2, uiState.searchState)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigateToNextResult normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Mock interaction
        every { searchManager.nextResult(any()) } returns poemItems.indexOf(poemItems.last())

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded

        viewModel.navigateToNextResult()

        verify { searchManager.nextResult(uiState.currentItemIndex) }
        coVerify {
            setLastVisitedPoem(
                SetLastVisitedPoemParams(
                    lastVisitedPoem = poems.last()
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigateToNextResult when next result is null`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Mock interaction
        every { searchManager.nextResult(any()) } returns null

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded

        viewModel.navigateToNextResult()

        verify { searchManager.nextResult(uiState.currentItemIndex) }
        coVerify(exactly = 0) {
            setLastVisitedPoem(
                SetLastVisitedPoemParams(
                    lastVisitedPoem = poems.last()
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigateToPreviousResult normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Mock interaction
        every { searchManager.previousResult(any()) } returns poemItems.indexOf(poemItems.last())

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded

        viewModel.navigateToPreviousResult()

        verify { searchManager.previousResult(uiState.currentItemIndex) }
        coVerify {
            setLastVisitedPoem(
                SetLastVisitedPoemParams(
                    lastVisitedPoem = poems.last()
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigateToPreviousResult when next result is null`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Mock interaction
        every { searchManager.previousResult(any()) } returns null

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded

        viewModel.navigateToPreviousResult()

        verify { searchManager.previousResult(uiState.currentItemIndex) }
        coVerify(exactly = 0) {
            setLastVisitedPoem(
                SetLastVisitedPoemParams(
                    lastVisitedPoem = poems.last()
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `sharePoemText normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        viewModel.sharePoemText()
        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded

        // Verify the Intent interactions
        verify {
            mockIntent.putExtra(
                Intent.EXTRA_TEXT,
                String.format(
                    "%s\n%s\n%s\n%s",
                    poemItems.first().hemistich1,
                    poemItems.first().hemistich2,
                    poemItems.first().hemistich3,
                    poemItems.first().hemistich4
                )
            )
        }
        verify { shareIntentProvider.getShareTextIntent() }
        assertEquals(
            (uiState.events.first() as PoemListViewModel.Event.SharePoemText).shareIntent,
            mockIntent
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `copyPoem normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        //mock
        val mockedClipboard: ClipboardManager = mockk(relaxed = true)
        every { mockedClipboard.setText(any()) } just Runs
        val mockedPoemText = String.format(
            "%s\n%s\n%s\n%s",
            poemItems.first().hemistich1,
            poemItems.first().hemistich2,
            poemItems.first().hemistich3,
            poemItems.first().hemistich4
        )

        viewModel.copyPoem(mockedClipboard)
        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded

        verify { mockedClipboard.setText(AnnotatedString(mockedPoemText)) }

        assertEquals(
            (uiState.events.first() as PoemListViewModel.Event.CopyPoemText).copiedPoem,
            mockedPoemText
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `randomPoem normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        val poemSlot = slot<SetLastVisitedPoemParams>()
        coEvery { setLastVisitedPoem(capture(poemSlot)) } just Runs

        viewModel.randomPoem()

        coVerify {
            setLastVisitedPoem(any())
        }

        assertTrue(poems.contains(poemSlot.captured.lastVisitedPoem))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `sharePoemImageUri normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val mockPoemUri: Uri = mockk(relaxed = true)

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        viewModel.sharePoemImageUri(mockPoemUri)
        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded

        // Verify the Intent interactions
        verify { mockIntent.setData(mockPoemUri) }
        verify { mockIntent.putExtra(Intent.EXTRA_STREAM, mockPoemUri) }
        verify { shareIntentProvider.getShareImageIntent() }

        assertEquals(
            (uiState.events.first() as PoemListViewModel.Event.SharePoemText).shareIntent,
            mockIntent
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `sharePoemImage normal case`() = runTest {

        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Call the function under test
        viewModel.viewIsReady()

        val mockedBitmap: Bitmap = mockk()
        val outputStream: FileOutputStream = mockk()

        every { mockedBitmap.compress(any(), any(), any()) } returns true
        every { outputStream.close() } just Runs
        every { imageFileOutputStreamProvider.getOutputStream() } returns outputStream

        viewModel.sharePoemImage(mockedBitmap)

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)
        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded

        assertEquals(
            (uiState.events.first() as PoemListViewModel.Event.SharePoemImage).imageToShare,
            imageFile
        )
        verify {
            mockedBitmap.compress(
                Bitmap.CompressFormat.PNG,
                100,
                outputStream
            )
        }
        verify { outputStream.close() }
        verify { imageFileOutputStreamProvider.getOutputStream() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onEventConsumed normal case loaded uiState`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Call the function under test
        viewModel.viewIsReady()

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)
        val mockedClipboard: ClipboardManager = mockk(relaxed = true)
        every { mockedClipboard.setText(any()) } just Runs

        viewModel.copyPoem(mockedClipboard)
        val consumedEvent =
            (viewModel.uiState.value as PoemListViewModel.UiState.Loaded).events.first()
        viewModel.onEventConsumed(consumedEvent)
        val uiStateEvents = (viewModel.uiState.value as PoemListViewModel.UiState.Loaded).events

        assertTrue(!uiStateEvents.contains(consumedEvent))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onEventConsumed normal case loading uiState`() = runTest {
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
        viewModel.navigateToSetting()

        val consumedEvent =
            (viewModel.uiState.value as PoemListViewModel.UiState.Loading).events.first()
        viewModel.onEventConsumed(consumedEvent)
        val uiStateEvents = (viewModel.uiState.value as PoemListViewModel.UiState.Loading).events

        assertTrue(!uiStateEvents.contains(consumedEvent))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `useSystemLanguage normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        viewModel.useSystemLanguage()
        coVerify { setSelectedPoemLocale(SetSelectedPoemLocaleParams(Locale.SystemLocale)) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
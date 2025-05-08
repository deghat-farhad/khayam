package com.vuxur.khayyam.pages.poemList.view.viewModel

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.model.Translation
import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.domain.usecase.poems.getPoems.GetPoems
import com.vuxur.khayyam.domain.usecase.settings.lastVositedPoem.getLastVisitedPoem.GetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.settings.lastVositedPoem.setLastVisitedPoem.SetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.settings.lastVositedPoem.setLastVisitedPoem.SetLastVisitedPoemParams
import com.vuxur.khayyam.domain.usecase.settings.translation.getSelectedTranslationOption.GetSelectedTranslationOption
import com.vuxur.khayyam.domain.usecase.settings.translation.useMatchingSystemLanguageTranslation.UseMatchSystemLanguageTranslation
import com.vuxur.khayyam.domain.usecase.settings.translation.useUntranslated.UseUntranslated
import com.vuxur.khayyam.mapper.PoemItemMapper
import com.vuxur.khayyam.mapper.TranslationItemMapper
import com.vuxur.khayyam.mapper.TranslationOptionsItemMapper
import com.vuxur.khayyam.model.PoemItem
import com.vuxur.khayyam.model.TranslationItem
import com.vuxur.khayyam.model.TranslationOptionsItem
import com.vuxur.khayyam.ui.pages.poemList.view.viewModel.ImageFileOutputStreamProviderImpl
import com.vuxur.khayyam.ui.pages.poemList.view.viewModel.PoemListViewModel
import com.vuxur.khayyam.ui.pages.poemList.view.viewModel.SearchManager
import com.vuxur.khayyam.ui.pages.poemList.view.viewModel.ShareIntentProvider
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PoemListViewModelTest {

    private lateinit var viewModel: PoemListViewModel
    private val getPoems: GetPoems = mockk()
    private val poemItemMapper: PoemItemMapper = mockk()
    private val getSelectedTranslationOption: GetSelectedTranslationOption = mockk()
    private val useMatchSystemLanguageTranslation: UseMatchSystemLanguageTranslation = mockk()
    private val useUntranslated: UseUntranslated = mockk()
    private val translationOptionsItemMapper: TranslationOptionsItemMapper = mockk()
    private val translationItemMapper: TranslationItemMapper = mockk()
    private val searchManager: SearchManager = mockk()
    private val setLastVisitedPoem: SetLastVisitedPoem = mockk()
    private val getLastVisitedPoem: GetLastVisitedPoem = mockk()

    // Mock data
    private val poems = listOf(
        mockk<Poem>(),
        mockk<Poem>(),
        mockk<Poem>(),
    )
    private val poemItems = poems.map { mockk<PoemItem>(relaxed = true) }
    private val specificTranslation: TranslationOptions.Specific = mockk(relaxed = true)
    private val specificTranslationItem: TranslationOptionsItem.Specific = mockk(relaxed = true)
    private val imageFileOutputStreamProvider: ImageFileOutputStreamProviderImpl = mockk()
    private val imageFile: File = mockk()
    private val shareIntentProvider: ShareIntentProvider = mockk()
    private val mockIntent = mockk<Intent>(relaxed = true)
    private val translation: Translation = mockk()
    private val translationItem: TranslationItem = mockk()
    private val lastVisitedPoemIndex = poems.size - 2
    private val lastVisitedPoem = poems[lastVisitedPoemIndex]
    private val searchState: PoemListViewModel.SearchState = PoemListViewModel.SearchState(
        searchPhrase = "searchPhrase",
        hasResult = true,
        hasNext = false,
        hasPrevious = false,
    )
    private val deviceLocale: Locale = mockk()

    @BeforeEach
    fun setUp() {
        viewModel = PoemListViewModel(
            getPoems,
            poemItemMapper,
            getSelectedTranslationOption,
            translationOptionsItemMapper,
            searchManager,
            setLastVisitedPoem,
            getLastVisitedPoem,
            imageFileOutputStreamProvider,
            imageFile,
            shareIntentProvider,
            useMatchSystemLanguageTranslation,
            useUntranslated
        )

        // Mock interactions
        coEvery { getSelectedTranslationOption(any()) } returns flowOf(specificTranslation)
        coEvery { getPoems(any()) } returns poems
        every { poemItemMapper.mapToPresentation(any<List<Poem>>()) } returns poemItems
        poemItems.forEachIndexed { index, poemItem ->
            every { poemItemMapper.mapToDomain(poemItem) } returns poems[index]
            every { poemItem.translation } returns translationItem
        }
        poems.forEachIndexed { index, poem ->
            every { poemItemMapper.mapToPresentation(poem) } returns poemItems[index]
            every { poem.translation } returns translation
        }
        coEvery { getLastVisitedPoem() } returns flowOf(lastVisitedPoem)
        every { specificTranslationItem.translation } returns translationItem
        every { specificTranslation.translation } returns translation
        every { translationOptionsItemMapper.mapToPresentation(specificTranslation) } returns specificTranslationItem
        every { translationOptionsItemMapper.mapToDomain(specificTranslationItem) } returns specificTranslation
        every {
            translationOptionsItemMapper.mapToDomain(
                TranslationOptionsItem.MatchDeviceLanguage.Available(
                    translationItem
                )
            )
        } returns TranslationOptions.MatchDeviceLanguage.Available(translation)
        every { translationItemMapper.mapToDomain(translationItem) } returns translation
        every { translationItemMapper.mapToPresentation(translation) } returns translationItem
        every { searchManager.checkSearchState(any()) } returns searchState
        coEvery { setLastVisitedPoem(any()) } just Runs
        every { shareIntentProvider.getShareTextIntent() } returns mockIntent
        every { shareIntentProvider.getShareImageIntent() } returns mockIntent
        every { mockIntent.setType(any()) } returns mockIntent
        every { mockIntent.putExtra(any(), any<String>()) } returns mockIntent
        every { mockIntent.putExtra(any(), any<Uri>()) } returns mockIntent
        every { mockIntent.setData(any()) } returns mockIntent
        coEvery { useUntranslated() } just Runs
        coEvery { useMatchSystemLanguageTranslation() } just Runs
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewIsReady normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Call the function under test
        viewModel.viewIsReady(deviceLocale)

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        // Check the UiState content
        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded
        assertEquals(poemItems, uiState.poems)
        assertEquals(lastVisitedPoemIndex, uiState.currentItemIndex)
        assertEquals(specificTranslationItem.translation, uiState.translation)
        assertEquals(false, uiState.showTranslationDecision)
        assertEquals(searchState, uiState.searchState)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `show language setting dialog if selected TranslationOption was None`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val selectedTranslationOption: TranslationOptions = TranslationOptions.None
        val selectedTranslationOptionsItem: TranslationOptionsItem =
            TranslationOptionsItem.None
        val selectedTranslationFlow = MutableStateFlow(selectedTranslationOption)
        val matchDeviceTranslation = TranslationOptions.MatchDeviceLanguage.Available(translation)
        val matchDeviceTranslationItem =
            TranslationOptionsItem.MatchDeviceLanguage.Available(translationItem)
        // Mock interactions
        coEvery { useMatchSystemLanguageTranslation() } answers {
            selectedTranslationFlow.tryEmit(matchDeviceTranslation)
        }
        coEvery { getSelectedTranslationOption(any()) } returns selectedTranslationFlow
        //coEvery { getLastVisitedPoem() } returns flowOf(null)
        every { translationOptionsItemMapper.mapToPresentation(selectedTranslationOption) } returns selectedTranslationOptionsItem
        every { translationOptionsItemMapper.mapToPresentation(matchDeviceTranslation) } returns matchDeviceTranslationItem

        // Call the function under test
        viewModel.viewIsReady(deviceLocale)

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        // Check the UiState content
        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded
        assertEquals(poemItems, uiState.poems)
        assertEquals(lastVisitedPoemIndex, uiState.currentItemIndex)
        assertEquals(matchDeviceTranslationItem.translation, uiState.translation)
        assertTrue(uiState.showTranslationDecision)
        assertEquals(searchState, uiState.searchState)
    }

    /*@OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewIsReady using SystemLocale`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val selectedPoemLocale: TranslationOptions = TranslationOptions.SystemLocale
        val selectedPoemTranslationOptionsItem: TranslationOptionsItem =
            TranslationOptionsItem.SystemTranslationOptions
        val systemLocale: TranslationOptions = TranslationOptions.Specific(java.util.Locale.US)
        val systemTranslationOptionsItem: TranslationOptionsItem =
            TranslationOptionsItem.CustomTranslationOptions(java.util.Locale.US)
        val resources = mockk<Resources>(relaxed = true)

        mockkStatic(Resources::class)
        mockkStatic(::getCurrentLocale)

        // Mock interactions
        coEvery { getSelectedTranslationOption() } returns flowOf(selectedPoemLocale)
        every { translationOptionsItemMapper.mapToPresentation(selectedPoemLocale) } returns selectedPoemTranslationOptionsItem
        every { translationOptionsItemMapper.mapToDomain(systemTranslationOptionsItem) } returns systemLocale
        every { getCurrentLocale(resources) } returns (systemLocale as TranslationOptions.Specific).locale
        every { Resources.getSystem() } returns resources

        // Call the function under test
        viewModel.viewIsReady(deviceLocale)

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        // Check the UiState content
        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded
        assertEquals(poemItems, uiState.poems)
        assertEquals(0, uiState.currentItemIndex)
        assertEquals(systemTranslationOptionsItem, uiState.selectedTranslationOptionsItem)

        // Verify that the correct functions were called
        coVerify { getSelectedTranslationOption() }
        coVerify { getPoems(GetPoemsParams(translationOptions = systemLocale)) }
        coVerify { getLastVisitedPoem() }
        verify { poemItemMapper.mapToPresentation(any<List<Poem>>()) }
        verify { poemItemMapper.mapToPresentation(poems.first()) }
        verify { translationOptionsItemMapper.mapToPresentation(selectedPoemLocale) }
        verify { translationOptionsItemMapper.mapToDomain(systemTranslationOptionsItem) }
        verify { searchManager.checkSearchState(0) }
    }*/

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewIsReady lastVisitedPoem doesn't exist`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val mockPoem: Poem = mockk()
        val mockLastVisitedPoemFlow = MutableStateFlow(poems.last())

        coEvery { getLastVisitedPoem() } returns mockLastVisitedPoemFlow
        every { poemItemMapper.mapToPresentation(mockPoem) } returns mockk()

        viewModel.viewIsReady(deviceLocale)

        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded
        assertEquals(2, uiState.currentItemIndex)

        mockLastVisitedPoemFlow.value = mockPoem
        assertEquals(2, uiState.currentItemIndex)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setCurrentPoemIndex normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Call the function under test
        viewModel.viewIsReady(deviceLocale)

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
        viewModel.viewIsReady(deviceLocale)

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)
        var uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded
        assertEquals(poemItems.indexOf(poemItems.first()), uiState.currentItemIndex)

        lastVisitedPoemFlow.emit(poems.last())

        uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded
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
            searchPhrase,
            Random.nextBoolean(),
            Random.nextBoolean(),
            Random.nextBoolean(),
        )
        val mockSearchState2 = PoemListViewModel.SearchState(
            searchPhrase,
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
        every { searchManager.checkSearchState(lastVisitedPoemIndex) } returns mockSearchState1 andThen mockSearchState2

        // Call the function under test
        viewModel.viewIsReady(deviceLocale)

        // Verify the UI state
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
            searchPhrase,
            Random.nextBoolean(),
            Random.nextBoolean(),
            Random.nextBoolean(),
        )
        val mockSearchState2 = PoemListViewModel.SearchState(
            searchPhrase,
            mockSearchState1.hasResult.not(),
            mockSearchState1.hasNext.not(),
            mockSearchState1.hasPrevious.not(),
        )

        coEvery {
            searchManager.nearestSearchResultIndex(
                any(),
                any(),
                any(),
                any(),
            )
        } returns null
        every { searchManager.checkSearchState(lastVisitedPoemIndex) } returns mockSearchState1 andThen mockSearchState2

        // Call the function under test
        viewModel.viewIsReady(deviceLocale)

        // Verify the UI state
        viewModel.navigateToNearestResult(searchPhrase)

        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded

        // Verify that the correct functions were called
        coVerify(exactly = 0) {
            setLastVisitedPoem(
                any()
            )
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
        viewModel.viewIsReady(deviceLocale)

        // Verify the UI state
        viewModel.navigateToNextResult()

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
        viewModel.viewIsReady(deviceLocale)

        // Verify the UI state
        viewModel.navigateToNextResult()

        coVerify(exactly = 0) {
            setLastVisitedPoem(
                any()
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
        viewModel.viewIsReady(deviceLocale)

        // Verify the UI state
        viewModel.navigateToPreviousResult()

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
        viewModel.viewIsReady(deviceLocale)

        // Verify the UI state
        viewModel.navigateToPreviousResult()

        coVerify(exactly = 0) {
            setLastVisitedPoem(
                any()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `sharePoemText normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Call the function under test
        viewModel.viewIsReady(deviceLocale)

        // Verify the UI state
        viewModel.sharePoemText()
        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded

        // Verify the Intent interactions
        verify {
            mockIntent.putExtra(
                Intent.EXTRA_TEXT,
                String.format(
                    "%s\n%s\n%s\n%s",
                    poemItems[lastVisitedPoemIndex].hemistich1,
                    poemItems[lastVisitedPoemIndex].hemistich2,
                    poemItems[lastVisitedPoemIndex].hemistich3,
                    poemItems[lastVisitedPoemIndex].hemistich4
                )
            )
        }
        assertEquals(
            mockIntent,
            (uiState.events.first() as PoemListViewModel.Event.SharePoemText).shareIntent
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `copyPoem normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Call the function under test
        viewModel.viewIsReady(deviceLocale)

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
            mockedPoemText,
            (uiState.events.first() as PoemListViewModel.Event.CopyPoemText).copiedPoem
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `randomPoem normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Call the function under test
        viewModel.viewIsReady(deviceLocale)

        // Verify the UI state
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
        viewModel.viewIsReady(deviceLocale)

        // Verify the UI state
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)

        viewModel.sharePoemImageUri(mockPoemUri)
        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded

        // Verify the Intent interactions
        verify { mockIntent.putExtra(Intent.EXTRA_STREAM, mockPoemUri) }
        verify { shareIntentProvider.getShareImageIntent() }

        assertEquals(
            mockIntent,
            (uiState.events.first() as PoemListViewModel.Event.SharePoemText).shareIntent
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `sharePoemImage normal case`() = runTest {

        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        // Call the function under test
        viewModel.viewIsReady(deviceLocale)

        val mockedBitmap: Bitmap = mockk()
        val outputStream: FileOutputStream = mockk()

        every { mockedBitmap.compress(any(), any(), any()) } returns true
        every { outputStream.close() } just Runs
        every { imageFileOutputStreamProvider.getOutputStream() } returns outputStream

        viewModel.sharePoemImage(mockedBitmap)

        // Verify the UI state
        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loaded

        assertEquals(
            imageFile,
            (uiState.events.first() as PoemListViewModel.Event.SharePoemImage).imageToShare
        )
        verify { outputStream.close() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onEventConsumed normal case loaded uiState`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        viewModel.viewIsReady(deviceLocale)

        val mockedClipboard: ClipboardManager = mockk(relaxed = true)
        every { mockedClipboard.setText(any()) } just Runs

        viewModel.copyPoem(mockedClipboard)
        val consumedEvent =
            (viewModel.uiState.value as PoemListViewModel.UiState.Loaded).events.first()
        viewModel.onEventConsumed(consumedEvent)
        val uiStateEvents = (viewModel.uiState.value as PoemListViewModel.UiState.Loaded).events

        assertFalse(uiStateEvents.contains(consumedEvent))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigateToSetting normal case (Loaded)`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        viewModel.viewIsReady(deviceLocale)
        viewModel.navigateToSetting()

        val uiStateEvents = (viewModel.uiState.value as PoemListViewModel.UiState.Loaded).events
        assertTrue(uiStateEvents.contains(PoemListViewModel.Event.NavigateToLanguageSetting))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `NONE translation as uiState is Loaded`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val untranslatedTranslationOption: TranslationOptions.Untranslated = mockk()
        val untranslatedTranslationOptionItem: TranslationOptionsItem.Untranslated = mockk()
        val noneTranslationOption: TranslationOptions.None = mockk()
        val noneTranslationOptionItem: TranslationOptionsItem.None = mockk()

        val translationFlow = MutableStateFlow<TranslationOptions>(untranslatedTranslationOption)

        coEvery { getSelectedTranslationOption(any()) } returns translationFlow
        every { translationOptionsItemMapper.mapToPresentation(untranslatedTranslationOption) } returns untranslatedTranslationOptionItem
        every { translationOptionsItemMapper.mapToDomain(untranslatedTranslationOptionItem) } returns untranslatedTranslationOption
        every { translationOptionsItemMapper.mapToPresentation(noneTranslationOption) } returns noneTranslationOptionItem
        every { translationOptionsItemMapper.mapToDomain(noneTranslationOptionItem) } returns noneTranslationOption

        viewModel.viewIsReady(deviceLocale)

        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loaded)
        translationFlow.emit(noneTranslationOption)
        assertTrue(viewModel.uiState.value is PoemListViewModel.UiState.Loading)
        val uiState = viewModel.uiState.value as PoemListViewModel.UiState.Loading
        assertFalse(uiState.isTranslationOptionSelected)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setToUseUntranslated normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        viewModel.viewIsReady(deviceLocale)

        viewModel.setToUseUntranslated()

        coVerify { useUntranslated() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getPoem error case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        coEvery { getPoems(any()) } throws Exception()

        val uiStateBeforeGetPoemsCall = viewModel.uiState.value

        viewModel.viewIsReady(deviceLocale)

        assertEquals(uiStateBeforeGetPoemsCall, viewModel.uiState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
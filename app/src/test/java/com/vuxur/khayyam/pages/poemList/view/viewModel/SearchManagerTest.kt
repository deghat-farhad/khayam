package com.vuxur.khayyam.pages.poemList.view.viewModel

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.domain.usecase.findPoems.FindPoems
import com.vuxur.khayyam.domain.usecase.findPoems.FindPoemsParams
import com.vuxur.khayyam.mapper.PoemItemMapper
import com.vuxur.khayyam.mapper.TranslationOptionsItemMapper
import com.vuxur.khayyam.model.PoemItem
import com.vuxur.khayyam.model.TranslationOptionsItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.math.abs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchManagerTest() {

    private lateinit var searchManager: SearchManager
    private val findPoems: FindPoems = mockk()
    private val poemItemMapper: PoemItemMapper = mockk()
    private val translationOptionsItemMapper: TranslationOptionsItemMapper = mockk()
    private val selectedPoemTranslationOptionsItem: TranslationOptionsItem.CustomTranslationOptions =
        mockk()
    private val selectedPoemLocale: TranslationOptions.Specific = mockk()
    private val poems = listOf(
        mockk<Poem>(relaxed = true),
        mockk<Poem>(relaxed = true),
        mockk<Poem>(relaxed = true),
    )
    private val poemItems = poems.map { mockk<PoemItem>(relaxed = true) }
    val indexOf: (poemItem: PoemItem) -> Int = mockk()
    private val searchPhrase = "dummy"
    private val indexes = (0..500).shuffled().take(poemItems.size)
    private val sortedIndexes = indexes.sorted()
    private val currentPoemItemIndex = 0

    @BeforeEach
    fun setup() {
        searchManager = SearchManager(
            findPoems,
            poemItemMapper,
            translationOptionsItemMapper,
        )

        coEvery { findPoems.invoke(any()) } returns poems
        every { translationOptionsItemMapper.mapToDomain(selectedPoemTranslationOptionsItem) } returns selectedPoemLocale
        every { poemItemMapper.mapToPresentation(poems) } returns poemItems
        poemItems.forEachIndexed { index, poemItem ->
            every { indexOf(poemItem) } returns indexes[index]
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nearestSearchResultIndex no ceilingIndex`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val currentPoemItemIndex = indexes.max() + 1

        val nearestSearchResultIndex = searchManager.nearestSearchResultIndex(
            searchPhrase,
            selectedPoemTranslationOptionsItem,
            currentPoemItemIndex,
            indexOf,
        )

        val nearestDistance = abs(currentPoemItemIndex - nearestSearchResultIndex!!)
        indexes.forEach { index ->
            assertTrue(abs(currentPoemItemIndex - index) >= nearestDistance)
        }

        coVerify {
            findPoems.invoke(
                FindPoemsParams(
                    searchPhrase,
                    selectedPoemLocale
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nearestSearchResultIndex no floorIndex`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val currentPoemItemIndex = indexes.min() - 1

        val nearestSearchResultIndex = searchManager.nearestSearchResultIndex(
            searchPhrase,
            selectedPoemTranslationOptionsItem,
            currentPoemItemIndex,
            indexOf,
        )

        val nearestDistance = abs(currentPoemItemIndex - nearestSearchResultIndex!!)
        indexes.forEach { index ->
            assertTrue(abs(currentPoemItemIndex - index) >= nearestDistance)
        }

        coVerify {
            findPoems.invoke(
                FindPoemsParams(
                    searchPhrase,
                    selectedPoemLocale
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nearestSearchResultIndex at the left`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val currentPoemItemIndex: Int =
            ((sortedIndexes.last() + sortedIndexes[sortedIndexes.lastIndex - 1]) / 2) - 1

        val nearestSearchResultIndex = searchManager.nearestSearchResultIndex(
            searchPhrase,
            selectedPoemTranslationOptionsItem,
            currentPoemItemIndex,
            indexOf,
        )

        val nearestDistance = abs(currentPoemItemIndex - nearestSearchResultIndex!!)
        indexes.forEach { index ->
            assertTrue(abs(currentPoemItemIndex - index) >= nearestDistance)
        }

        coVerify {
            findPoems.invoke(
                FindPoemsParams(
                    searchPhrase,
                    selectedPoemLocale
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nearestSearchResultIndex at the right`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val currentPoemItemIndex: Int =
            ((sortedIndexes.last() + sortedIndexes[sortedIndexes.lastIndex - 1]) / 2) + 1

        val nearestSearchResultIndex = searchManager.nearestSearchResultIndex(
            searchPhrase,
            selectedPoemTranslationOptionsItem,
            currentPoemItemIndex,
            indexOf,
        )

        val nearestDistance = abs(currentPoemItemIndex - nearestSearchResultIndex!!)
        indexes.forEach { index ->
            assertTrue(abs(currentPoemItemIndex - index) >= nearestDistance)
        }

        coVerify {
            findPoems.invoke(
                FindPoemsParams(
                    searchPhrase,
                    selectedPoemLocale
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nearestSearchResultIndex no result`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        coEvery { findPoems.invoke(any()) } returns emptyList()
        every { poemItemMapper.mapToPresentation(any<List<Poem>>()) } returns emptyList()

        val nearestSearchResultIndex = searchManager.nearestSearchResultIndex(
            searchPhrase,
            selectedPoemTranslationOptionsItem,
            currentPoemItemIndex,
            indexOf,
        )

        assertEquals(null, nearestSearchResultIndex)

        coVerify {
            findPoems.invoke(
                FindPoemsParams(
                    searchPhrase,
                    selectedPoemLocale
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nearestSearchResultIndex blank search phrase`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val searchPhrase = ""

        val nearestSearchResultIndex = searchManager.nearestSearchResultIndex(
            searchPhrase,
            selectedPoemTranslationOptionsItem,
            currentPoemItemIndex,
            indexOf,
        )

        assertEquals(null, nearestSearchResultIndex)

        coVerify(exactly = 0) { findPoems.invoke(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nextResult normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        searchManager.nearestSearchResultIndex(
            searchPhrase,
            selectedPoemTranslationOptionsItem,
            currentPoemItemIndex,
            indexOf,
        )

        val nexResult = searchManager.nextResult(sortedIndexes[sortedIndexes.lastIndex - 1])

        assertEquals(sortedIndexes.last(), nexResult)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `nextResult there is no nextResult`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        searchManager.nearestSearchResultIndex(
            searchPhrase,
            selectedPoemTranslationOptionsItem,
            currentPoemItemIndex,
            indexOf,
        )

        val nexResult = searchManager.nextResult(sortedIndexes.last())

        assertEquals(null, nexResult)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `previousResult normal case`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        searchManager.nearestSearchResultIndex(
            searchPhrase,
            selectedPoemTranslationOptionsItem,
            currentPoemItemIndex,
            indexOf,
        )

        val previousResult = searchManager.previousResult(sortedIndexes[1])

        assertEquals(sortedIndexes.first(), previousResult)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `previousResult there is no previousResult`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        searchManager.nearestSearchResultIndex(
            searchPhrase,
            selectedPoemTranslationOptionsItem,
            currentPoemItemIndex,
            indexOf,
        )

        val previousResult = searchManager.previousResult(sortedIndexes.first())

        assertEquals(null, previousResult)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `checkSearchState hasResult, hasNext, HasPrevious`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val currentPoemItemIndex = (sortedIndexes.last() + sortedIndexes.first()) / 2
        searchManager.nearestSearchResultIndex(
            searchPhrase,
            selectedPoemTranslationOptionsItem,
            currentPoemItemIndex,
            indexOf,
        )
        val searchState = searchManager.checkSearchState(currentPoemItemIndex)
        val expectedSearchStare = PoemListViewModel.SearchState(
            hasResult = true,
            hasNext = true,
            hasPrevious = true,
        )

        assertEquals(expectedSearchStare, searchState)
    }

    @Test
    fun `checkSearchState noResult, noNext, noPrevious`() {
        val searchState = searchManager.checkSearchState(currentPoemItemIndex)
        val expectedSearchStare = PoemListViewModel.SearchState(
            hasResult = false,
            hasNext = false,
            hasPrevious = false,
        )

        assertEquals(expectedSearchStare, searchState)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
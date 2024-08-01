package com.vuxur.khayyam.pages.poemList.view.viewModel

import com.vuxur.khayyam.domain.usecase.findPoems.FindPoems
import com.vuxur.khayyam.domain.usecase.findPoems.FindPoemsParams
import com.vuxur.khayyam.mapper.PoemItemMapper
import com.vuxur.khayyam.mapper.TranslationItemMapper
import com.vuxur.khayyam.model.PoemItem
import com.vuxur.khayyam.model.TranslationItem
import java.util.TreeSet
import javax.inject.Inject
import kotlin.math.abs

class SearchManager @Inject constructor(
    private val findPoems: FindPoems,
    private val poemItemMapper: PoemItemMapper,
    private val translationItemMapper: TranslationItemMapper,
) {
    private val searchResultSet = TreeSet<Int>()
    private var searchPhrase: String? = null

    suspend fun nearestSearchResultIndex(
        searchPhrase: String,
        translation: TranslationItem,
        currentPoemItemIndex: Int,
        indexOf: (poemItem: PoemItem) -> Int,
    ): Int? {
        fetchSearchResult(searchPhrase, translation, indexOf)
        this.searchPhrase = searchPhrase
        val ceilingIndex = searchResultSet.ceiling(currentPoemItemIndex)
        val floorIndex = searchResultSet.floor(currentPoemItemIndex)
        return when {
            searchResultSet.isEmpty() -> null
            ceilingIndex == null -> floorIndex
            floorIndex == null -> ceilingIndex
            abs(ceilingIndex - currentPoemItemIndex) < abs(floorIndex - currentPoemItemIndex) -> ceilingIndex
            else -> floorIndex
        }
    }

    fun nextResult(currentPoemItemIndex: Int): Int? = searchResultSet.higher(currentPoemItemIndex)
    fun previousResult(currentPoemItemIndex: Int): Int? =
        searchResultSet.lower(currentPoemItemIndex)

    fun checkSearchState(currentPoemItemIndex: Int?) = PoemListViewModel.SearchState(
        hasResult = searchResultSet.isNotEmpty(),
        hasNext = searchResultSet.higher(currentPoemItemIndex) != null,
        hasPrevious = searchResultSet.lower(currentPoemItemIndex) != null,
        searchPhrase = searchPhrase,
    )

    private suspend fun fetchSearchResult(
        searchPhrase: String,
        translation: TranslationItem,
        indexOf: (poemItem: PoemItem) -> Int,
    ) {
        searchResultSet.clear()
        if (searchPhrase.isNotBlank()) {
            val params =
                FindPoemsParams(
                    searchPhrase,
                    translationItemMapper.mapToDomain(translation)
                )
            searchResultSet.addAll(
                poemItemMapper.mapToPresentation(findPoems(params)).map { indexOf(it) }
            )
        }
    }
}
package com.vuxur.khayyam.pages.poemList.view.viewModel

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.usecase.getLastVisitedPoem.GetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.getPoems.GetPoems
import com.vuxur.khayyam.domain.usecase.getPoems.GetPoemsParams
import com.vuxur.khayyam.domain.usecase.getSelectedPoemLocale.GetSelectedPoemLocale
import com.vuxur.khayyam.domain.usecase.setLastVisitedPoem.SetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.setLastVisitedPoem.SetLastVisitedPoemParams
import com.vuxur.khayyam.mapper.LocaleItemMapper
import com.vuxur.khayyam.mapper.PoemItemMapper
import com.vuxur.khayyam.model.LocaleItem
import com.vuxur.khayyam.model.PoemItem
import com.vuxur.khayyam.utils.getCurrentLocale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class PoemListViewModel @Inject constructor(
    private val getPoems: GetPoems,
    private val poemItemMapper: PoemItemMapper,
    private val getSelectedPoemLocale: GetSelectedPoemLocale,
    private val localeItemMapper: LocaleItemMapper,
    private val searchManager: SearchManager,
    private val setLastVisitedPoem: SetLastVisitedPoem,
    private val getLastVisitedPoem: GetLastVisitedPoem,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading())
    val uiState: StateFlow<UiState> = _uiState
    private val poemList get() = (_uiState.value as UiState.Loaded).poems

    fun viewIsReady() {
        if (uiState.value is UiState.Loading) {
            onSelectedPoemLocaleChange { selectedLocaleItem ->
                val appropriateLocaleItem =
                    getAppropriateLocaleItemOrNavigateToSetting(selectedLocaleItem)
                if (appropriateLocaleItem is LocaleItem.CustomLocale)
                    setLoadedState(appropriateLocaleItem)
            }
            onLastVisitedPoemChanged { lastVisitedPoemItem ->
                navigateToPoem(lastVisitedPoemItem)
                updateSearchState()
            }
        }
    }

    private fun getAppropriateLocaleItemOrNavigateToSetting(selectedLocaleItem: LocaleItem): LocaleItem {
        return when (selectedLocaleItem) {
            is LocaleItem.CustomLocale -> selectedLocaleItem
            LocaleItem.NoLocale -> {
                consumeEvent(Event.NavigateToLanguageSetting)
                selectedLocaleItem
            }
            LocaleItem.SystemLocale -> LocaleItem.CustomLocale(getCurrentLocale(Resources.getSystem()))
        }
    }

    private fun onSelectedPoemLocaleChange(action: suspend (selectedPoemLocaleItem: LocaleItem) -> Unit) {
        viewModelScope.launch {
            getSelectedPoemLocale().collect { selectedPoemLocale ->
                val selectedLocaleItem = localeItemMapper.mapToPresentation(selectedPoemLocale)
                action(selectedLocaleItem)
            }
        }
    }

    private fun navigateToPoem(poemItem: PoemItem) {
        (_uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            val localIndexOfPoemItem = poemList.indexOf(poemItem)
            if (localIndexOfPoemItem > 0) {
                _uiState.value = uiStateSnapshot.copy(
                    currentItemIndex = localIndexOfPoemItem,
                )
            }
        }
    }

    private fun onLastVisitedPoemChanged(action: (lastVisitedPoem: PoemItem) -> Unit) {
        viewModelScope.launch {
            getLastVisitedPoem()
                .mapNotNull { lastVisitedPoem ->
                    lastVisitedPoem?.let(poemItemMapper::mapToPresentation)
                }.collect { lastVisitedPoemItem ->
                    action(lastVisitedPoemItem)
                }
        }
    }

    fun setCurrentPoemIndex(currentPoemIndex: Int) {
        poemList.getOrNull(currentPoemIndex)?.let { correspondingPoemItem ->
            setCurrentPoemItem(correspondingPoemItem)
        }
    }

    private fun setCurrentPoemItem(poemItem: PoemItem) {
        val setLastVisitedPoemParams = SetLastVisitedPoemParams(
            lastVisitedPoem = poemItemMapper.mapToDomain(
                poemItem
            )
        )
        viewModelScope.launch {
            setLastVisitedPoem(setLastVisitedPoemParams)
        }
    }

    private fun updateSearchState() {
        (_uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            val searchState = searchManager.checkSearchState(uiStateSnapshot.currentItemIndex)
            _uiState.value = uiStateSnapshot.copy(
                searchState = searchState,
            )
        }
    }

    fun navigateToNearestResult(searchPhrase: String) {
        (uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            viewModelScope.launch {
                val nearestResult = searchManager.nearestSearchResultIndex(
                    searchPhrase,
                    uiStateSnapshot.selectedLocaleItem,
                    uiStateSnapshot.currentItemIndex
                ) {
                    poemList.indexOf(it)
                }
                if (nearestResult != null)
                    setCurrentPoemIndex(nearestResult)
                else
                    updateSearchState()
            }
        }
    }

    fun navigateToNextResult() {
        (uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            searchManager.nextResult(uiStateSnapshot.currentItemIndex)?.let {
                setCurrentPoemIndex(it)
            }
        }
    }

    fun navigateToPreviousResult() {
        (uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            searchManager.previousResult(uiStateSnapshot.currentItemIndex)?.let {
                setCurrentPoemIndex(it)
            }

        }
    }

    fun sharePoemText() {
        (uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                assemblePoem(poemList[uiStateSnapshot.currentItemIndex])
            )
            consumeEvent(Event.SharePoemText(shareIntent))
        }
    }

    fun copyPoem(clipboard: ClipboardManager) {
        (uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            val poemText = assemblePoem(poemList[uiStateSnapshot.currentItemIndex])
            clipboard.setText(AnnotatedString(poemText))
            consumeEvent(Event.CopyPoemText(poemText))
        }
    }

    fun randomPoem() {
        setCurrentPoemIndex(Random.nextInt(poemList.size))
    }

    fun sharePoemImage(bitmap: Bitmap, cacheDir: File) {

        val cachePath = File(cacheDir, "images")
        cachePath.mkdirs()

        val stream =
            FileOutputStream("$cachePath/image.png")

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val imagePath = File(cacheDir, "images")
        consumeEvent(Event.SharePoemImage(File(imagePath, "image.png")))
    }

    fun sharePoemImageUri(poemUri: Uri) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.setDataAndType(poemUri, "image/*")
        shareIntent.putExtra(Intent.EXTRA_STREAM, poemUri)
        consumeEvent(Event.SharePoemText(shareIntent))
    }

    fun onEventConsumed(
        eventConsumed: Event,
    ) {
        when (val uiStateSnapshot = _uiState.value) {
            is UiState.Loaded -> _uiState.value = uiStateSnapshot.copy(
                events = uiStateSnapshot.events.filterNot { event ->
                    event == eventConsumed
                },
            )

            is UiState.Loading -> _uiState.value = uiStateSnapshot.copy(
                events = uiStateSnapshot.events.filterNot { event ->
                    event == eventConsumed
                },
            )
        }
    }

    private suspend fun loadPoems(selectedPoemLocaleItem: LocaleItem.CustomLocale): List<PoemItem> {
        val params = GetPoemsParams(
            locale = localeItemMapper.mapToDomain(selectedPoemLocaleItem) as Locale.CustomLocale
        )
        return poemItemMapper.mapToPresentation(getPoems(params))
    }

    private suspend fun setLoadedState(selectedLocaleItem: LocaleItem.CustomLocale) {
        val poems = loadPoems(selectedLocaleItem)

        val lastVisitedPoemItem = getLastVisitedPoem()
            .firstOrNull()
            ?.let { poemItemMapper.mapToPresentation(it) }

        val initialPoemIndex = poems.indexOf(lastVisitedPoemItem).takeIf { it > 0 } ?: 0

        _uiState.value = UiState.Loaded(
            poems = poems,
            currentItemIndex = initialPoemIndex,
            selectedLocaleItem = selectedLocaleItem
        )
    }

    private fun consumeEvent(
        eventToConsume: Event,
    ) {
        when (val uiStateSnapshot = _uiState.value) {
            is UiState.Loaded -> _uiState.value = uiStateSnapshot.copy(
                events = (uiStateSnapshot.events + eventToConsume)
            )

            is UiState.Loading -> _uiState.value = uiStateSnapshot.copy(
                events = (uiStateSnapshot.events + eventToConsume)
            )
        }
    }

    private fun assemblePoem(poemItem: PoemItem): String {
        return String.format(
            "%s\n%s\n%s\n%s",
            poemItem.hemistich1,
            poemItem.hemistich2,
            poemItem.hemistich3,
            poemItem.hemistich4
        )
    }

    data class SearchState(
        val hasResult: Boolean,
        val hasNext: Boolean,
        val hasPrevious: Boolean,
    )

    sealed class UiState{
        data class Loading(
            val events: List<Event> = emptyList(),
        ) : UiState()
        data class Loaded(
            val poems: List<PoemItem>,
            val currentItemIndex: Int = 0,
            val searchState: SearchState = SearchState(
                hasResult = false,
                hasNext = false,
                hasPrevious = false
            ),
            val events: List<Event> = emptyList(),
            val selectedLocaleItem: LocaleItem.CustomLocale,
        ) : UiState()
    }

    sealed class Event {
        data class SharePoemImage(
            val imageToShare: File,
        ) : Event()

        data class SharePoemText(
            val shareIntent: Intent,
        ) : Event()

        data class CopyPoemText(
            val copiedPoem: String,
        ) : Event()

        data object NavigateToLanguageSetting : Event()
    }
}
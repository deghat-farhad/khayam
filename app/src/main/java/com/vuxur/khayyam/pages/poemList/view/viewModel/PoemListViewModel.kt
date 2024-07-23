package com.vuxur.khayyam.pages.poemList.view.viewModel

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vuxur.khayyam.di.UtilityModule
import com.vuxur.khayyam.domain.usecase.getLastVisitedPoem.GetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.getPoems.GetPoems
import com.vuxur.khayyam.domain.usecase.getPoems.GetPoemsParams
import com.vuxur.khayyam.domain.usecase.getSelectedTranslationOption.GetSelectedTranslationOption
import com.vuxur.khayyam.domain.usecase.setLastVisitedPoem.SetLastVisitedPoem
import com.vuxur.khayyam.domain.usecase.setLastVisitedPoem.SetLastVisitedPoemParams
import com.vuxur.khayyam.domain.usecase.useMatchingSystemLanguageTranslation.UseMatchSystemLanguageTranslation
import com.vuxur.khayyam.domain.usecase.useUntranslated.UseUntranslated
import com.vuxur.khayyam.mapper.PoemItemMapper
import com.vuxur.khayyam.mapper.TranslationOptionsItemMapper
import com.vuxur.khayyam.model.PoemItem
import com.vuxur.khayyam.model.TranslationItem
import com.vuxur.khayyam.model.TranslationOptionsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

@HiltViewModel
class PoemListViewModel @Inject constructor(
    private val getPoems: GetPoems,
    private val poemItemMapper: PoemItemMapper,
    private val getSelectedTranslationOption: GetSelectedTranslationOption,
    private val translationOptionsItemMapper: TranslationOptionsItemMapper,
    private val searchManager: SearchManager,
    private val setLastVisitedPoem: SetLastVisitedPoem,
    private val getLastVisitedPoem: GetLastVisitedPoem,
    private val imageFileOutputStreamProvider: ImageFileOutputStreamProvider,
    @Named(UtilityModule.DI_NAME_IMAGE_FILE)
    private val imageFile: File,
    private val shareIntentProvider: ShareIntentProvider,
    private val useMatchSystemLanguageTranslation: UseMatchSystemLanguageTranslation,
    private val useUntranslated: UseUntranslated,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Loading(isTranslationOptionSelected = true))
    val uiState: StateFlow<UiState> = _uiState
    private val poemList get() = (_uiState.value as UiState.Loaded).poems

    fun viewIsReady() {
        if (_uiState.value is UiState.Loading) {
            onSelectedTranslationOptionChange { translationOptionsItem ->
                if (translationOptionsItem is TranslationOptionsItem.None) {
                    setIsTranslationOptionSelected()
                    setToUseMatchingSystemLanguageTranslation()
                } else {
                    setLoadedState(translationOptionsItem)
                }
            }
            onLastVisitedPoemChanged { lastVisitedPoemItem ->
                navigateToPoem(lastVisitedPoemItem)
                updateSearchState()
            }
        }
    }

    private fun setIsTranslationOptionSelected() {
        _uiState.value = when (val uiStateSnapshot = _uiState.value) {
            is UiState.Loaded -> UiState.Loading(isTranslationOptionSelected = false)

            is UiState.Loading -> uiStateSnapshot.copy(isTranslationOptionSelected = false)
        }
    }

    private fun onSelectedTranslationOptionChange(action: suspend (selectedPoemTranslationOptionsItem: TranslationOptionsItem) -> Unit) {
        viewModelScope.launch {
            getSelectedTranslationOption().collect { selectedTranslationOption ->
                val selectedTranslationOptionItem =
                    translationOptionsItemMapper.mapToPresentation(selectedTranslationOption)
                action(selectedTranslationOptionItem)
            }
        }
    }

    private fun navigateToPoem(poemItem: PoemItem) {
        (_uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            val localIndexOfPoemItem = poemList.indexOf(poemItem)
            if (localIndexOfPoemItem >= 0) {
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
                    uiStateSnapshot.translation,
                    uiStateSnapshot.currentItemIndex
                ) {
                    poemList.indexOf(it)
                }
                if (nearestResult != null)
                    setCurrentPoemIndex(nearestResult)
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
            val shareIntent = shareIntentProvider.getShareTextIntent()
                .putExtra(
                    Intent.EXTRA_TEXT,
                    assemblePoem(poemList[uiStateSnapshot.currentItemIndex])
                )
            consumeEvent(Event.Loaded.SharePoemText(shareIntent))
        }
    }

    fun copyPoem(clipboard: ClipboardManager) {
        (uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            val poemText = assemblePoem(poemList[uiStateSnapshot.currentItemIndex])
            clipboard.setText(AnnotatedString(poemText))
            consumeEvent(Event.Loaded.CopyPoemText(poemText))
        }
    }

    fun randomPoem() {
        setCurrentPoemIndex(Random.nextInt(poemList.size))
    }

    fun sharePoemImage(bitmap: Bitmap) {
        imageFileOutputStreamProvider.getOutputStream().use { fileOutputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        }
        consumeEvent(Event.Loaded.SharePoemImage(imageFile))
    }

    fun sharePoemImageUri(poemUri: Uri) {
        val shareIntent = shareIntentProvider.getShareImageIntent()
            .setData(poemUri)
            .putExtra(Intent.EXTRA_STREAM, poemUri)
        consumeEvent(Event.Loaded.SharePoemText(shareIntent))
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

    fun setToUseMatchingSystemLanguageTranslation() {
        viewModelScope.launch {
            useMatchSystemLanguageTranslation()
        }
    }

    fun setToUseUntranslated() {
        viewModelScope.launch {
            useUntranslated()
            translationDecisionMade()
        }
    }

    fun navigateToSetting() {
        consumeEvent(
            when (_uiState.value) {
                is UiState.Loaded -> Event.Loaded.NavigateToLanguageSetting
                is UiState.Loading -> Event.Loading.NavigateToLanguageSetting
            }
        )
    }

    private suspend fun loadPoems(translationOptionsItem: TranslationOptionsItem): Result<List<PoemItem>> {
        val params = GetPoemsParams(
            translationOptions = translationOptionsItemMapper.mapToDomain(translationOptionsItem)
        )
        return getPoems(params).map { poemItemMapper.mapToPresentation(it) }
    }

    private suspend fun setLoadedState(selectedTranslationOptionsItem: TranslationOptionsItem) {
        val isTranslationOptionSelected =
            (_uiState.value as? UiState.Loading)?.isTranslationOptionSelected ?: true
        loadPoems(selectedTranslationOptionsItem)
            .onSuccess { poems ->
                val lastVisitedPoemItem = getLastVisitedPoem()
                    .firstOrNull()
                    ?.let { poemItemMapper.mapToPresentation(it) }

                val initialPoemIndex = poems.indexOf(lastVisitedPoemItem).takeIf { it > 0 } ?: 0

                _uiState.value = UiState.Loaded(
                    poems = poems,
                    currentItemIndex = initialPoemIndex,
                    translation = poems.first().translation,
                    showTranslationDecision = !isTranslationOptionSelected,
                )
            }
            .onFailure {
                //todo: set some error state
            }
    }

    private fun consumeEvent(
        eventToConsume: Event,
    ) {
        when (val uiStateSnapshot = _uiState.value) {
            is UiState.Loaded ->
                (eventToConsume as? Event.Loaded)?.let {
                    _uiState.value = uiStateSnapshot.copy(
                        events = (uiStateSnapshot.events + eventToConsume)
                    )
                }

            is UiState.Loading ->
                (eventToConsume as? Event.Loading)?.let {
                    _uiState.value = uiStateSnapshot.copy(
                        events = (uiStateSnapshot.events + eventToConsume)
                    )
                }
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

    fun translationDecisionMade() {
        (_uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
            _uiState.value = uiStateSnapshot.copy(showTranslationDecision = false)
        }
    }

    data class SearchState(
        val hasResult: Boolean,
        val hasNext: Boolean,
        val hasPrevious: Boolean,
    )

    sealed class UiState {
        data class Loading(
            val events: List<Event.Loading> = emptyList(),
            val isTranslationOptionSelected: Boolean,
        ) : UiState()

        data class Loaded(
            val poems: List<PoemItem>,
            val currentItemIndex: Int = 0,
            val searchState: SearchState = SearchState(
                hasResult = false,
                hasNext = false,
                hasPrevious = false
            ),
            val events: List<Event.Loaded> = emptyList(),
            val translation: TranslationItem,
            val showTranslationDecision: Boolean = false,
        ) : UiState()
    }

    sealed class Event {
        sealed class Loading : Event() {
            data object NavigateToLanguageSetting : Loading()
        }

        sealed class Loaded : Event() {
            data class SharePoemImage(
                val imageToShare: File,
            ) : Loaded()

            data class SharePoemText(
                val shareIntent: Intent,
            ) : Loaded()

            data class CopyPoemText(
                val copiedPoem: String,
            ) : Loaded()

            data object NavigateToLanguageSetting : Loaded()
        }
    }
}
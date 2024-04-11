package com.vuxur.khayyam.pages.poemList

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.domain.usecase.findPoems.FindPoems
import com.vuxur.khayyam.domain.usecase.findPoems.FindPoemsParams
import com.vuxur.khayyam.domain.usecase.getPoems.GetPoems
import com.vuxur.khayyam.domain.usecase.getPoems.GetPoemsParams
import com.vuxur.khayyam.domain.usecase.getSelectedPoemLocale.GetSelectedPoemLocale
import com.vuxur.khayyam.mapper.LocaleItemMapper
import com.vuxur.khayyam.mapper.PoemItemMapper
import com.vuxur.khayyam.model.LocaleItem
import com.vuxur.khayyam.model.PoemItem
import com.vuxur.khayyam.utils.getCurrentLocale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class PoemListViewModel @Inject constructor(
    private val getPoems: GetPoems,
    private val findPoems: FindPoems,
    private val poemItemMapper: PoemItemMapper,
    private val getSelectedPoemLocale: GetSelectedPoemLocale,
    private val localeItemMapper: LocaleItemMapper,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading())
    val uiState: StateFlow<UiState> = _uiState

    private var searchResult: MutableList<PoemItem> = mutableListOf()
    private val currentPoemIndex
        get() = _uiState.value.let { uiStateSnapshot ->
            if (uiStateSnapshot is UiState.Loaded)
                uiStateSnapshot.currentItemIndex
            else
                -1
        }
    private val poemList get() = (_uiState.value as UiState.Loaded).poems
    private val currentPoem get() = poemList[currentPoemIndex]

    fun viewIsReady() {
        viewModelScope.launch {
            getSelectedPoemLocale().collect { selectedPoemLocale ->
                when (selectedPoemLocale) {
                    Locale.NoLocale -> updateUiState(eventToConsume = Event.NavigateToLanguageSetting)

                    is Locale.CustomLocale -> loadPoems(
                        localeItemMapper.mapToPresentation(
                            selectedPoemLocale
                        )
                    )

                    Locale.SystemLocale -> loadPoems(
                        LocaleItem.CustomLocale(
                            getCurrentLocale(
                                Resources.getSystem()
                            )
                        )
                    )
                }
            }
        }
    }

    fun setCurrentPoemIndex(currentPoemIndex: Int) {
        updateUiState(
            currentItemIndex = currentPoemIndex
        )
    }

    private suspend fun loadPoems(selectedPoemLocale: LocaleItem.CustomLocale) {
            val params = GetPoemsParams(
                locale = localeItemMapper.mapToDomain(selectedPoemLocale)
            )
            getPoems(params).onEach { poems ->
                updateUiState(
                    poems = poemItemMapper.mapToPresentation(poems),
                    currentItemIndex = 0,
                    selectedLocaleItem = selectedPoemLocale,
                )
            }.launchIn(viewModelScope)
    }

    private suspend fun findPoem(
        searchPhrase: String,
        selectedPoemLocale: LocaleItem.CustomLocale,
        condition: (index: Int) -> Boolean = { true },
    ): PoemItem? {
        val params = FindPoemsParams(searchPhrase, localeItemMapper.mapToDomain(selectedPoemLocale))
        searchResult = poemItemMapper.mapToPresentation(findPoems(params).first()).toMutableList()

        return searchResult.filter {
            condition(it.index)
        }.minByOrNull {
            kotlin.math.abs(it.index - currentPoemIndex)
        }
    }

    fun findNearestPoem(searchPhrase: String) {
        viewModelScope.launch {
            (uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
                updateUiState(
                    currentItemIndex = findPoem(
                        searchPhrase,
                        uiStateSnapshot.selectedLocaleItem
                    )?.index ?: currentPoemIndex
                )
            }
        }
    }

    fun onNextResult(searchPhrase: String) {
        viewModelScope.launch {
            (uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
                updateUiState(
                    currentItemIndex = findPoem(searchPhrase, uiStateSnapshot.selectedLocaleItem) {
                        it > currentPoemIndex
                    }?.index ?: currentPoemIndex
                )
            }
        }
    }

    fun onPreviousResult(searchPhrase: String) {
        viewModelScope.launch {
            (uiState.value as? UiState.Loaded)?.let { uiStateSnapshot ->
                updateUiState(
                    currentItemIndex = findPoem(searchPhrase, uiStateSnapshot.selectedLocaleItem) {
                        it < currentPoemIndex
                    }?.index ?: currentPoemIndex
                )
            }
        }
    }

    fun sharePoemText() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, assemblePoem(currentPoem))
        updateUiState(eventToConsume = Event.SharePoemText(shareIntent))
    }

    fun copyPoem(clipboard: ClipboardManager) {
        val poemText = assemblePoem(currentPoem)
        clipboard.setText(AnnotatedString(poemText))
        updateUiState(eventToConsume = Event.CopyPoemText(poemText))
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
        updateUiState(
            eventToConsume = Event.SharePoemImage(File(imagePath, "image.png")),
        )
    }

    fun sharePoemImageUri(poemUri: Uri) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.setDataAndType(poemUri, "image/*")
        shareIntent.putExtra(Intent.EXTRA_STREAM, poemUri)
        updateUiState(
            eventToConsume = Event.SharePoemText(shareIntent),
        )
    }

    fun searchClosed() {
        viewIsReady()
    }

    fun onEventConsumed(event: Event) {
        updateUiState(eventConsumed = event)
    }

    private fun updateUiState(
        poems: List<PoemItem>? = null,
        currentItemIndex: Int = currentPoemIndex,
        eventToConsume: Event? = null,
        eventConsumed: Event? = null,
        selectedLocaleItem: LocaleItem.CustomLocale? = null,
    ) {
        when (val state = _uiState.value) {
            is UiState.Loaded -> {
                _uiState.value = state.copy(
                    poems = poems ?: state.poems,
                    isThereAnyResult = searchResult.isNotEmpty(),
                    isThereNextResult = searchResult.isNotEmpty() && currentItemIndex < searchResult.last().index,
                    isTherePreviousResult = searchResult.isNotEmpty() && currentItemIndex > searchResult.first().index,
                    currentItemIndex = currentItemIndex,
                    events = (state.events.filterNot { it == eventConsumed } + eventToConsume).filterNotNull(),
                    selectedLocaleItem = selectedLocaleItem ?: state.selectedLocaleItem
                )
            }

            is UiState.Loading -> {
                if (selectedLocaleItem != null && poems != null && currentItemIndex != -1) {
                    _uiState.value = UiState.Loaded(
                        poems = poems,
                        currentItemIndex = currentItemIndex,
                        selectedLocaleItem = selectedLocaleItem,
                    )
                } else {
                    _uiState.value = UiState.Loading(
                        events = (state.events.filterNot { it == eventConsumed } + eventToConsume).filterNotNull(),
                    )
                }
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

    sealed class UiState{

        data class Loading(
            val events: List<Event> = emptyList(),
        ) : UiState()
        data class Loaded(
            val poems: List<PoemItem>,
            val currentItemIndex: Int = 0,
            val isThereAnyResult: Boolean = false,
            val isThereNextResult: Boolean = false,
            val isTherePreviousResult: Boolean = false,
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

    private val PoemItem.index: Int
        get() {
            return poemList.indexOf(this)
        }
}
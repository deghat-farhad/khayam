package com.vuxur.khayyam.poemList

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vuxur.khayyam.domain.usecase.findPoems.FindPoems
import com.vuxur.khayyam.domain.usecase.findPoems.FindPoemsParams
import com.vuxur.khayyam.domain.usecase.getPoems.GetPoems
import com.vuxur.khayyam.mapper.PoemItemMapper
import com.vuxur.khayyam.model.PoemItem
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
    private val poemItemMapper: PoemItemMapper
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private var searchResult: MutableList<PoemItem> = mutableListOf()
    private val currentPoemIndex get() = (_uiState.value as UiState.Loaded).currentItemIndex
    private val poemList get() = (_uiState.value as UiState.Loaded).poems
    private val currentPoem get() = poemList[currentPoemIndex]

    fun viewIsReady() {
        loadPoems()
    }

    fun setCurrentPoemIndex(currentPoemIndex: Int) {
        updateUiState(
            currentItemIndex = currentPoemIndex
        )
    }

    private fun loadPoems() {
        getPoems().onEach { poems ->
            updateUiState(
                poems = poemItemMapper.mapToPresentation(poems),
                currentItemIndex = 0
            )
        }.launchIn(viewModelScope)
    }

    private suspend fun findPoem(
        searchPhrase: String,
        condition: (index: Int) -> Boolean = { true }
    ): PoemItem? {
        val params = FindPoemsParams(searchPhrase)
        searchResult = poemItemMapper.mapToPresentation(findPoems(params).first()).toMutableList()

        return searchResult.filter {
            condition(it.index)
        }.minByOrNull {
            kotlin.math.abs(it.index - currentPoemIndex)
        }
    }

    fun findNearestPoem(searchPhrase: String) {
        viewModelScope.launch {
            updateUiState(
                currentItemIndex = findPoem(searchPhrase)?.index ?: currentPoemIndex
            )
        }
    }

    fun onNextResult(searchPhrase: String) {
        viewModelScope.launch {
            updateUiState(
                currentItemIndex = findPoem(searchPhrase) {
                    it > currentPoemIndex
                }?.index ?: currentPoemIndex
            )
        }
    }

    fun onPreviousResult(searchPhrase: String) {
        viewModelScope.launch {
            updateUiState(
                currentItemIndex = findPoem(searchPhrase) {
                    it < currentPoemIndex
                }?.index ?: currentPoemIndex
            )
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
        loadPoems()
    }

    fun onEventConsumed(event: Event) {
        updateUiState(eventConsumed = event)
    }

    private fun updateUiState(
        poems: List<PoemItem>? = null,
        currentItemIndex: Int = currentPoemIndex,
        eventToConsume: Event? = null,
        eventConsumed: Event? = null,
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
                )
            }
            UiState.Loading -> {
                if (poems == null) {
                    throw IllegalArgumentException("poems and currentItemIndex cannot be null when UiState is Loading")
                }
                _uiState.value = UiState.Loaded(
                    poems = poems,
                    currentItemIndex = currentItemIndex,
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

    sealed class UiState{
        data object Loading : UiState()
        data class Loaded(
            val poems: List<PoemItem>,
            val currentItemIndex: Int = 0,
            val isThereAnyResult: Boolean = false,
            val isThereNextResult: Boolean = false,
            val isTherePreviousResult: Boolean = false,
            val events: List<Event> = emptyList(),
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
    }

    private val PoemItem.index: Int
        get() {
            return poemList.indexOf(this)
        }
}
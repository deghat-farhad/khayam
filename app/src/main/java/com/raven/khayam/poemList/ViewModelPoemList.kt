package com.raven.khayam.poemList

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raven.khayam.domain.usecase.findPoems.FindPoems
import com.raven.khayam.domain.usecase.findPoems.FindPoemsParams
import com.raven.khayam.domain.usecase.getPoems.GetPoems
import com.raven.khayam.mapper.PoemItemMapper
import com.raven.khayam.model.PoemItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.random.Random


class ViewModelPoemList @Inject constructor(
    private val getPoems: GetPoems,
    private val findPoems: FindPoems,
    private val poemItemMapper: PoemItemMapper
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState


    private val poemList: MutableList<PoemItem> = mutableListOf()

    fun viewIsReady() {
        loadPoems()
    }

    fun sharePoemImage(bitmap: Bitmap, cacheDir: File, currentItemIndex: Int) {

        val cachePath = File(cacheDir, "images")
        cachePath.mkdirs()

        val stream =
            FileOutputStream("$cachePath/image.jpg")

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.close()

        val imagePath = File(cacheDir, "images")
        updateUiState(imageToShare = File(imagePath, "image.jpg"), currentItemIndex = currentItemIndex)
    }

    fun sharePoemImageUri(poemUri: Uri, currentItemIndex: Int) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.setDataAndType(poemUri, "image/*")
        shareIntent.putExtra(Intent.EXTRA_STREAM, poemUri)
        updateUiState(shareIntent = shareIntent, currentItemIndex = currentItemIndex)
    }

    private fun emitPoems(poems: List<PoemItem>) {
        poemList.clear()
        poemList.addAll(poems)
        updateUiState(poems = poemList, currentItemIndex = 0)
    }

    private fun loadPoems() {
        getPoems().onEach {poems ->
            emitPoems(poemItemMapper.mapToPresentation(poems))
        }.launchIn(viewModelScope)
    }

    fun findPoem(searchPhrase: String) {
        val params = FindPoemsParams(searchPhrase)
        findPoems(params).onEach {poems ->
            emitPoems(poemItemMapper.mapToPresentation(poems))
        }.launchIn(viewModelScope)
    }

    fun sharePoemText(currentItem: Int) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, assemblePoem(poemList[currentItem]))
        updateUiState(shareIntent = shareIntent, currentItemIndex = currentItem)
    }

    fun copyPoem(currentItem: Int, clipboard: ClipboardManager) {
        val poemText = assemblePoem(poemList[currentItem])
        val clip = ClipData.newPlainText("poem", poemText)
        clipboard.setPrimaryClip(clip)
        updateUiState(copiedPoem = poemText, currentItemIndex = currentItem)
    }

    fun randomPoem() {
        updateUiState(currentItemIndex = Random.nextInt(poemList.size))
    }
    fun searchClosed(){
        loadPoems()
    }

    private fun updateUiState(
        poems: List<PoemItem>? = null,
        imageToShare: File? = null,
        shareIntent: Intent? = null,
        copiedPoem: String? = null,
        currentItemIndex: Int
    ) {
        when (val state = _uiState.value) {
            is UiState.Loaded -> {
                _uiState.value = state.copy(
                    poems = poems ?: state.poems,
                    imageToShare = imageToShare,
                    shareIntent = shareIntent,
                    copiedPoem = copiedPoem,
                    currentItemIndex = currentItemIndex
                )
            }
            UiState.Loading -> {
                if (poems == null) {
                    throw IllegalArgumentException("poems and currentItemIndex cannot be null when UiState is Loading")
                }
                _uiState.value = UiState.Loaded(
                    poems = poems,
                    imageToShare = imageToShare,
                    shareIntent = shareIntent,
                    copiedPoem = copiedPoem,
                    currentItemIndex = currentItemIndex
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
        object Loading: UiState()
        data class Loaded(
            val poems: List<PoemItem>,
            val imageToShare: File?,
            val shareIntent: Intent?,
            val copiedPoem: String?,
            val currentItemIndex: Int = 0
            ): UiState()
    }
}
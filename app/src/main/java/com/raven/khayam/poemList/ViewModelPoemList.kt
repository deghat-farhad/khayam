package com.raven.khayam.poemList

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farhad.editableprofile.utils.SingleLiveEvent
import com.raven.khayam.domain.model.Poem
import com.raven.khayam.domain.usecase.base.DefaultObserver
import com.raven.khayam.domain.usecase.getPoems.GetPoems
import com.raven.khayam.mapper.PoemItemMapper
import com.raven.khayam.model.PoemItem
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.random.Random


class ViewModelPoemList @Inject constructor(
    private val getPoems: GetPoems,
    private val poemItemMapper: PoemItemMapper
) : ViewModel() {

    val poemList = arrayListOf<PoemItem>()

    val showPoems: SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>() }
    val poemImageFile: MutableLiveData<File> by lazy { MutableLiveData<File>() }
    val shareIntentLive: MutableLiveData<Intent> by lazy { MutableLiveData<Intent>() }
    val copied: SingleLiveEvent<Unit> by lazy { SingleLiveEvent<Unit>() }
    val randomPoemIndex: SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>() }

    fun viewIsReady() {
        loadPoems()
    }

    fun sharePoemImage(bitmap: Bitmap, cacheDir: File) {

        val cachePath = File(cacheDir, "images")
        cachePath.mkdirs()

        val stream =
            FileOutputStream("$cachePath/image.jpg")

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.close()

        val imagePath = File(cacheDir, "images")
        poemImageFile.value = File(imagePath, "image.jpg")
    }

    fun sharePoemImageUri(poemUri: Uri) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.setDataAndType(poemUri, "image/*")
        shareIntent.putExtra(Intent.EXTRA_STREAM, poemUri)
        shareIntentLive.value = shareIntent
    }

    private fun loadPoems() {
        val observer = object : DefaultObserver<List<Poem>>() {
            override fun onNext(t: List<Poem>) {
                super.onNext(t)
                poemList.addAll(poemItemMapper.mapToPresentation(t))
                showPoems.value = Random.nextInt(t.size)
            }
        }
        getPoems.execute(observer)
    }

    fun sharePoemText(currentItem: Int) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, assemblePoem(poemList[currentItem]))
        shareIntentLive.value = shareIntent
    }

    fun copyPoem(currentItem: Int, clipboard: ClipboardManager) {
        val clip = ClipData.newPlainText("poem", assemblePoem(poemList[currentItem]))
        clipboard.setPrimaryClip(clip)
        copied.call()
    }

    fun randomPoem() {
        randomPoemIndex.value = Random.nextInt(poemList.size)
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
}
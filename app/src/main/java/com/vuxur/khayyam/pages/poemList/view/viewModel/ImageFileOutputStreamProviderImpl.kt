package com.vuxur.khayyam.pages.poemList.view.viewModel

import java.io.File
import java.io.FileOutputStream

class ImageFileOutputStreamProviderImpl(
    private val imageCacheDirectory: File,
    private val imageFileName: String
) : ImageFileOutputStreamProvider {
    override fun getOutputStream(): FileOutputStream {
        return FileOutputStream("$imageCacheDirectory/$imageFileName")
    }
}
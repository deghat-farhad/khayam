package com.vuxur.khayyam.pages.poemList.view.viewModel

import java.io.FileOutputStream

interface ImageFileOutputStreamProvider {
    fun getOutputStream(): FileOutputStream
}
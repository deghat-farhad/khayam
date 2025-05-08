package com.vuxur.khayyam.ui.pages.poemList.view.viewModel

import java.io.FileOutputStream

interface ImageFileOutputStreamProvider {
    fun getOutputStream(): FileOutputStream
}
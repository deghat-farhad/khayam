package com.vuxur.khayyam.pages.poemList.view.viewModel

import java.io.File
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class ImageFileOutputStreamProviderImplTest() {
    private lateinit var imageFileOutputStreamProviderImpl: ImageFileOutputStreamProviderImpl
    private val imageFileName: String = "dummy"

    @TempDir
    lateinit var tempCacheDirectory: File

    @BeforeEach
    fun setup() {
        imageFileOutputStreamProviderImpl = ImageFileOutputStreamProviderImpl(
            tempCacheDirectory,
            imageFileName,
        )
    }

    @Test
    fun `getOutputStream normal case`() {
        val expectedFile = File(tempCacheDirectory, imageFileName)

        val outputStream = imageFileOutputStreamProviderImpl.getOutputStream()
        outputStream.close()

        assertTrue(expectedFile.exists())
        assertTrue(expectedFile.isFile)
    }
}
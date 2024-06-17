package com.vuxur.khayyam.di

import android.content.Context
import com.vuxur.khayyam.pages.poemList.view.viewModel.ImageFileOutputStreamProvider
import com.vuxur.khayyam.pages.poemList.view.viewModel.ImageFileOutputStreamProviderImpl
import com.vuxur.khayyam.pages.poemList.view.viewModel.ShareIntentProvider
import com.vuxur.khayyam.pages.poemList.view.viewModel.ShareIntentProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class UtilityModule {

    companion object {
        const val DI_NAME_IMAGE_CACHE_DIRECTORY = "DINameImageCacheDirectory"
        const val DI_NAME_IMAGE_FILE = "DINameImageFile"

        const val IMAGES_DIR = "images"
        const val IMAGE_FILE_NAME = "image.png"
    }

    @Named(DI_NAME_IMAGE_CACHE_DIRECTORY)
    @Provides
    fun provideImageCacheDirectory(
        @ApplicationContext context: Context,
    ): File {
        val cacheDir = context.cacheDir
        val imageCacheDir = File(cacheDir, IMAGES_DIR)
        imageCacheDir.mkdirs()
        return imageCacheDir
    }

    @Provides
    fun provideImageFileOutputStreamProvider(
        @Named(DI_NAME_IMAGE_CACHE_DIRECTORY)
        imageCacheDirectory: File,
    ): ImageFileOutputStreamProvider {
        return ImageFileOutputStreamProviderImpl(
            imageCacheDirectory,
            IMAGE_FILE_NAME
        )
    }

    @Named(DI_NAME_IMAGE_FILE)
    @Provides
    fun provideImageFile(
        @Named(DI_NAME_IMAGE_CACHE_DIRECTORY)
        imageCacheDirectory: File,
    ): File {
        return File(imageCacheDirectory, IMAGE_FILE_NAME)
    }

    @Provides
    fun provideShareIntentProvider(): ShareIntentProvider {
        return ShareIntentProviderImpl()
    }
}
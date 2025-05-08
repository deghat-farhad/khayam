package com.vuxur.khayyam.pages.poemList.view.viewModel

import android.content.Intent
import com.vuxur.khayyam.ui.pages.poemList.view.viewModel.ShareIntentProviderImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ShareIntentProviderImplTest() {
    private val shareIntentProvider = ShareIntentProviderImpl()
    private val mockkIntent: Intent = mockk()

    @BeforeEach
    fun setup() {
        mockkConstructor(Intent::class)

        every { anyConstructed<Intent>().setAction(any()) } returns mockkIntent
        every { mockkIntent.addFlags(any()) } returns mockkIntent
        every { mockkIntent.setType(any()) } returns mockkIntent
    }

    @Test
    fun `getShareTextIntent normal case`() {
        val shareTextIntent = shareIntentProvider.getShareTextIntent()

        verify { anyConstructed<Intent>().setAction(Intent.ACTION_SEND) }
        verify { mockkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) }
        verify { mockkIntent.setType("text/plain") }
        assertEquals(mockkIntent, shareTextIntent)
    }

    @Test
    fun `getShareImageIntent normal case`() {
        val shareImageIntent = shareIntentProvider.getShareImageIntent()

        verify { anyConstructed<Intent>().setAction(Intent.ACTION_SEND) }
        verify { mockkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) }
        verify { mockkIntent.setType("image/*") }
        assertEquals(mockkIntent, shareImageIntent)
    }
}
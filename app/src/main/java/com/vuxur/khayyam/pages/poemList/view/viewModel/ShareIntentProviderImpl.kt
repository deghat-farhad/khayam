package com.vuxur.khayyam.pages.poemList.view.viewModel

import android.content.Intent

class ShareIntentProviderImpl : ShareIntentProvider {
    override fun getShareTextIntent() = Intent()
        .setAction(Intent.ACTION_SEND)
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        .setType("text/plain")

    override fun getShareImageIntent() = Intent()
        .setAction(Intent.ACTION_SEND)
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        .setType("image/*")

}
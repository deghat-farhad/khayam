package com.vuxur.khayyam.pages.poemList.view.viewModel

import android.content.Intent

interface ShareIntentProvider {
    fun getShareTextIntent(): Intent
    fun getShareImageIntent(): Intent
}
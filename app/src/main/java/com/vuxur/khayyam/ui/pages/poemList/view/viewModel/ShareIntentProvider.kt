package com.vuxur.khayyam.ui.pages.poemList.view.viewModel

import android.content.Intent

interface ShareIntentProvider {
    fun getShareTextIntent(): Intent
    fun getShareImageIntent(): Intent
}
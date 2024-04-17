package com.vuxur.khayyam.utils

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.vuxur.khayyam.R
import com.vuxur.khayyam.model.LocaleItem

fun getPoemsFontFamily(localeItem: LocaleItem.CustomLocale): FontFamily? {
    val isFarsi = localeItem.locale.language == "fa"
    return if (isFarsi) FontFamily(Font(R.font.iran_sans_x_regular)) else null
}

fun getPoemsFontSize(localeItem: LocaleItem.CustomLocale): TextUnit {
    val isFarsi = localeItem.locale.language == "fa"
    return if (isFarsi) 19.sp else 18.sp
}
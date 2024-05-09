package com.vuxur.khayyam.utils

import android.content.res.Resources
import android.os.Build
import android.view.View
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.text.layoutDirection
import java.util.Locale


fun getCurrentLocale(resources: Resources): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales[0]
    } else {
        @Suppress("DEPRECATION")
        resources.configuration.locale
    }
}

fun Locale.toLayoutDirection() = when (layoutDirection) {
    View.LAYOUT_DIRECTION_RTL -> LayoutDirection.Rtl
    View.LAYOUT_DIRECTION_LTR -> LayoutDirection.Ltr
    else -> LayoutDirection.Ltr
}
package com.vuxur.khayyam.utils

import android.content.res.Resources
import android.os.Build
import java.util.Locale


fun getCurrentLocale(resources: Resources): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales[0]
    } else {
        @Suppress("DEPRECATION")
        resources.configuration.locale
    }
}
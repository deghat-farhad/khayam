package com.vuxur.khayyam.model

import java.util.Locale

sealed class LocaleItem {
    data object NoLocale : LocaleItem()
    data object SystemLocale : LocaleItem()
    data class CustomLocale(
        val locale: Locale
    ) : LocaleItem() {
        val isOriginal = locale.toLanguageTag() == "fa-IR"
    }
}
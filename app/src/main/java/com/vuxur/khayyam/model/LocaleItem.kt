package com.vuxur.khayyam.model

import java.util.Locale

sealed class LocaleItem {
    data object NoLocale : LocaleItem()
    data object SystemLocale : LocaleItem()
    data class SelectedLocale(
        val locale: Locale
    ) : LocaleItem()
}
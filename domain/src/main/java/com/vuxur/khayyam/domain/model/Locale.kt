package com.vuxur.khayyam.domain.model

sealed class Locale {

    data object NoLocale : Locale()
    data object SystemLocale : Locale()
    data class SelectedLocale(
        val locale: java.util.Locale
    ) : Locale()
}
package com.vuxur.khayyam.data.entity

import java.util.Locale

sealed class LocaleEntity {
    data object NoLocale : LocaleEntity()
    data object SystemLocale : LocaleEntity()
    data class CustomLocale(
        val locale: Locale
    ) : LocaleEntity()
}

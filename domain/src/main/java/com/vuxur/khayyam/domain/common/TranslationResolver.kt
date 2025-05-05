package com.vuxur.khayyam.domain.common

import com.vuxur.khayyam.domain.model.TranslationOptions
import kotlin.IllegalArgumentException

fun TranslationOptions.resolve() =
    when (this) {
        is TranslationOptions.MatchDeviceLanguage.Available -> translation
        is TranslationOptions.MatchDeviceLanguage.Unavailable -> fallBackTranslation
        is TranslationOptions.Specific -> translation
        is TranslationOptions.Untranslated -> translation
        TranslationOptions.None ->
            throw IllegalArgumentException("\"params.translationOption\" should not be \"TranslationOptions.None\".")
    }

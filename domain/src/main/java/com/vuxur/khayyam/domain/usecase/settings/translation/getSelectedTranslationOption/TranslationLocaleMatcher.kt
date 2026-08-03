package com.vuxur.khayyam.domain.usecase.settings.translation.getSelectedTranslationOption

import com.vuxur.khayyam.domain.model.Translation
import java.util.Locale

internal fun List<Translation>.findBestMatchFor(locale: Locale): Translation? {
    val requestedTag = locale.toLanguageTag()
    val requestedLanguage = locale.language

    if (requestedLanguage.isBlank()) return null

    return firstOrNull { it.languageTag.equals(requestedTag, ignoreCase = true) }
        ?: firstOrNull { it.languageTag.equals(requestedLanguage, ignoreCase = true) }
        ?: firstOrNull {
            Locale.forLanguageTag(it.languageTag).language.equals(
                requestedLanguage,
                ignoreCase = true,
            )
        }
}

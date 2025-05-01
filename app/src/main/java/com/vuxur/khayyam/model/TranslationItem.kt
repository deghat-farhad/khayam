package com.vuxur.khayyam.model

import com.vuxur.khayyam.domain.usecase.settings.translation.getSelectedTranslationOption.UNTRANSLATED_LANGUAGE_TAG
import java.util.Locale

data class TranslationItem(
    val id: Int,
    val languageTag: String,
    val translator: String,
) {
    val locale = Locale.forLanguageTag(languageTag)
    val displayLanguage = locale.displayLanguage
    fun isUntranslated() = languageTag == UNTRANSLATED_LANGUAGE_TAG
}
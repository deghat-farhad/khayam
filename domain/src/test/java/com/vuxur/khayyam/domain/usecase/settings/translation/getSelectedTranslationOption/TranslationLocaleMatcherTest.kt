package com.vuxur.khayyam.domain.usecase.settings.translation.getSelectedTranslationOption

import com.vuxur.khayyam.domain.model.Translation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.Locale

class TranslationLocaleMatcherTest {

    @Test
    fun `returns exact locale match before other translations in the same language`() {
        val translations = listOf(
            translation(id = 1, languageTag = "en-US"),
            translation(id = 2, languageTag = "en-GB"),
        )

        val result = translations.findBestMatchFor(Locale.forLanguageTag("en-GB"))

        assertEquals(2, result?.id)
    }

    @Test
    fun `prefers a base-language translation for a regional device locale`() {
        val translations = listOf(
            translation(id = 1, languageTag = "ar-EG"),
            translation(id = 2, languageTag = "ar"),
        )

        val result = translations.findBestMatchFor(Locale.forLanguageTag("ar-SA"))

        assertEquals(2, result?.id)
    }

    @Test
    fun `uses another regional translation with the same base language`() {
        val translations = listOf(
            translation(id = 1, languageTag = "en-US"),
            translation(id = 2, languageTag = "fa-IR"),
        )

        val result = translations.findBestMatchFor(Locale.forLanguageTag("en-GB"))

        assertEquals(1, result?.id)
    }

    @Test
    fun `returns no match for an unsupported language`() {
        val translations = listOf(
            translation(id = 1, languageTag = "en-US"),
            translation(id = 2, languageTag = "fa-IR"),
        )

        val result = translations.findBestMatchFor(Locale.forLanguageTag("tr-TR"))

        assertNull(result)
    }

    private fun translation(id: Int, languageTag: String) = Translation(
        id = id,
        languageTag = languageTag,
        translator = "Translator",
    )
}

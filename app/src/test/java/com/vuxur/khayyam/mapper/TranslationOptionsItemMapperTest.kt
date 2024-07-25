package com.vuxur.khayyam.mapper

import com.vuxur.khayyam.domain.model.Translation
import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.model.TranslationItem
import com.vuxur.khayyam.model.TranslationOptionsItem
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TranslationOptionsItemMapperTest {

    private val translationItemMapper: TranslationItemMapper = mockk()
    private val translationOptionsItemMapper = TranslationOptionsItemMapper(translationItemMapper)

    private val dummyTranslation = Translation(
        1,
        "deviceLanguageTag",
        "translator"
    )
    private val dummyTranslationItem = TranslationItem(
        1,
        "deviceLanguageTag",
        "translator"
    )

    private val dummyNoneTranslationOption = TranslationOptions.None
    private val dummyUntranslatedOption = TranslationOptions.Untranslated
    private val dummyMatchSystemTranslationOption = TranslationOptions.MatchDeviceLanguage(
        dummyTranslation
    )
    private val dummySpecificTranslationOption = TranslationOptions.Specific(
        dummyTranslation
    )
    private val dummyNoneTranslationOptionItem = TranslationOptionsItem.None
    private val dummyUntranslatedOptionItem = TranslationOptionsItem.Untranslated
    private val dummyMatchSystemTranslationOptionItem = TranslationOptionsItem.MatchDeviceLanguage(
        dummyTranslationItem
    )
    private val dummySpecificTranslationOptionItem = TranslationOptionsItem.Specific(
        dummyTranslationItem
    )

    @BeforeEach
    fun setup() {
        every { translationItemMapper.mapToDomain(dummyTranslationItem) } returns dummyTranslation
        every { translationItemMapper.mapToPresentation(dummyTranslation) } returns dummyTranslationItem
    }

    @Test
    fun `mapToPresentation NoneTranslationOption`() {
        val localeItem = translationOptionsItemMapper.mapToPresentation(dummyNoneTranslationOption)
        assertEquals(dummyNoneTranslationOptionItem, localeItem)
    }

    @Test
    fun `mapToPresentation MatchSystemTranslationOption`() {
        val localeItem =
            translationOptionsItemMapper.mapToPresentation(dummyMatchSystemTranslationOption)
        assertEquals(dummyMatchSystemTranslationOptionItem, localeItem)
    }

    @Test
    fun `mapToPresentation SpecificTranslationOption`() {
        val localeItem =
            translationOptionsItemMapper.mapToPresentation(dummySpecificTranslationOption)
        assertEquals(dummySpecificTranslationOptionItem, localeItem)
    }

    @Test
    fun `mapToDomain NoneTranslationOption`() {
        val locale = translationOptionsItemMapper.mapToDomain(dummyNoneTranslationOptionItem)
        assertEquals(dummyNoneTranslationOption, locale)
    }

    @Test
    fun `mapToDomain MatchSystemTranslationOption`() {
        val locale = translationOptionsItemMapper.mapToDomain(dummyMatchSystemTranslationOptionItem)
        assertEquals(dummyMatchSystemTranslationOption, locale)
    }

    @Test
    fun `mapToDomain SpecificTranslationOption`() {
        val locale = translationOptionsItemMapper.mapToDomain(dummySpecificTranslationOptionItem)
        assertEquals(dummySpecificTranslationOption, locale)
    }
}
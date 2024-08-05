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
    private lateinit var translationOptionsItemMapper: TranslationOptionsItemMapper

    private val dummyTranslation: Translation = mockk()
    private val dummyTranslationItem: TranslationItem = mockk()

    private val dummyNoneTranslationOption = TranslationOptions.None
    private val dummyUntranslatedOption = TranslationOptions.Untranslated(dummyTranslation)
    private val dummyMatchSystemTranslationOptionAvailable =
        TranslationOptions.MatchDeviceLanguage.Available(
            dummyTranslation
        )
    private val dummyMatchSystemTranslationOptionUnavailable =
        TranslationOptions.MatchDeviceLanguage.Unavailable(
            dummyTranslation
        )
    private val dummySpecificTranslationOption = TranslationOptions.Specific(
        dummyTranslation
    )
    private val dummyNoneTranslationOptionItem = TranslationOptionsItem.None
    private val dummyUntranslatedOptionItem =
        TranslationOptionsItem.Untranslated(dummyTranslationItem)
    private val dummyMatchSystemTranslationOptionItemAvailable =
        TranslationOptionsItem.MatchDeviceLanguage.Available(
            dummyTranslationItem
        )
    private val dummyMatchSystemTranslationOptionItemUnavailable =
        TranslationOptionsItem.MatchDeviceLanguage.Unavailable(
            dummyTranslationItem
        )
    private val dummySpecificTranslationOptionItem = TranslationOptionsItem.Specific(
        dummyTranslationItem
    )

    @BeforeEach
    fun setup() {
        translationOptionsItemMapper = TranslationOptionsItemMapper(translationItemMapper)

        every { translationItemMapper.mapToDomain(dummyTranslationItem) } returns dummyTranslation
        every { translationItemMapper.mapToPresentation(dummyTranslation) } returns dummyTranslationItem
    }

    @Test
    fun `mapToPresentation NoneTranslationOption`() {
        val translationOptionItem =
            translationOptionsItemMapper.mapToPresentation(dummyNoneTranslationOption)
        assertEquals(dummyNoneTranslationOptionItem, translationOptionItem)
    }

    @Test
    fun `mapToPresentation MatchSystemTranslationOption Available`() {
        val translationOptionItem =
            translationOptionsItemMapper.mapToPresentation(
                dummyMatchSystemTranslationOptionAvailable
            )
        assertEquals(dummyMatchSystemTranslationOptionItemAvailable, translationOptionItem)
    }

    @Test
    fun `mapToPresentation MatchSystemTranslationOption Unavailable`() {
        val translationOptionItem =
            translationOptionsItemMapper.mapToPresentation(
                dummyMatchSystemTranslationOptionUnavailable
            )
        assertEquals(dummyMatchSystemTranslationOptionItemUnavailable, translationOptionItem)
    }

    @Test
    fun `mapToPresentation SpecificTranslationOption`() {
        val translationOptionItem =
            translationOptionsItemMapper.mapToPresentation(dummySpecificTranslationOption)
        assertEquals(dummySpecificTranslationOptionItem, translationOptionItem)
    }

    @Test
    fun `mapToPresentation Untranslated`() {
        val translationOptionItem =
            translationOptionsItemMapper.mapToPresentation(dummyUntranslatedOption)
        assertEquals(dummyUntranslatedOptionItem, translationOptionItem)
    }

    @Test
    fun `mapToDomain NoneTranslationOption`() {
        val translation = translationOptionsItemMapper.mapToDomain(dummyNoneTranslationOptionItem)
        assertEquals(dummyNoneTranslationOption, translation)
    }

    @Test
    fun `mapToDomain MatchSystemTranslationOption Available`() {
        val translation =
            translationOptionsItemMapper.mapToDomain(dummyMatchSystemTranslationOptionItemAvailable)
        assertEquals(dummyMatchSystemTranslationOptionAvailable, translation)
    }

    @Test
    fun `mapToDomain MatchSystemTranslationOption Unavailable`() {
        val translation =
            translationOptionsItemMapper.mapToDomain(
                dummyMatchSystemTranslationOptionItemUnavailable
            )
        assertEquals(dummyMatchSystemTranslationOptionUnavailable, translation)
    }

    @Test
    fun `mapToDomain SpecificTranslationOption`() {
        val translation =
            translationOptionsItemMapper.mapToDomain(dummySpecificTranslationOptionItem)
        assertEquals(dummySpecificTranslationOption, translation)
    }

    @Test
    fun `mapToDomain Untranslated`() {
        val translation = translationOptionsItemMapper.mapToDomain(dummyUntranslatedOptionItem)
        assertEquals(dummyUntranslatedOption, translation)
    }
}
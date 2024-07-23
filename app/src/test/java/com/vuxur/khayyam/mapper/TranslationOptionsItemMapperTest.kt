package com.vuxur.khayyam.mapper

import com.vuxur.khayyam.domain.model.TranslationOptions
import com.vuxur.khayyam.model.TranslationOptionsItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TranslationOptionsItemMapperTest() {

    private val translationOptionsItemMapper = TranslationOptionsItemMapper()

    private val dummyNoLocale = TranslationOptions.None
    private val dummySystemLocale = TranslationOptions.SystemLocale
    private val dummyCustomLocale = TranslationOptions.Specific(
        java.util.Locale.ENGLISH
    )
    private val dummyNoTranslationOptionsItem = TranslationOptionsItem.NoTranslationOptions
    private val dummySystemTranslationOptionsItem = TranslationOptionsItem.SystemTranslationOptions
    private val dummyCustomTranslationOptionsItem = TranslationOptionsItem.CustomTranslationOptions(
        dummyCustomLocale.locale
    )
    private val dummyLocaleList = listOf(
        dummyNoLocale,
        dummySystemLocale,
        dummyCustomLocale
    )
    private val dummyLocaleItemList = listOf(
        dummyNoTranslationOptionsItem,
        dummySystemTranslationOptionsItem,
        dummyCustomTranslationOptionsItem,
    )

    @Test
    fun `mapToPresentation NoLocale`() {
        val localeItem = translationOptionsItemMapper.mapToPresentation(dummyNoLocale)
        assertEquals(dummyNoTranslationOptionsItem, localeItem)
    }

    @Test
    fun `mapToPresentation SystemLocale`() {
        val localeItem = translationOptionsItemMapper.mapToPresentation(dummySystemLocale)
        assertEquals(dummySystemTranslationOptionsItem, localeItem)
    }

    @Test
    fun `mapToPresentation CustomLocale`() {
        val localeItem = translationOptionsItemMapper.mapToPresentation(dummyCustomLocale)
        assertEquals(dummyCustomTranslationOptionsItem, localeItem)
    }

    @Test
    fun `mapToDomain NoLocale`() {
        val locale = translationOptionsItemMapper.mapToDomain(dummyNoTranslationOptionsItem)
        assertEquals(dummyNoLocale, locale)
    }

    @Test
    fun `mapToDomain SystemLocale`() {
        val locale = translationOptionsItemMapper.mapToDomain(dummySystemTranslationOptionsItem)
        assertEquals(dummySystemLocale, locale)
    }

    @Test
    fun `mapToDomain CustomLocale`() {
        val locale = translationOptionsItemMapper.mapToDomain(dummyCustomTranslationOptionsItem)
        assertEquals(dummyCustomLocale, locale)
    }

    @Test
    fun `mapToPresentation localeList`() {
        val localeList = translationOptionsItemMapper.mapToPresentation(dummyLocaleList)
        assertEquals(dummyLocaleItemList, localeList)
    }
}
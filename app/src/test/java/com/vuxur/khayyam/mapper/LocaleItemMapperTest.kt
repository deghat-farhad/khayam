package com.vuxur.khayyam.mapper

import com.vuxur.khayyam.domain.model.Locale
import com.vuxur.khayyam.model.LocaleItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LocaleItemMapperTest() {

    private val localeItemMapper = LocaleItemMapper()

    private val dummyNoLocale = Locale.NoLocale
    private val dummySystemLocale = Locale.SystemLocale
    private val dummyCustomLocale = Locale.CustomLocale(
        java.util.Locale.ENGLISH
    )
    private val dummyNoLocaleItem = LocaleItem.NoLocale
    private val dummySystemLocaleItem = LocaleItem.SystemLocale
    private val dummyCustomLocaleItem = LocaleItem.CustomLocale(
        dummyCustomLocale.locale
    )
    private val dummyLocaleList = listOf(
        dummyNoLocale,
        dummySystemLocale,
        dummyCustomLocale
    )
    private val dummyLocaleItemList = listOf(
        dummyNoLocaleItem,
        dummySystemLocaleItem,
        dummyCustomLocaleItem,
    )

    @Test
    fun `mapToPresentation NoLocale`() {
        val localeItem = localeItemMapper.mapToPresentation(dummyNoLocale)
        assertEquals(dummyNoLocaleItem, localeItem)
    }

    @Test
    fun `mapToPresentation SystemLocale`() {
        val localeItem = localeItemMapper.mapToPresentation(dummySystemLocale)
        assertEquals(dummySystemLocaleItem, localeItem)
    }

    @Test
    fun `mapToPresentation CustomLocale`() {
        val localeItem = localeItemMapper.mapToPresentation(dummyCustomLocale)
        assertEquals(dummyCustomLocaleItem, localeItem)
    }

    @Test
    fun `mapToDomain NoLocale`() {
        val locale = localeItemMapper.mapToDomain(dummyNoLocaleItem)
        assertEquals(dummyNoLocale, locale)
    }

    @Test
    fun `mapToDomain SystemLocale`() {
        val locale = localeItemMapper.mapToDomain(dummySystemLocaleItem)
        assertEquals(dummySystemLocale, locale)
    }

    @Test
    fun `mapToDomain CustomLocale`() {
        val locale = localeItemMapper.mapToDomain(dummyCustomLocaleItem)
        assertEquals(dummyCustomLocale, locale)
    }

    @Test
    fun `mapToPresentation localeList`() {
        val localeList = localeItemMapper.mapToPresentation(dummyLocaleList)
        assertEquals(dummyLocaleItemList, localeList)
    }
}
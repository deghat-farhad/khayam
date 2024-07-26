package com.vuxur.khayyam.mapper

import com.vuxur.khayyam.domain.model.Translation
import com.vuxur.khayyam.model.TranslationItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TranslationItemMapperTest {
    private val translationItemMapper = TranslationItemMapper()

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
    private val dummyTranslationList = listOf(
        dummyTranslation.copy(id = 1),
        dummyTranslation.copy(id = 2),
        dummyTranslation.copy(id = 3),
    )
    private val dummyTranslationItemList = listOf(
        dummyTranslationItem.copy(id = 1),
        dummyTranslationItem.copy(id = 2),
        dummyTranslationItem.copy(id = 3),
    )

    @Test
    fun `mapToDomain Test`() {
        val translation = translationItemMapper.mapToDomain(dummyTranslationItem)
        assertEquals(dummyTranslation, translation)
    }

    @Test
    fun `mapToPresentation Test`() {
        val translationItem = translationItemMapper.mapToPresentation(dummyTranslation)
        assertEquals(dummyTranslationItem, translationItem)
    }

    @Test
    fun `mapToPresentation List Test`() {
        val translationItemList = translationItemMapper.mapToPresentation(dummyTranslationList)
        translationItemList.forEachIndexed { index, value ->
            assertEquals(dummyTranslationItemList[index], value)
        }
    }
}
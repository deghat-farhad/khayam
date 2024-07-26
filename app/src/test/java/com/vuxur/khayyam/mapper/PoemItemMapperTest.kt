package com.vuxur.khayyam.mapper

import com.vuxur.khayyam.domain.model.Poem
import com.vuxur.khayyam.domain.model.Translation
import com.vuxur.khayyam.model.PoemItem
import com.vuxur.khayyam.model.TranslationItem
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PoemItemMapperTest() {
    private val translationItemMapper: TranslationItemMapper = mockk()
    private val poemItemMapper = PoemItemMapper(translationItemMapper)

    private val dummyTranslation: Translation = mockk()
    private val dummyTranslationItem: TranslationItem = mockk()

    private val dummyPoem = Poem(
        -1,
        "poem.index",
        "poem.hemistich1",
        "poem.hemistich2",
        "poem.hemistich3",
        "poem.hemistich4",
        true,
        dummyTranslation,
    )
    private val dummyPoemItem = PoemItem(
        -1,
        "poem.index",
        "poem.hemistich1",
        "poem.hemistich2",
        "poem.hemistich3",
        "poem.hemistich4",
        true,
        dummyTranslationItem,
    )
    private val dummyPoemList =
        listOf(dummyPoem.copy(id = 0), dummyPoem.copy(id = 1), dummyPoem.copy(id = 2))
    private val dummyPoemItemList =
        listOf(dummyPoemItem.copy(id = 0), dummyPoemItem.copy(id = 1), dummyPoemItem.copy(id = 2))

    @BeforeEach
    fun setup() {
        every { translationItemMapper.mapToDomain(dummyTranslationItem) } returns dummyTranslation
        every { translationItemMapper.mapToPresentation(dummyTranslation) } returns dummyTranslationItem
    }

    @Test
    fun `mapToPresentation test`() {
        val poemItem = poemItemMapper.mapToPresentation(dummyPoem)
        assertEquals(dummyPoemItem, poemItem)
    }

    @Test
    fun `mapToDomain test`() {
        val poem = poemItemMapper.mapToDomain(dummyPoemItem)
        assertEquals(dummyPoem, poem)
    }

    @Test
    fun `mapToPresentation List`() {
        val poemItemList = poemItemMapper.mapToPresentation(dummyPoemList)
        assertEquals(dummyPoemItemList, poemItemList)
    }
}
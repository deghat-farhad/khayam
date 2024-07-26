package com.vuxur.khayyam.mapper

import com.vuxur.khayyam.domain.model.Translation
import com.vuxur.khayyam.model.TranslationItem
import javax.inject.Inject

class TranslationItemMapper @Inject constructor() {
    fun mapToPresentation(translation: Translation) = TranslationItem(
        translation.id,
        translation.languageTag,
        translation.translator,
    )

    fun mapToPresentation(translationList: List<Translation>) =
        translationList.map { mapToPresentation(it) }

    fun mapToDomain(translationItem: TranslationItem) = Translation(
        translationItem.id,
        translationItem.languageTag,
        translationItem.translator
    )
}
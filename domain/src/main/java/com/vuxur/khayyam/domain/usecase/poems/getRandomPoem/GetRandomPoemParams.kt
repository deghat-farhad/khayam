package com.vuxur.khayyam.domain.usecase.poems.getRandomPoem

import com.vuxur.khayyam.domain.model.TranslationOptions

data class GetRandomPoemParams(
    val translationOptions: TranslationOptions,
)
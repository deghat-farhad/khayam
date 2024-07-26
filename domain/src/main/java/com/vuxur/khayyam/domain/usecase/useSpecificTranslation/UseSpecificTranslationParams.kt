package com.vuxur.khayyam.domain.usecase.useSpecificTranslation

import com.vuxur.khayyam.domain.model.TranslationOptions

data class UseSpecificTranslationParams(
    val translationOption: TranslationOptions.Specific
)

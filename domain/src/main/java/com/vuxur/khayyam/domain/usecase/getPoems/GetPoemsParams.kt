package com.vuxur.khayyam.domain.usecase.getPoems

import com.vuxur.khayyam.domain.model.TranslationOptions

data class GetPoemsParams(
    val translationOptions: TranslationOptions
)
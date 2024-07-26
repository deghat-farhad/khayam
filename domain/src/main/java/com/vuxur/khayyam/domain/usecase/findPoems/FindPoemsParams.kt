package com.vuxur.khayyam.domain.usecase.findPoems

import com.vuxur.khayyam.domain.model.Translation

data class FindPoemsParams(
    val searchPhrase: String,
    val translation: Translation,
)
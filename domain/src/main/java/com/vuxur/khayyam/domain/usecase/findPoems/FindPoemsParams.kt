package com.vuxur.khayyam.domain.usecase.findPoems

import com.vuxur.khayyam.domain.model.Locale

data class FindPoemsParams (
    val searchPhrase: String,
    val locale: Locale.CustomLocale,
)
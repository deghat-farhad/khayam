package com.vuxur.khayyam.domain.usecase.getPoems

import com.vuxur.khayyam.domain.model.Locale

data class GetPoemsParams(
    val locale: Locale.CustomLocale
)
package com.vuxur.khayyam.domain.model

data class Translation(
    val id: Int,
    val languageTag: String,
    val translator: String,
)
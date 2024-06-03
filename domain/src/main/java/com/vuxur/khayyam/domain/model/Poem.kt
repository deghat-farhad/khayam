package com.vuxur.khayyam.domain.model

data class Poem(
    val id: Int,
    val index: String,
    val hemistich1: String,
    val hemistich2: String,
    val hemistich3: String,
    val hemistich4: String,
    val isSuspicious: Boolean,
    val category: Int
)
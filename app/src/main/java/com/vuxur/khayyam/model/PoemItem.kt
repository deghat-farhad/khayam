package com.vuxur.khayyam.model

data class PoemItem(
    val id: Int,
    val hemistich1: String,
    val hemistich2: String,
    val hemistich3: String,
    val hemistich4: String,
    val isSuspicious: Boolean,
    val category: Int
)
package com.raven.khayam.domain.model

data class Poem (
    val id: Int,
    val text: String,
    val isSuspicious: Boolean,
    val category: Int
)
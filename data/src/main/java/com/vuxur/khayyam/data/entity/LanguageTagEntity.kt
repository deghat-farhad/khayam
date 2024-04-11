package com.vuxur.khayyam.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LanguageTagEntity(
    @PrimaryKey val id: Int,
    val languageTag: String
)
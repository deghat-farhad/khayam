package com.vuxur.khayyam.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LanguageEntity(
    @PrimaryKey val id: Int,
    val code: String
)
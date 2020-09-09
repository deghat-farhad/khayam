package com.raven.khayam.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String
)
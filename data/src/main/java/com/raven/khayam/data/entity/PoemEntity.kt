package com.raven.khayam.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["category"],
        onDelete = CASCADE
    )]
)
data class PoemEntity(
    @PrimaryKey val id: Int,
    val text: String,
    val isSuspicious: Boolean,
    val category: Int
)
package com.vuxur.khayyam.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = LanguageTagEntity::class,
        parentColumns = ["id"],
        childColumns = ["language"],
        onDelete = CASCADE
    )]
)
data class PoemEntity(
    @PrimaryKey val id: String,
    val hemistich1: String,
    val hemistich2: String,
    val hemistich3: String,
    val hemistich4: String,
    val isSuspicious: Boolean,
    val language: Int
)
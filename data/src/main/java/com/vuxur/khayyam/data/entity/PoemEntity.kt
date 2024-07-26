package com.vuxur.khayyam.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    foreignKeys = [ForeignKey(
        entity = TranslationEntity::class,
        parentColumns = ["id"],
        childColumns = ["translationId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PoemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val index: String,
    val hemistich1: String,
    val hemistich2: String,
    val hemistich3: String,
    val hemistich4: String,
    val isSuspicious: Boolean,
    val translationId: Int,
)

data class PoemWithTranslationEntity(
    @Embedded val poem: PoemEntity,
    @Relation(
        parentColumn = "translationId",
        entityColumn = "id"
    )
    val translation: TranslationEntity
)
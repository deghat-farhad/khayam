package com.vuxur.khayyam.data.local.database

import androidx.room.Dao
import androidx.room.Query
import com.vuxur.khayyam.data.entity.PoemWithTranslationEntity
import com.vuxur.khayyam.data.entity.TranslationEntity

@Dao
interface PoemDatabaseDao {
    @Query(
        """
            SELECT
                PoemEntity.id,
                PoemEntity.`index`,
                PoemEntity.hemistich1,
                PoemEntity.hemistich2,
                PoemEntity.hemistich3,
                PoemEntity.hemistich4,
                PoemEntity.isSuspicious,
                PoemEntity.translationId 
            FROM
                PoemEntity 
                JOIN
                    TranslationEntity 
                    ON TranslationEntity.id = PoemEntity.translationId 
            WHERE
                TranslationEntity.languageTag = :translationLanguageTag
    """
    )
    suspend fun getPoems(translationLanguageTag: String): List<PoemWithTranslationEntity>

    @Query(
        """
            SELECT
                PoemEntity.id,
                PoemEntity.`index`,
                PoemEntity.hemistich1,
                PoemEntity.hemistich2,
                PoemEntity.hemistich3,
                PoemEntity.hemistich4,
                PoemEntity.isSuspicious,
                PoemEntity.translationId 
            FROM
                PoemEntity 
                JOIN
                    TranslationEntity 
                    ON TranslationEntity.id = PoemEntity.translationId 
            WHERE
                (PoemEntity.hemistich1 LIKE '%' || :searchPhrase || '%' 
                OR PoemEntity.hemistich2 LIKE '%' || :searchPhrase || '%' 
                OR PoemEntity.hemistich3 LIKE '%' || :searchPhrase || '%' 
                OR PoemEntity.hemistich4 LIKE '%' || :searchPhrase || '%' 
                OR PoemEntity.`index` = :searchPhrase)
                AND TranslationEntity.languageTag = :translationLanguageTag
        """
    )
    suspend fun findPoems(searchPhrase: String, translationLanguageTag: String): List<PoemWithTranslationEntity>

    @Query("SELECT * FROM TranslationEntity")
    suspend fun getAvailableTranslations(): List<TranslationEntity>

    @Query(
        """
        SELECT 
            * 
        FROM 
            TranslationEntity
        WHERE
            languageTag = :translationLanguageTag
        """
    )
    suspend fun getTranslationsWithLanguageTag(translationLanguageTag: String): List<TranslationEntity>

    @Query(
        """
            SELECT
                PoemEntity.id,
                PoemEntity.`index`,
                PoemEntity.hemistich1,
                PoemEntity.hemistich2,
                PoemEntity.hemistich3,
                PoemEntity.hemistich4,
                PoemEntity.isSuspicious,
                PoemEntity.translationId 
            FROM
                PoemEntity
            WHERE
                PoemEntity.id = :id
    """
    )
    suspend fun getPoemById(id: Int): PoemWithTranslationEntity

    @Query(
        """
        SELECT 
            * 
        FROM 
            TranslationEntity
        WHERE
            id = :translationId
        """
    )
    suspend fun getTranslationsWithId(translationId: Int): TranslationEntity
}

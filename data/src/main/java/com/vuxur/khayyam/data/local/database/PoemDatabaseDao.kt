package com.vuxur.khayyam.data.local.database

import androidx.room.Dao
import androidx.room.Query
import com.vuxur.khayyam.data.entity.LanguageTagEntity
import com.vuxur.khayyam.data.entity.PoemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PoemDatabaseDao {
    @Query(
        """
            SELECT
                PoemEntity.id,
                PoemEntity.hemistich1,
                PoemEntity.hemistich2,
                PoemEntity.hemistich3,
                PoemEntity.hemistich4,
                PoemEntity.isSuspicious,
                PoemEntity.language 
            FROM
                PoemEntity 
                JOIN
                    LanguageTagEntity 
                    ON LanguageTagEntity.id = PoemEntity.language 
            WHERE
                LanguageTagEntity.languageTag = :languageCode
    """
    )
    fun getPoems(languageCode: String): Flow<List<PoemEntity>>

    @Query(
        """
            SELECT
                PoemEntity.id,
                PoemEntity.hemistich1,
                PoemEntity.hemistich2,
                PoemEntity.hemistich3,
                PoemEntity.hemistich4,
                PoemEntity.isSuspicious,
                PoemEntity.language 
            FROM
                PoemEntity 
                JOIN
                    LanguageTagEntity 
                    ON LanguageTagEntity.id = PoemEntity.language 
            WHERE
                (PoemEntity.hemistich1 LIKE '%' || :searchPhrase || '%' 
                OR PoemEntity.hemistich2 LIKE '%' || :searchPhrase || '%' 
                OR PoemEntity.hemistich3 LIKE '%' || :searchPhrase || '%' 
                OR PoemEntity.hemistich4 LIKE '%' || :searchPhrase || '%' 
                OR PoemEntity.id = :searchPhrase)
                AND LanguageTagEntity.languageTag = :languageCode
        """
    )
    fun findPoems(searchPhrase: String, languageCode: String): Flow<List<PoemEntity>>

    @Query("SELECT * FROM LanguageTagEntity")
    suspend fun getLocales(): List<LanguageTagEntity>
}
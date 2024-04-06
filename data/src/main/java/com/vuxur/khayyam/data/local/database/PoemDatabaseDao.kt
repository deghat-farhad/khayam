package com.vuxur.khayyam.data.local.database

import androidx.room.Dao
import androidx.room.Query
import com.vuxur.khayyam.data.entity.LanguageTagEntity
import com.vuxur.khayyam.data.entity.PoemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PoemDatabaseDao {
    @Query("SELECT PoemEntity.id, PoemEntity.hemistich1, PoemEntity.hemistich2, PoemEntity.hemistich3, PoemEntity.hemistich4, PoemEntity.isSuspicious, PoemEntity.language FROM PoemEntity JOIN LanguageTagEntity ON LanguageTagEntity.id = PoemEntity.language WHERE LanguageTagEntity.languageTag = :languageCode")
    fun getPoems(languageCode: String = "en-US"): Flow<List<PoemEntity>>

    @Query("SELECT * FROM PoemEntity WHERE hemistich1 LIKE '%' || :searchPhrase || '%' OR hemistich2 LIKE '%' || :searchPhrase || '%' OR hemistich3 LIKE '%' || :searchPhrase || '%' OR hemistich4 LIKE '%' || :searchPhrase || '%' OR id = :searchPhrase ORDER BY id")
    fun findPoems(searchPhrase: String): Flow<List<PoemEntity>>

    @Query("SELECT * FROM LanguageTagEntity")
    suspend fun getLocales(): List<LanguageTagEntity>
}
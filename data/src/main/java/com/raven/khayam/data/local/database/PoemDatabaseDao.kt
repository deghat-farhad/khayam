package com.raven.khayam.data.local.database

import androidx.room.Dao
import androidx.room.Query
import com.raven.khayam.data.entity.PoemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PoemDatabaseDao {
    @Query("SELECT * FROM PoemEntity ORDER BY id")
    fun getPoems(): Flow<List<PoemEntity>>

    @Query("SELECT * FROM PoemEntity WHERE hemistich1 LIKE '%' || :searchPhrase || '%' OR hemistich2 LIKE '%' || :searchPhrase || '%' OR hemistich3 LIKE '%' || :searchPhrase || '%' OR hemistich4 LIKE '%' || :searchPhrase || '%' OR id = :searchPhrase ORDER BY id")
    fun findPoems(searchPhrase: String): Flow<List<PoemEntity>>
}
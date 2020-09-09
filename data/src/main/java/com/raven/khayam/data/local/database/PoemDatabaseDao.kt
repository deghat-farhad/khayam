package com.raven.khayam.data.local.database

import androidx.room.Dao
import androidx.room.Query
import com.raven.khayam.data.entity.PoemEntity
import io.reactivex.Observable

@Dao
interface PoemDatabaseDao {
    @Query("SELECT * FROM PoemEntity ORDER BY id")
    fun getPoems(): Observable<List<PoemEntity>>
}
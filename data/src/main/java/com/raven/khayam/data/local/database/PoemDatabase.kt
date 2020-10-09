package com.raven.khayam.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raven.khayam.data.entity.CategoryEntity
import com.raven.khayam.data.entity.PoemEntity

@Database(entities = [PoemEntity::class, CategoryEntity::class], version = 2, exportSchema = false)
abstract class PoemDatabase : RoomDatabase() {
    abstract val poemDatabaseDao: PoemDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: PoemDatabase? = null
        fun getInstance(context: Context): PoemDatabase {
            synchronized(this) {
                var instance =
                    INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PoemDatabase::class.java,
                        "poem_database"
                    )
                        .createFromAsset("database/poem_database.db")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
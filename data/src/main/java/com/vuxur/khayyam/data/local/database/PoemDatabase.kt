package com.vuxur.khayyam.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vuxur.khayyam.data.entity.LanguageEntity
import com.vuxur.khayyam.data.entity.PoemEntity

@Database(entities = [PoemEntity::class, LanguageEntity::class], version = 3, exportSchema = false)
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
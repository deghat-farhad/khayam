package com.vuxur.khayyam.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vuxur.khayyam.data.entity.PoemEntity
import com.vuxur.khayyam.data.entity.TranslationEntity
import com.vuxur.khayyam.data.utils.getFileHash
import java.io.File

@Database(
    entities = [PoemEntity::class, TranslationEntity::class],
    version = 6,
    exportSchema = false
)
abstract class PoemDatabase : RoomDatabase() {
    abstract val poemDatabaseDao: PoemDatabaseDao

    companion object {
        const val DATABASE_NAME = "poem_database"

        @Volatile
        private var INSTANCE: PoemDatabase? = null
        fun getInstance(context: Context): PoemDatabase {
            synchronized(this) {
                if (!isDatabaseUpdated(context, DATABASE_NAME)) {
                    context.deleteDatabase(DATABASE_NAME)
                }

                var instance =
                    INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PoemDatabase::class.java,
                        DATABASE_NAME
                    )
                        .createFromAsset("database/$DATABASE_NAME.db")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        fun isDatabaseUpdated(context: Context, dbName: String): Boolean {
            val dbFile = File(context.getDatabasePath(dbName).absolutePath)
            if (!dbFile.exists()) return false

            val installedDbHash = dbFile.inputStream().use { getFileHash(it) }
            val assetDbHash = context.assets.open("database/$dbName.db").use { getFileHash(it) }

            return installedDbHash == assetDbHash
        }
    }
}
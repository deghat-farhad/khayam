package com.raven.khayam.data.local

import com.raven.khayam.data.local.database.PoemDatabase
import com.raven.khayam.data.local.database.PoemDatabaseDao

class Local(private val database: PoemDatabaseDao){
    fun getPoems() = database.getPoems()
}
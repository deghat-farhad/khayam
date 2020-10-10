package com.raven.khayam.data.local

import com.raven.khayam.data.local.database.PoemDatabaseDao
import com.raven.khayam.data.utils.PersianDigitsMapper.toEnglishDigits

class Local(private val database: PoemDatabaseDao) {
    fun getPoems() = database.getPoems()
    fun findPoems(searchPhrase: String) = database.findPoems(searchPhrase.toEnglishDigits())
}
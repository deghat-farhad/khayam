package com.vuxur.khayyam.data.local

import com.vuxur.khayyam.data.local.database.PoemDatabaseDao
import com.vuxur.khayyam.data.utils.PersianDigitsMapper.toEnglishDigits

class Local(private val database: PoemDatabaseDao) {
    fun getPoems() = database.getPoems()
    fun findPoems(searchPhrase: String) = database.findPoems(searchPhrase.toEnglishDigits())
}
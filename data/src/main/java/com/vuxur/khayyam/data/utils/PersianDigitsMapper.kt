package com.vuxur.khayyam.data.utils

object PersianDigitsMapper {
     fun String.toEnglishDigits(): String {
        val lettersHashMap = hashMapOf<Char, Char>('۰' to '0','۱' to '1','۲' to '2','۳' to '3','۴' to '4','۵' to '5','۶' to '6','۷' to '7','۸' to '8','۹' to '9')

        val charList: List<Char> =  this.map {
            if(lettersHashMap.containsKey(it))
                lettersHashMap.getValue(it)
            else
                it
        }

        return String(charList.toCharArray())
    }
}
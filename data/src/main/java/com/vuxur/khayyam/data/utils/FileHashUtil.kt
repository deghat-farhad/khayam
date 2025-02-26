package com.vuxur.khayyam.data.utils

import java.io.InputStream
import java.security.MessageDigest

fun getFileHash(inputStream: InputStream): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val buffer = ByteArray(1024)
    var bytesRead: Int

    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
        digest.update(buffer, 0, bytesRead)
    }

    return digest.digest().joinToString("") { "%02x".format(it) }
}
package com.vuxur.khayyam.utils

import android.content.Context
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidPermissionChecker @Inject constructor(@ApplicationContext private val context: Context) :
    PermissionChecker {

    override fun isPermissionGranted(permission: String): Boolean {
        // Check if permission is granted
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }
}
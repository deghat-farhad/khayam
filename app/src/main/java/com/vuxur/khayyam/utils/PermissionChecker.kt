package com.vuxur.khayyam.utils

interface PermissionChecker {
    fun isPermissionGranted(permission: String): Boolean
}
package com.vuxur.khayyam.device.model

data class PoemDeviceModel(
    val id: Int,
    val index: String,
    val hemistich1: String,
    val hemistich2: String,
    val hemistich3: String,
    val hemistich4: String,
    val isSuspicious: Boolean,
)
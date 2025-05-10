package com.vuxur.khayyam.device.mapper

import com.vuxur.khayyam.device.model.PoemDeviceModel
import com.vuxur.khayyam.domain.model.Poem
import javax.inject.Inject

class PoemDevicePoemMapper @Inject constructor() {
    companion object {
        fun mapToDevice(poem: Poem) = PoemDeviceModel(
            id = poem.id,
            index = poem.index,
            hemistich1 = poem.hemistich1,
            hemistich2 = poem.hemistich2,
            hemistich3 = poem.hemistich3,
            hemistich4 = poem.hemistich4,
            isSuspicious = poem.isSuspicious,
        )
    }
}
package com.vuxur.khayyam.domain.usecase.notification.showRandomPoemNotification

import com.vuxur.khayyam.domain.model.Poem

data class ShowPoemNotificationParams(
    val poem: Poem,
)

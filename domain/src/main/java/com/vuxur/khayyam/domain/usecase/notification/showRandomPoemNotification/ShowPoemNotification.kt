package com.vuxur.khayyam.domain.usecase.notification.showRandomPoemNotification

import com.vuxur.khayyam.domain.repository.NotificationRepository
import com.vuxur.khayyam.domain.usecase.base.UseCaseWithParams

class ShowPoemNotification(
    private val notificationRepository: NotificationRepository,
) : UseCaseWithParams<Unit, ShowPoemNotificationParams> {
    override suspend fun invoke(params: ShowPoemNotificationParams) {
        notificationRepository.showRandomPoemNotification(params.poem)
    }
}
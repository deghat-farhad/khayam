package com.vuxur.khayyam.entry.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vuxur.khayyam.domain.usecase.notification.rescheduleNotification.RescheduleNotification
import com.vuxur.khayyam.domain.usecase.notification.showRandomPoemNotification.ShowPoemNotification
import com.vuxur.khayyam.domain.usecase.notification.showRandomPoemNotification.ShowPoemNotificationParams
import com.vuxur.khayyam.domain.usecase.poems.getRandomPoem.GetRandomPoem
import com.vuxur.khayyam.domain.usecase.poems.getRandomPoem.GetRandomPoemParams
import com.vuxur.khayyam.domain.usecase.settings.translation.getSelectedTranslationOption.GetSelectedTranslationOption
import com.vuxur.khayyam.domain.usecase.settings.translation.getSelectedTranslationOption.GetSelectedTranslationOptionParams
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationAlarmBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var showPoemNotification: ShowPoemNotification

    @Inject
    lateinit var getSelectedTranslationOption: GetSelectedTranslationOption

    @Inject
    lateinit var getRandomPoem: GetRandomPoem

    @Inject
    lateinit var rescheduleNotification: RescheduleNotification

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onReceive(context: Context, intent: Intent) {
        scope.launch {
            showRandomPoemNotification()
            scheduleNextNotification()
        }
    }

    private suspend fun showRandomPoemNotification() {
        val getSelectedTranslationOptionParams = GetSelectedTranslationOptionParams(
            Locale.getDefault()
        )
        val translation =
            getSelectedTranslationOption(getSelectedTranslationOptionParams).first()

        val randomPoemParams = GetRandomPoemParams(translation)
        val randomPoem = getRandomPoem(randomPoemParams)
        val showPoemNotificationParams = ShowPoemNotificationParams(
            randomPoem
        )
        showPoemNotification.invoke(showPoemNotificationParams)
    }

    private suspend fun scheduleNextNotification() {
        rescheduleNotification.invoke()
    }
}
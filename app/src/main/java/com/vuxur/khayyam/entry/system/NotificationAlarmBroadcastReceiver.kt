package com.vuxur.khayyam.entry.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
    lateinit var showRandomPoemNotification: ShowPoemNotification

    @Inject
    lateinit var getSelectedTranslationOption: GetSelectedTranslationOption

    @Inject
    lateinit var getRandomPoem: GetRandomPoem

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onReceive(context: Context, intent: Intent) {
        scope.launch {
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
            showRandomPoemNotification.invoke(showPoemNotificationParams)
        }
    }
}
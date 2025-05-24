package com.vuxur.khayyam.entry.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vuxur.khayyam.domain.usecase.notification.rescheduleNotification.RescheduleNotification
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BootBroadcastReceiver() : BroadcastReceiver() {

    @Inject
    lateinit var rescheduleNotification: RescheduleNotification

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            scope.launch {
                rescheduleNotification.invoke()
            }
        }
    }
}
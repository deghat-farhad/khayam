package com.vuxur.khayyam.device.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.vuxur.khayyam.device.extentions.toNextTriggerMillis
import com.vuxur.khayyam.device.model.TimeOfDayDeviceModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun scheduleAt(timeOfDayDeviceModel: TimeOfDayDeviceModel, uniqueRequestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent().apply {
            component = ComponentName(
                "com.vuxur.khayyam",
                "com.vuxur.khayyam.entry.system.NotificationAlarmBroadcastReceiver"
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            uniqueRequestCode,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val triggerTime = timeOfDayDeviceModel.toNextTriggerMillis()
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancel(uniqueRequestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent().apply {
            component = ComponentName(
                "com.vuxur.khayyam",
                "com.vuxur.khayyam.entry.system.NotificationAlarmBroadcastReceiver"
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            uniqueRequestCode,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
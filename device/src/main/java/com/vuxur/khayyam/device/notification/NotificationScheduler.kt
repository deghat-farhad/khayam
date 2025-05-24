package com.vuxur.khayyam.device.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.vuxur.khayyam.device.extentions.toNextTriggerMillis
import com.vuxur.khayyam.device.model.TimeOfDayDeviceModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

const val EXTRA_REQUEST_CODE = "requestCode"

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
            putExtra(EXTRA_REQUEST_CODE, uniqueRequestCode)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            uniqueRequestCode,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val triggerTime = timeOfDayDeviceModel.toNextTriggerMillis()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
            Log.d(
                "NotificationScheduler",
                "Using setAndAllowWhileIdle (API ${Build.VERSION.SDK_INT})"
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
            Log.d("NotificationScheduler", "Using setExact (API ${Build.VERSION.SDK_INT})")
        }
    }

    fun cancel(uniqueRequestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent().apply {
            component = ComponentName(
                "com.vuxur.khayyam",
                "com.vuxur.khayyam.entry.system.NotificationAlarmBroadcastReceiver"
            )
            putExtra(EXTRA_REQUEST_CODE, uniqueRequestCode)
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
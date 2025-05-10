package com.vuxur.khayyam.device.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.vuxur.khayyam.device.R
import com.vuxur.khayyam.device.model.PoemDeviceModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PoemNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val channelId = "daily_poem_channel"
    private val channelName = "Daily Khayyam Poem"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = context.getString(R.string.shows_a_daily_khayyam_poem)
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun show(poemDeviceModel: PoemDeviceModel) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_poem_notification)
            .setContentTitle(context.getString(R.string.poem_of_the_day))
            .setContentText(assemblePoem(poemDeviceModel))
            .setStyle(NotificationCompat.BigTextStyle().bigText(assemblePoem(poemDeviceModel)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context)
            .notify(poemDeviceModel.id.hashCode(), builder.build())
    }

    private fun assemblePoem(poemDeviceModel: PoemDeviceModel): String {
        return String.format(
            "%s\n%s\n%s\n%s",
            poemDeviceModel.hemistich1,
            poemDeviceModel.hemistich2,
            poemDeviceModel.hemistich3,
            poemDeviceModel.hemistich4
        )
    }
}
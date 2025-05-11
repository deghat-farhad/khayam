package com.vuxur.khayyam.device.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.vuxur.khayyam.device.R
import com.vuxur.khayyam.device.model.PoemDeviceModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PoemNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val channelId = "daily_poem_channel"
    private val channelName = context.getString(R.string.daily_khayyam_poem)

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
        val poemText = assemblePoem(poemDeviceModel)

        val deepLinkUri = "khayyam://poem_list?poem_id=${poemDeviceModel.id}".toUri()
        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            poemDeviceModel.id, // unique request code
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_poem_notification) // should look elegant and distinct
            .setContentTitle(context.getString(R.string.poem_of_the_day))
            .setContentText(poemText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(poemText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            //.setColor(ContextCompat.getColor(context, R.color.notification_accent)) // optional
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        NotificationManagerCompat.from(context)
            .notify(poemDeviceModel.id.hashCode(), builder.build())
    }

    private fun assemblePoem(poemDeviceModel: PoemDeviceModel): String {
        return listOf(
            poemDeviceModel.hemistich1,
            poemDeviceModel.hemistich2,
            poemDeviceModel.hemistich3,
            poemDeviceModel.hemistich4
        ).joinToString(separator = "\n")
    }
}
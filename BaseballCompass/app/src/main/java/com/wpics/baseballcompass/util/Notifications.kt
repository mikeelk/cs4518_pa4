package com.wpics.baseballcompass.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object Notifications{

    private const val CHANNEL_ID = "baseball_compass_refresh"


    fun ensure(context: Context) {
        val mgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mgr.createNotificationChannel(NotificationChannel(CHANNEL_ID, "BaseballCompass", NotificationManager.IMPORTANCE_HIGH))
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun show(context: Context, title: String, text: String) {
        Log.d("BaseballCompassNotification", "Notification Sent")
        val n = NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true).build()
        NotificationManagerCompat.from(context).notify((0..100000).random(), n)
    }
}
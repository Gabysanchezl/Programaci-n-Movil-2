package com.gaby.ubika.ui.components

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
 import androidx.core.app.NotificationManagerCompat.*
import com.gaby.ubika.utils.canPostNotifications

object NotificationHelper {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)

    fun mostrarNotificacion(context: Context, mensaje: String) {
        val channelId = "graduacion_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Recordatorios de Graduación",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Graduación próxima")
            .setContentText(mensaje)
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .build()

        if (canPostNotifications(context)) {
            from(context).notify(100, notification)
        }
    }
}

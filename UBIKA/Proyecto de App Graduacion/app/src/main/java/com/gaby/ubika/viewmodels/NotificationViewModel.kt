package com.gaby.ubika.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
 import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.gaby.ubika.data.repository.UbikaRepository
import com.gaby.ubika.ui.components.NotificationHelper
 import com.gaby.ubika.ui.components.ReminderWorker
 import com.gaby.ubika.utils.canPostNotifications
import java.util.concurrent.TimeUnit

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UbikaRepository(application.applicationContext)

    fun programarRecordatorio(context: Context, fechaGraduacion: Long) {
        if (!canPostNotifications(context)) {
            Log.w("Recordatorio", "Permiso de notificaciones no concedido, no puede programar.")
            return
        }

        val hoy = System.currentTimeMillis()
        val diasRestantes = TimeUnit.MILLISECONDS.toDays(fechaGraduacion - hoy).toInt()

        if (diasRestantes in 0..5) {
            val mensaje = if (diasRestantes == 0) {
                "¡Hoy es el día de la graduación! Asegúrate de tener todo listo."
            } else {
                "Faltan $diasRestantes días para la graduación. ¡Apresúrate con las asignaciones!"
            }
            try {
                NotificationHelper.mostrarNotificacion(context, mensaje)
            } catch (e: SecurityException) {
                Log.e("NotificationViewModel", "No se pudo mostrar notificación: ${e.message}")
            }        }

         val workRequest = if (diasRestantes in 0..5) {
            // Cada 12 horas si faltan <= 5 días
            PeriodicWorkRequestBuilder<ReminderWorker>(12, TimeUnit.HOURS)
                .setInputData(workDataOf("fechaGraduacion" to fechaGraduacion))
                .build()
        } else {
            // Cada día si faltan más de 5 días
            PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
                .setInputData(workDataOf("fechaGraduacion" to fechaGraduacion))
                .build()
        }

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "graduacion_reminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

}

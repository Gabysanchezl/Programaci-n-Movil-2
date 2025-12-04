package com.gaby.ubika.ui.components

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.annotation.RequiresPermission
import com.gaby.ubika.utils.canPostNotifications
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit


class ReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : Worker(appContext, params) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun doWork(): Result {
        val fechaGraduacion = inputData.getLong("fechaGraduacion", -1L)
        if (fechaGraduacion <= 0) return Result.success()

        val hoy = LocalDate.now()
        val fecha = Instant.ofEpochMilli(fechaGraduacion)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val diasRestantes = ChronoUnit.DAYS.between(hoy, fecha).toInt()

        if (diasRestantes in 0..5) {
            val mensaje = if (diasRestantes == 0) {
                "¡Hoy es el día de la graduación! Asegúrate de tener todo listo."
            } else {
                "Faltan $diasRestantes días para la graduación. ¡Apresúrate con las asignaciones!"
            }
            if (canPostNotifications(applicationContext)) {
                try {
                    NotificationHelper.mostrarNotificacion(applicationContext, mensaje)
                } catch (e: SecurityException) {
                    Log.e("ReminderWorker", "No se pudo mostrar notificación: ${e.message}")
                }
            } else {
                Log.w("ReminderWorker", "Permiso de notificaciones denegado")
            }
        }

        return Result.success()
    }
}

package com.example.notifications

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.widget.SoltarAppWidgetProvider
import java.util.concurrent.TimeUnit

/**
 * Worker "vigilante" de WorkManager (B3).
 * Se ejecuta periódicamente con la única misión de comprobar que las alarmas siguen
 * programadas en AlarmManager y reponerlas si el sistema o Doze las ha purgado.
 * NO es el mecanismo principal de disparo, sino la red de seguridad secundaria.
 */
class NotificationWatchdogWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val context = applicationContext
            val isScheduled = NotificationScheduler.isDailyAlarmScheduled(context)

            if (!isScheduled) {
                Log.d(TAG, "Watchdog detectó que la alarma diaria no estaba programada. Reprogramando todo...")
                NotificationScheduler.scheduleAll(context)
            }

            // Refresca la frase y métricas del widget de pantalla de inicio (B8)
            SoltarAppWidgetProvider.notifyWidgetDataChanged(context)

            // Si la protección continua está configurada, asegura que el servicio esté activo
            if (NotificationScheduler.isContinuousProtectionEnabled(context)) {
                NotificationScheduler.startContinuousProtectionService(context)
            }

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error en NotificationWatchdogWorker: ${e.message}", e)
            Result.success() // No reintentar agresivamente para evitar consumo de batería
        }
    }

    companion object {
        private const val TAG = "WatchdogWorker"
        const val UNIQUE_WORK_NAME = "soltar_notification_watchdog"

        fun schedule(context: Context) {
            try {
                val workRequest = PeriodicWorkRequestBuilder<NotificationWatchdogWorker>(
                    15, TimeUnit.MINUTES,
                    5, TimeUnit.MINUTES // flex interval
                ).build()

                WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    UNIQUE_WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error encolando watchdog periodic work: ${e.message}")
            }
        }
    }
}

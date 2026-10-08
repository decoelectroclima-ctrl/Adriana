package com.example.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.data.SoltarDatabase
import com.example.widget.SoltarAppWidgetProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Receptor de alarmas programadas y eventos del sistema (B2, B4).
 * Utiliza goAsync() para garantizar tiempo de ejecución sin riesgo de proceso muerto.
 * Reprograma SIEMPRE como primer paso para blindar la cadena de avisos ante fallos o timeouts,
 * y descarta notificaciones si la alarma se dispara con retraso excesivo (ej. terminal apagado).
 */
class SoltarAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                withTimeoutOrNull(8000L) {
                    val scheduledTime = intent.getLongExtra(NotificationScheduler.EXTRA_SCHEDULED_TIME, 0L)
                    val isStale = scheduledTime > 0L && (System.currentTimeMillis() - scheduledTime > MAX_ALARM_DELAY_MILLIS)

                    when (action) {
                        SoltarNotificationHelper.ACTION_DAILY_REMINDER,
                        NotificationScheduler.ACTION_DAILY_REMINDER -> {
                            // 1. Reprogramar la siguiente alarma ANTES de procesar para blindar la cadena
                            try {
                                val db = SoltarDatabase.getDatabase(context)
                                val settings = db.soltarSettingsDao().getSettingsOnce()
                                if (settings == null || settings.notificationsEnabled) {
                                    NotificationScheduler.scheduleDailyReminder(
                                        context,
                                        settings?.reminderHour ?: 21,
                                        settings?.reminderMinute ?: 0
                                    )
                                }
                            } catch (_: Exception) {
                                NotificationScheduler.scheduleDailyReminder(context, 21, 0)
                            }

                            // 2. Solo si no viene con retraso excesivo, procesar y mostrar notificación
                            if (isStale) {
                                Log.w(TAG, "Alarma diaria descartada por retraso excesivo (${(System.currentTimeMillis() - scheduledTime) / 60000} min). No se muestra notificación obsoleta.")
                            } else {
                                SoltarNotificationHelper.processDailyReminder(context)
                            }
                            SoltarAppWidgetProvider.notifyWidgetDataChanged(context)
                        }

                        SoltarNotificationHelper.ACTION_MANDATORY_JOURNAL,
                        NotificationScheduler.ACTION_MANDATORY_JOURNAL -> {
                            // 1. Reprogramar la alarma de diario obligatorio ANTES de procesar
                            try {
                                val db = SoltarDatabase.getDatabase(context)
                                val settings = db.soltarSettingsDao().getSettingsOnce()
                                NotificationScheduler.scheduleMandatoryJournalReminder(
                                    context,
                                    settings?.mandatoryJournalHour ?: 20,
                                    settings?.mandatoryJournalMinute ?: 0
                                )
                            } catch (_: Exception) {
                                NotificationScheduler.scheduleMandatoryJournalReminder(context, 20, 0)
                            }

                            // 2. Solo si no viene con retraso excesivo, generar y mostrar la notificación
                            if (isStale) {
                                Log.w(TAG, "Alarma de diario obligatorio descartada por retraso excesivo (${(System.currentTimeMillis() - scheduledTime) / 60000} min). No se muestra notificación obsoleta.")
                            } else {
                                SoltarNotificationHelper.sendMandatoryJournalNotification(context)
                            }
                            SoltarAppWidgetProvider.notifyWidgetDataChanged(context)
                        }

                        SoltarNotificationHelper.ACTION_CUSTOM_NOTIFICATION,
                        NotificationScheduler.ACTION_CUSTOM_NOTIFICATION -> {
                            val id = intent.getLongExtra("notification_id", -1L)
                            val title = intent.getStringExtra("notification_title") ?: "Recordatorio de Soberanía"
                            val message = intent.getStringExtra("notification_message") ?: "Mantén tu enfoque y respira hondo."

                            // 1. Reprogramar la notificación personalizada para el día siguiente ANTES de procesar
                            if (id != -1L) {
                                NotificationScheduler.rescheduleCustomNotification(context, id)
                            }

                            // 2. Solo si no viene con retraso excesivo, generar y mostrar la notificación
                            if (isStale) {
                                Log.w(TAG, "Alarma personalizada descartada por retraso excesivo (${(System.currentTimeMillis() - scheduledTime) / 60000} min). No se muestra notificación obsoleta.")
                            } else {
                                SoltarNotificationHelper.sendCustomNotification(context, title, message)
                            }
                        }

                        Intent.ACTION_BOOT_COMPLETED,
                        Intent.ACTION_MY_PACKAGE_REPLACED,
                        Intent.ACTION_TIME_CHANGED,
                        Intent.ACTION_TIMEZONE_CHANGED -> {
                            Log.d(TAG, "Evento del sistema ($action) recibido. Reprogramando todo el sistema de alarmas...")
                            NotificationScheduler.scheduleAll(context)
                            SoltarAppWidgetProvider.notifyWidgetDataChanged(context)
                        }

                        else -> {
                            Log.d(TAG, "Acción desconocida: $action")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción en SoltarAlarmReceiver.onReceive: ${e.message}", e)
            } finally {
                try {
                    pendingResult.finish()
                } catch (_: Exception) {}
            }
        }
    }

    companion object {
        private const val TAG = "SoltarAlarmReceiver"

        /** Margen razonable de tolerancia (2 horas) para descartar alarmas desfasadas por apagado prolongado */
        const val MAX_ALARM_DELAY_MILLIS = 2 * 60 * 60 * 1000L
    }
}

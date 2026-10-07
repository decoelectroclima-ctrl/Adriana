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
 * Ejecuta exclusivamente consultas ligeras a Room y lógica determinista (sin ViewModels,
 * sin llamadas a Gemini ni modelos on-device en este camino).
 */
class SoltarAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                withTimeoutOrNull(8000L) {
                    when (action) {
                        SoltarNotificationHelper.ACTION_DAILY_REMINDER,
                        NotificationScheduler.ACTION_DAILY_REMINDER -> {
                            // Procesa recordatorio diario / hito / fecha de riesgo y re-encadena la alarma
                            SoltarNotificationHelper.processDailyReminder(context)
                            SoltarAppWidgetProvider.notifyWidgetDataChanged(context)
                        }

                        SoltarNotificationHelper.ACTION_MANDATORY_JOURNAL,
                        NotificationScheduler.ACTION_MANDATORY_JOURNAL -> {
                            SoltarNotificationHelper.sendMandatoryJournalNotification(context)
                            // Re-encadena para el día siguiente
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
                            SoltarAppWidgetProvider.notifyWidgetDataChanged(context)
                        }

                        SoltarNotificationHelper.ACTION_CUSTOM_NOTIFICATION,
                        NotificationScheduler.ACTION_CUSTOM_NOTIFICATION -> {
                            val id = intent.getLongExtra("notification_id", -1L)
                            val title = intent.getStringExtra("notification_title") ?: "Recordatorio de Soberanía"
                            val message = intent.getStringExtra("notification_message") ?: "Mantén tu enfoque y respira hondo."
                            SoltarNotificationHelper.sendCustomNotification(context, title, message)
                            if (id != -1L) {
                                SoltarNotificationHelper.rescheduleCustomNotificationNextDay(context, id)
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
    }
}

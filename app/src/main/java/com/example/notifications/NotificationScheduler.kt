package com.example.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.data.SoltarDatabase
import com.example.data.CustomNotificationItem
import com.example.widget.SoltarAppWidgetProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.util.Calendar

/**
 * Programador central único de notificaciones y alarmas para SOLTAR.
 * Garantiza que cada recordatorio sobreviva con la app cerrada, reinicio de terminal,
 * cambios de zona horaria y Doze mode.
 */
object NotificationScheduler {

    private const val TAG = "NotificationScheduler"

    const val ACTION_DAILY_REMINDER = "com.example.soltar.ACTION_DAILY_REMINDER"
    const val ACTION_MANDATORY_JOURNAL = "com.example.soltar.ACTION_MANDATORY_JOURNAL"
    const val ACTION_CUSTOM_NOTIFICATION = "com.example.soltar.ACTION_CUSTOM_NOTIFICATION"
    const val EXTRA_SCHEDULED_TIME = "extra_scheduled_time"

    const val REQUEST_CODE_DAILY_ALARM = 1001
    const val REQUEST_CODE_MANDATORY_JOURNAL = 1005
    const val REQUEST_CODE_RISK_DATE = 1009

    const val PREFS_NOTIFICATIONS = "soltar_notification_prefs"
    const val KEY_CONTINUOUS_PROTECTION_ENABLED = "continuous_protection_enabled"

    private val json = Json { ignoreUnknownKeys = true }

    fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NOTIFICATIONS, Context.MODE_PRIVATE)
    }

    /**
     * Comprueba si el sistema permite programar alarmas exactas (Android 12+ / API 31+).
     */
    fun canScheduleExactAlarms(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            alarmManager?.canScheduleExactAlarms() ?: false
        } else {
            true
        }
    }

    /**
     * Calcula determinísticamente el próximo instante en milisegundos para una hora y minuto.
     * Si la hora ya pasó hoy (o coincide exactamente), programa para mañana.
     * Maneja correctamente saltos de medianoche, cambios de hora (DST) y zonas horarias.
     */
    fun calculateNextTriggerMillis(
        hourOfDay: Int,
        minute: Int,
        nowMillis: Long = System.currentTimeMillis()
    ): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = nowMillis
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= nowMillis) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        return calendar.timeInMillis
    }

    /**
     * Programa una alarma de manera segura con fallo controlado.
     * Intenta alarma exacta (con allow-while-idle o alarm clock si es crítica) y,
     * si no hay permiso o se produce SecurityException, cae a setAndAllowWhileIdle.
     */
    fun setAlarmSafe(
        context: Context,
        triggerMillis: Long,
        pendingIntent: PendingIntent,
        isCriticalSecurity: Boolean = false
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        try {
            if (canScheduleExactAlarms(context)) {
                if (isCriticalSecurity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val showIntent = Intent(context, com.example.MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    val showPending = PendingIntent.getActivity(
                        context,
                        1999,
                        showIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    alarmManager.setAlarmClock(
                        AlarmManager.AlarmClockInfo(triggerMillis, showPending),
                        pendingIntent
                    )
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        triggerMillis,
                        pendingIntent
                    )
                }
            } else {
                // Sin permiso exacto: cae limpiamente a setAndAllowWhileIdle para sobrevivir a Doze
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        triggerMillis,
                        pendingIntent
                    )
                }
            }
        } catch (e: SecurityException) {
            Log.w(TAG, "SecurityException al programar alarma exacta, usando setAndAllowWhileIdle fallback: ${e.message}")
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        triggerMillis,
                        pendingIntent
                    )
                }
            } catch (fallbackEx: Exception) {
                Log.e(TAG, "Error fatal en fallback de alarma: ${fallbackEx.message}")
            }
        }
    }

    /**
     * Programa la alarma diaria de reflexión, hitos o fechas de riesgo.
     */
    fun scheduleDailyReminder(context: Context, hourOfDay: Int = 21, minute: Int = 0) {
        val triggerMillis = calculateNextTriggerMillis(hourOfDay, minute)
        val intent = Intent(context, SoltarAlarmReceiver::class.java).apply {
            action = ACTION_DAILY_REMINDER
            putExtra(EXTRA_SCHEDULED_TIME, triggerMillis)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_DAILY_ALARM,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        setAlarmSafe(context, triggerMillis, pendingIntent, isCriticalSecurity = false)
    }

    /**
     * Cancela la alarma diaria.
     */
    fun cancelDailyReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, SoltarAlarmReceiver::class.java).apply {
            action = ACTION_DAILY_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_DAILY_ALARM,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    /**
     * Verifica si la alarma diaria se encuentra actualmente programada en el sistema.
     */
    fun isDailyAlarmScheduled(context: Context): Boolean {
        val intent = Intent(context, SoltarAlarmReceiver::class.java).apply {
            action = ACTION_DAILY_REMINDER
        }
        val pending = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_DAILY_ALARM,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        return pending != null
    }

    /**
     * Programa el recordatorio de diario obligatorio.
     */
    fun scheduleMandatoryJournalReminder(context: Context, hourOfDay: Int = 20, minute: Int = 0) {
        val triggerMillis = calculateNextTriggerMillis(hourOfDay, minute)
        val intent = Intent(context, SoltarAlarmReceiver::class.java).apply {
            action = ACTION_MANDATORY_JOURNAL
            putExtra(EXTRA_SCHEDULED_TIME, triggerMillis)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_MANDATORY_JOURNAL,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        setAlarmSafe(context, triggerMillis, pendingIntent, isCriticalSecurity = true)
    }

    /**
     * Cancela el recordatorio de diario obligatorio.
     */
    fun cancelMandatoryJournalReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, SoltarAlarmReceiver::class.java).apply {
            action = ACTION_MANDATORY_JOURNAL
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_MANDATORY_JOURNAL,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    /**
     * Programa una notificación personalizada por el usuario.
     */
    fun scheduleCustomNotification(context: Context, item: CustomNotificationItem) {
        if (!item.enabled) return
        val triggerMillis = calculateNextTriggerMillis(item.hour, item.minute)
        val intent = Intent(context, SoltarAlarmReceiver::class.java).apply {
            action = ACTION_CUSTOM_NOTIFICATION
            putExtra("notification_id", item.id)
            putExtra("notification_title", item.title)
            putExtra("notification_message", item.message)
            putExtra(EXTRA_SCHEDULED_TIME, triggerMillis)
        }
        val requestCode = (3000 + (Math.abs(item.id) % 10000)).toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        setAlarmSafe(context, triggerMillis, pendingIntent, isCriticalSecurity = false)
    }

    /**
     * Reprograma una notificación personalizada para el día siguiente leyendo su configuración.
     */
    suspend fun rescheduleCustomNotification(context: Context, id: Long) {
        try {
            val db = SoltarDatabase.getDatabase(context)
            val settings = db.soltarSettingsDao().getSettingsOnce() ?: return
            if (settings.customNotificationsJson.isNotBlank()) {
                val list = json.decodeFromString<List<CustomNotificationItem>>(settings.customNotificationsJson)
                val item = list.find { it.id == id }
                if (item != null && item.enabled) {
                    scheduleCustomNotification(context, item)
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error reprogramando notificación personalizada $id: ${e.message}")
        }
    }

    /**
     * Cancela una notificación personalizada.
     */
    fun cancelCustomNotification(context: Context, id: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, SoltarAlarmReceiver::class.java).apply {
            action = ACTION_CUSTOM_NOTIFICATION
        }
        val requestCode = (3000 + (Math.abs(id) % 10000)).toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    /**
     * Programa o reprograma todas las notificaciones de la aplicación leyendo la configuración.
     * Es el punto central (B1) invocado al abrir la app, tras reinicio (BOOT), actualización (REPLACED),
     * cambio de zona horaria (TIME_SET/TIMEZONE) y por el vigilante WorkManager (B3).
     */
    fun scheduleAll(context: Context) {
        // Asegura que los canales de notificación existan
        SoltarNotificationHelper.createNotificationChannels(context)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = SoltarDatabase.getDatabase(context)
                val settings = db.soltarSettingsDao().getSettingsOnce()

                withContext(Dispatchers.Main) {
                    if (settings != null) {
                        if (settings.notificationsEnabled) {
                            scheduleDailyReminder(context, settings.reminderHour, settings.reminderMinute)
                        } else {
                            cancelDailyReminder(context)
                        }

                        // El diario obligatorio se programa siempre como anclaje de recuperación
                        scheduleMandatoryJournalReminder(
                            context,
                            settings.mandatoryJournalHour,
                            settings.mandatoryJournalMinute
                        )

                        // Notificaciones personalizadas
                        if (settings.customNotificationsJson.isNotBlank()) {
                            try {
                                val list = json.decodeFromString<List<CustomNotificationItem>>(settings.customNotificationsJson)
                                list.forEach { item ->
                                    if (item.enabled) {
                                        scheduleCustomNotification(context, item)
                                    } else {
                                        cancelCustomNotification(context, item.id)
                                    }
                                }
                            } catch (_: Exception) {}
                        }
                    } else {
                        scheduleDailyReminder(context, 21, 0)
                        scheduleMandatoryJournalReminder(context, 20, 0)
                    }

                    // Encola el vigilante periódico de WorkManager (B3)
                    NotificationWatchdogWorker.schedule(context)

                    // Refresca el widget de pantalla de inicio (B8)
                    SoltarAppWidgetProvider.notifyWidgetDataChanged(context)

                    // Si la protección continua está activa, gestiona el servicio
                    if (isContinuousProtectionEnabled(context)) {
                        startContinuousProtectionService(context)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error en scheduleAll: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    scheduleDailyReminder(context, 21, 0)
                    scheduleMandatoryJournalReminder(context, 20, 0)
                    NotificationWatchdogWorker.schedule(context)
                    SoltarAppWidgetProvider.notifyWidgetDataChanged(context)
                }
            }
        }
    }

    /**
     * Estado del modo "Protección continua" (B7).
     */
    fun isContinuousProtectionEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_CONTINUOUS_PROTECTION_ENABLED, false)
    }

    fun setContinuousProtectionEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_CONTINUOUS_PROTECTION_ENABLED, enabled).apply()
        if (enabled) {
            startContinuousProtectionService(context)
        } else {
            stopContinuousProtectionService(context)
        }
    }

    fun startContinuousProtectionService(context: Context) {
        try {
            val intent = Intent(context, SoltarContinuousProtectionService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, intent)
            } else {
                context.startService(intent)
            }
        } catch (e: Exception) {
            Log.w(TAG, "No se pudo iniciar servicio en primer plano (restricción de background Android): ${e.message}")
        }
    }

    fun stopContinuousProtectionService(context: Context) {
        try {
            val intent = Intent(context, SoltarContinuousProtectionService::class.java)
            context.stopService(intent)
        } catch (e: Exception) {
            Log.w(TAG, "Error deteniendo servicio de protección continua: ${e.message}")
        }
    }
}

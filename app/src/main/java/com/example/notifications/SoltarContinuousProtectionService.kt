package com.example.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.widget.SoltarAppWidgetProvider

/**
 * Servicio en primer plano para el modo "Protección Continua" (B7).
 * Ofrece una presencia persistente con notificación discreta de baja prioridad
 * y acceso inmediato al Modo Impulso / SOS en momentos de vulnerabilidad.
 */
class SoltarContinuousProtectionService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createProtectionChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = buildPersistentNotification()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val serviceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                } else {
                    0
                }
                startForeground(NOTIFICATION_ID, notification, serviceType)
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error iniciando startForeground: ${e.message}")
        }

        return START_STICKY
    }

    private fun buildPersistentNotification(): Notification {
        // Intent al pulsar la notificación: Abre la app normalmente
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this,
            9001,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Acción rápida: Abrir directamente el Modo SOS / Gestión de Impulsos
        val sosIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(SoltarAppWidgetProvider.EXTRA_OPEN_ACTION, SoltarAppWidgetProvider.ACTION_URGE_MODE)
        }
        val sosPendingIntent = PendingIntent.getActivity(
            this,
            9002,
            sosIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_PROTECTION_ID)
            .setSmallIcon(R.drawable.ic_stat_soltar)
            .setContentTitle("SOLTAR • Guardia de Soberanía")
            .setContentText("Acompañamiento activo ante impulsos y recordatorios fiables.")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "Tu serenidad está protegida. Toca '🚨 SOS' si sientes un impulso urgente o deseas regularte."
                )
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setContentIntent(openAppPendingIntent)
            .addAction(0, "🚨 Modo Impulso (SOS)", sosPendingIntent)
            .build()
    }

    private fun createProtectionChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_PROTECTION_ID,
                "Protección Continua y Guardia SOS",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Mantiene la presencia de guardia y acceso inmediato al modo de urgencias."
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val TAG = "ProtectionService"
        const val NOTIFICATION_ID = 9005
        const val CHANNEL_PROTECTION_ID = "soltar_continuous_protection_channel"
    }
}

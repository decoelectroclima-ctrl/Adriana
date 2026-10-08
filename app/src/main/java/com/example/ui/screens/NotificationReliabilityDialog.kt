package com.example.ui.screens

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import com.example.notifications.NotificationScheduler
import com.example.notifications.SoltarNotificationHelper
import com.example.ui.theme.*

/**
 * Pantalla de Diagnóstico y Fiabilidad de Notificaciones: "Asegurar mis notificaciones" (B6).
 * Presenta una lista de comprobación en tiempo real con estados (verde / ámbar / rojo)
 * y botones de acción directos para blindar la llegada de notificaciones ante Doze,
 * hibernación de Android y asesinos de tareas de fabricantes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationReliabilityDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val powerManager = remember { context.getSystemService(Context.POWER_SERVICE) as? PowerManager }

    // Estados dinámicos que se refrescan al interactuar
    var refreshTrigger by remember { mutableIntStateOf(0) }

    val notificationsEnabled by produceState(initialValue = false, refreshTrigger) {
        value = NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    val exactAlarmsAllowed by produceState(initialValue = false, refreshTrigger) {
        value = NotificationScheduler.canScheduleExactAlarms(context)
    }

    val ignoringBatteryOptimizations by produceState(initialValue = false, refreshTrigger) {
        value = powerManager?.isIgnoringBatteryOptimizations(context.packageName) ?: false
    }

    var continuousProtectionEnabled by remember {
        mutableStateOf(NotificationScheduler.isContinuousProtectionEnabled(context))
    }

    val manufacturer = remember { Build.MANUFACTURER.lowercase() }
    val manufacturerName = remember {
        when {
            manufacturer.contains("xiaomi") || manufacturer.contains("redmi") || manufacturer.contains("poco") -> "Xiaomi / MIUI / HyperOS"
            manufacturer.contains("samsung") -> "Samsung / One UI"
            manufacturer.contains("huawei") || manufacturer.contains("honor") -> "Huawei / EMUI"
            manufacturer.contains("oppo") || manufacturer.contains("realme") -> "Oppo / Realme / ColorOS"
            manufacturer.contains("vivo") || manufacturer.contains("iqoo") -> "Vivo / FuntouchOS"
            manufacturer.contains("oneplus") -> "OnePlus / OxygenOS"
            else -> Build.MANUFACTURER.replaceFirstChar { it.uppercase() }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("notification_reliability_dialog"),
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(20.dp)),
            color = SoltarBackground,
            border = BorderStroke(1.dp, SoltarBorder)
        ) {
            Scaffold(
                containerColor = SoltarBackground,
                topBar = {
                    Surface(
                        color = SoltarSurface,
                        border = BorderStroke(1.dp, SoltarBorderSubtle)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = SoltarAmber,
                                    modifier = Modifier.size(24.dp)
                                )
                                Column {
                                    Text(
                                        text = "Asegurar mis Notificaciones",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Garantiza avisos diarios incluso con la app cerrada",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextMuted)
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Spacer(modifier = Modifier.height(4.dp))

                    // Nota explicativa honesta sobre el funcionamiento en segundo plano
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SoltarSurfaceElevated,
                        border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = SoltarAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Android detiene aplicaciones que no se abren con frecuencia para ahorrar batería. Revisa estos 5 puntos para que SOLTAR te proteja sin interrupciones.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }

                    // 1. Permiso de Notificaciones del Sistema
                    ReliabilityCheckCard(
                        title = "1. Notificaciones del Sistema",
                        description = if (notificationsEnabled) {
                            "Las notificaciones de SOLTAR y sus canales están activados."
                        } else {
                            "Las notificaciones están bloqueadas a nivel de sistema operativo."
                        },
                        status = if (notificationsEnabled) CheckStatus.OK else CheckStatus.ERROR,
                        actionLabel = if (notificationsEnabled) "Ver Ajustes" else "Activar Notificaciones",
                        onAction = {
                            try {
                                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                }
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.parse("package:${context.packageName}")
                                }
                                context.startActivity(intent)
                            }
                            refreshTrigger++
                        }
                    )

                    // 2. Alarmas Exactas (Android 12+)
                    ReliabilityCheckCard(
                        title = "2. Alarmas y Recordatorios Exactos",
                        description = if (exactAlarmsAllowed) {
                            "Permitido. Los avisos sonarán a la hora exacta configurada."
                        } else {
                            "Restringido. Android puede retrasar las alertas horas durante el modo reposo (Doze)."
                        },
                        status = if (exactAlarmsAllowed) CheckStatus.OK else CheckStatus.WARNING,
                        actionLabel = if (exactAlarmsAllowed) "Comprobar" else "Permitir Alarma Exacta",
                        onAction = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                try {
                                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                                        data = Uri.parse("package:${context.packageName}")
                                    }
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.parse("package:${context.packageName}")
                                    }
                                    context.startActivity(intent)
                                }
                            } else {
                                Toast.makeText(context, "Tu versión de Android no requiere este permiso", Toast.LENGTH_SHORT).show()
                            }
                            refreshTrigger++
                        }
                    )

                    // 3. Optimización de Batería (Sin Restricciones)
                    ReliabilityCheckCard(
                        title = "3. Batería sin Restricciones",
                        description = if (ignoringBatteryOptimizations) {
                            "Optimización desactivada. El sistema no dormirá los procesos de recordatorio."
                        } else {
                            "Optimización activa. Recomendado: Cambiar a 'Sin restricciones' para evitar omisión de notificaciones tras 24-48 horas."
                        },
                        status = if (ignoringBatteryOptimizations) CheckStatus.OK else CheckStatus.WARNING,
                        actionLabel = if (ignoringBatteryOptimizations) "Comprobado" else "Desactivar Restricción",
                        onAction = {
                            try {
                                val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                try {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.parse("package:${context.packageName}")
                                    }
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                            refreshTrigger++
                        }
                    )

                    // 4. Pausar actividad de la app si no se usa (Hibernación Android 11+)
                    ReliabilityCheckCard(
                        title = "4. Hibernación de Android (App sin uso)",
                        description = "En Android 11+, el sistema revoca permisos tras semanas sin uso. En Ajustes de la App, busca 'Pausar actividad de la app si no se usa' y desmárcalo.",
                        status = CheckStatus.INFO,
                        actionLabel = "Abrir Ajustes de la App",
                        onAction = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.parse("package:${context.packageName}")
                            }
                            context.startActivity(intent)
                            refreshTrigger++
                        }
                    )

                    // 5. Ajustes Específicos del Fabricante (Autoarranque)
                    ReliabilityCheckCard(
                        title = "5. Ajustes de Fabricante ($manufacturerName)",
                        description = getManufacturerGuidance(manufacturer),
                        status = CheckStatus.INFO,
                        actionLabel = "Abrir Ajustes de $manufacturerName",
                        onAction = {
                            openManufacturerSettings(context, manufacturer)
                        }
                    )

                    // 6. Modo Protección Continua (Foreground Service B7)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = SoltarSurface,
                        border = BorderStroke(1.dp, if (continuousProtectionEnabled) SoltarAmber.copy(alpha = 0.5f) else SoltarBorderSubtle)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = if (continuousProtectionEnabled) SoltarAmber else TextMuted
                                    )
                                    Column {
                                        Text(
                                            text = "Modo Protección Continua (Opcional)",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = if (continuousProtectionEnabled) "Activo • Servicio permanente en marcha" else "Inactivo (Recomendado por defecto)",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (continuousProtectionEnabled) SoltarAmber else TextMuted,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Switch(
                                    checked = continuousProtectionEnabled,
                                    onCheckedChange = { checked ->
                                        continuousProtectionEnabled = checked
                                        NotificationScheduler.setContinuousProtectionEnabled(context, checked)
                                        Toast.makeText(
                                            context,
                                            if (checked) "Protección continua activada" else "Protección continua desactivada",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = SoltarAmber,
                                        checkedTrackColor = SoltarAmber.copy(alpha = 0.3f),
                                        uncheckedThumbColor = TextMuted,
                                        uncheckedTrackColor = SoltarSurfaceElevated
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Mantiene una notificación fija discreta en la barra de estado con acceso inmediato a '🚨 SOS'. Evita que los fabricantes maten el proceso, con un consumo ligeramente mayor de batería.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // 7. Botón para probar la notificación en vivo
                    OutlinedButton(
                        onClick = {
                            SoltarNotificationHelper.sendDailyCheckinNotification(context)
                            Toast.makeText(context, "Notificación de prueba enviada al panel", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, SoltarSage)
                    ) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = SoltarSage, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Probar Notificación de Soberanía Ahora", color = SoltarSage, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

enum class CheckStatus {
    OK, WARNING, ERROR, INFO
}

@Composable
private fun ReliabilityCheckCard(
    title: String,
    description: String,
    status: CheckStatus,
    actionLabel: String,
    onAction: () -> Unit
) {
    val (statusColor, statusIcon) = when (status) {
        CheckStatus.OK -> Pair(SoltarSage, Icons.Default.CheckCircle)
        CheckStatus.WARNING -> Pair(SoltarAmber, Icons.Default.Warning)
        CheckStatus.ERROR -> Pair(UrgeAlertRed, Icons.Default.Cancel)
        CheckStatus.INFO -> Pair(TextSecondary, Icons.Default.HelpOutline)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = SoltarSurface,
        border = BorderStroke(1.dp, SoltarBorderSubtle)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = statusIcon,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = when (status) {
                            CheckStatus.OK -> "Activo"
                            CheckStatus.WARNING -> "Aviso"
                            CheckStatus.ERROR -> "Revisar"
                            CheckStatus.INFO -> "Guía"
                        },
                        color = statusColor,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 10.sp
                    )
                }
            }

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 11.5.sp,
                lineHeight = 16.sp
            )

            Button(
                onClick = onAction,
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SoltarSurfaceElevated),
                border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = actionLabel,
                    color = statusColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun getManufacturerGuidance(manufacturer: String): String {
    return when {
        manufacturer.contains("xiaomi") || manufacturer.contains("redmi") || manufacturer.contains("poco") ->
            "En MIUI / HyperOS: Entra en Seguridad > Administrar aplicaciones > SOLTAR > Activa 'Inicio automático' y en Ahorro de batería selecciona 'Sin restricciones'."
        manufacturer.contains("samsung") ->
            "En One UI: Entra en Ajustes > Batería > Límites de uso en segundo plano > Aplicaciones que nunca se suspenden > Añade SOLTAR."
        manufacturer.contains("huawei") || manufacturer.contains("honor") ->
            "En EMUI: Entra en Ajustes > Batería > Inicio de aplicaciones > Busca SOLTAR y pon 'Gestionar manualmente' (activa Autoarranque, Inicio secundario y En segundo plano)."
        manufacturer.contains("oppo") || manufacturer.contains("realme") ->
            "En ColorOS / Realme UI: Entra en Ajustes > Batería > Inicio automático > Permitir inicio en segundo plano para SOLTAR."
        manufacturer.contains("vivo") || manufacturer.contains("iqoo") ->
            "En FuntouchOS: Entra en Ajustes > Batería > Consumo elevado en segundo plano > Activa el permiso para SOLTAR."
        manufacturer.contains("oneplus") ->
            "En OxygenOS: Entra en Ajustes > Batería > Optimización de batería > Busca SOLTAR y selecciona 'No optimizar'."
        else ->
            "Asegúrate de permitir inicio en segundo plano y desactivar la suspensión automática en los ajustes del fabricante de tu dispositivo."
    }
}

private fun openManufacturerSettings(context: Context, manufacturer: String) {
    val intents = mutableListOf<Intent>()

    when {
        manufacturer.contains("xiaomi") || manufacturer.contains("redmi") || manufacturer.contains("poco") -> {
            intents.add(Intent().setClassName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"))
            intents.add(Intent().setClassName("com.miui.securitycenter", "com.miui.powercenter.PowerSettings"))
        }
        manufacturer.contains("huawei") || manufacturer.contains("honor") -> {
            intents.add(Intent().setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"))
            intents.add(Intent().setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.bootstart.BootStartActivity"))
        }
        manufacturer.contains("oppo") || manufacturer.contains("realme") -> {
            intents.add(Intent().setClassName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"))
            intents.add(Intent().setClassName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity"))
        }
        manufacturer.contains("vivo") || manufacturer.contains("iqoo") -> {
            intents.add(Intent().setClassName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"))
            intents.add(Intent().setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.PurviewTabActivity"))
        }
        manufacturer.contains("oneplus") -> {
            intents.add(Intent().setClassName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity"))
        }
        manufacturer.contains("samsung") -> {
            intents.add(Intent().setClassName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity"))
            intents.add(Intent().setClassName("com.samsung.android.sm", "com.samsung.android.sm.ui.battery.BatteryActivity"))
        }
    }

    var started = false
    for (intent in intents) {
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            started = true
            break
        } catch (_: Exception) {}
    }

    if (!started) {
        // Fallback a los ajustes generales de la aplicación
        try {
            val fallback = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${context.packageName}")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(fallback)
        } catch (_: Exception) {
            Toast.makeText(context, "Abre los Ajustes de Batería de tu dispositivo manualmente", Toast.LENGTH_LONG).show()
        }
    }
}

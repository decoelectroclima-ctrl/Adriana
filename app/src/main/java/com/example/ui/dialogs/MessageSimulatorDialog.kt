package com.example.ui.dialogs

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.audio.SoltarSoundManager
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.delay

private enum class MessageSimulatorStage {
    COMPOSING,
    CONTAINMENT_OPTIONS,
    WAITING_TEN_MINUTES
}

/**
 * Simulacro de mensaje a la expareja.
 * Permite desahogar de inmediato la urgencia impulsiva de escribir, conteniendo
 * el estímulo en un entorno seguro sin que el mensaje sea enviado jamás.
 * Una vez finalizado, ofrece tres vías de resolución consciente:
 * 1. Espera consciente guiada de 10 minutos (curva dopaminérgica).
 * 2. Acceso a las funciones de contención del protocolo SOS.
 * 3. Derivación del mensaje o llamada a un contacto de apoyo de confianza.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageSimulatorDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()
    val context = LocalContext.current

    val exName = remember(settings) {
        settings?.exPartnerName?.takeIf { it.isNotBlank() }
            ?: settings?.exName?.takeIf { it.isNotBlank() }
            ?: "tu expareja"
    }

    var currentStage by remember { mutableStateOf(MessageSimulatorStage.COMPOSING) }
    var draftedMessage by remember { mutableStateOf("") }
    var secondsRemaining by remember { mutableIntStateOf(600) } // 10 minutos = 600s
    var isTimerRunning by remember { mutableStateOf(false) }

    // Cuenta regresiva de 10 minutos cuando se activa
    LaunchedEffect(isTimerRunning, secondsRemaining) {
        if (isTimerRunning && secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining -= 1
        } else if (isTimerRunning && secondsRemaining == 0) {
            isTimerRunning = false
            viewModel.playSound(SoltarSoundManager.SoundType.CALM_BELL)
        }
    }

    val contacts = remember(settings) {
        listOfNotNull(
            if (!settings?.contact1Name.isNullOrBlank()) Triple(settings?.contact1Name!!, settings?.contact1Phone ?: "", settings?.contact1Relationship ?: "") else null,
            if (!settings?.contact2Name.isNullOrBlank()) Triple(settings?.contact2Name!!, settings?.contact2Phone ?: "", settings?.contact2Relationship ?: "") else null,
            if (!settings?.contact3Name.isNullOrBlank()) Triple(settings?.contact3Name!!, settings?.contact3Phone ?: "", settings?.contact3Relationship ?: "") else null
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
                .testTag("message_simulator_dialog"),
            shape = RoundedCornerShape(20.dp),
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
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = UrgeAlertRed.copy(alpha = 0.15f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.ChatBubbleOutline,
                                            contentDescription = null,
                                            tint = UrgeAlertRed,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = "SIMULACRO DE MENSAJE",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = UrgeAlertRed,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = when (currentStage) {
                                            MessageSimulatorStage.COMPOSING -> "Para: $exName (Simulado)"
                                            MessageSimulatorStage.CONTAINMENT_OPTIONS -> "Impulso contenido con éxito"
                                            MessageSimulatorStage.WAITING_TEN_MINUTES -> "Pausa consciente de 10 min"
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.size(32.dp).testTag("message_simulator_close")
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextMuted)
                            }
                        }
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    when (currentStage) {
                        MessageSimulatorStage.COMPOSING -> {
                            ComposingStage(
                                exName = exName,
                                draftedMessage = draftedMessage,
                                onMessageChange = { draftedMessage = it },
                                onFinishMessage = {
                                    if (draftedMessage.isNotBlank()) {
                                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                        currentStage = MessageSimulatorStage.CONTAINMENT_OPTIONS
                                    }
                                },
                                onCancel = onDismiss
                            )
                        }

                        MessageSimulatorStage.CONTAINMENT_OPTIONS -> {
                            ContainmentOptionsStage(
                                exName = exName,
                                message = draftedMessage,
                                contacts = contacts,
                                onStartTenMinutesWait = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.CALM_BELL)
                                    secondsRemaining = 600
                                    isTimerRunning = true
                                    currentStage = MessageSimulatorStage.WAITING_TEN_MINUTES
                                },
                                onOpenSosFunctions = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.URGE_ALERT)
                                    onDismiss()
                                    viewModel.openUrgeSheet()
                                },
                                onOpenSupportContact = {
                                    onDismiss()
                                    viewModel.openSupportContactDialog(1)
                                },
                                onSaveToJournal = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                    viewModel.saveJournalEntry(
                                        title = "Simulacro de mensaje a $exName",
                                        content = draftedMessage,
                                        moodTag = "Urgencia Contenida",
                                        framework = viewModel.uiState.value.preferredFramework,
                                        requestMentorship = false
                                    )
                                    viewModel.showNotification("✍️ Guardado como reflexión protegida en tu diario.")
                                    onDismiss()
                                },
                                onDestroyMessage = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.WARM_CHIME)
                                    viewModel.showNotification("🔥 Mensaje descartado. Tu soberanía permanece intacta.")
                                    onDismiss()
                                }
                            )
                        }

                        MessageSimulatorStage.WAITING_TEN_MINUTES -> {
                            TenMinutesWaitStage(
                                secondsRemaining = secondsRemaining,
                                isTimerRunning = isTimerRunning,
                                onToggleTimer = { isTimerRunning = !isTimerRunning },
                                onFinishWait = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.WARM_CHIME)
                                    viewModel.showNotification("🌊 Has surfeado la ola del impulso con éxito.")
                                    onDismiss()
                                },
                                onOpenSos = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.URGE_ALERT)
                                    onDismiss()
                                    viewModel.openUrgeSheet()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ComposingStage(
    exName: String,
    draftedMessage: String,
    onMessageChange: (String) -> Unit,
    onFinishMessage: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Aviso ético y de seguridad
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SoltarSurfaceElevated,
            border = BorderStroke(1.dp, UrgeAlertRed.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    tint = UrgeAlertRed,
                    modifier = Modifier.size(22.dp)
                )
                Column {
                    Text(
                        text = "Espacio Seguro de Catarsis",
                        style = MaterialTheme.typography.titleSmall,
                        color = UrgeAlertRed,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Escribe sin censura todo lo que sientes el impulso de decirle a $exName. Este mensaje NUNCA será enviado a nadie.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Panel de composición simulado estilo chat
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = SoltarSurface,
            border = BorderStroke(1.dp, SoltarBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Borrador de desahogo:",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Text(
                        text = "${draftedMessage.length} caracteres",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = draftedMessage,
                    onValueChange = onMessageChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 180.dp)
                        .testTag("message_simulator_input"),
                    placeholder = {
                        Text(
                            text = "Dile todo lo que te quema por dentro: reclamos, preguntas, dolor, explicaciones o lo que necesitas soltar...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SoltarAmber,
                        unfocusedBorderColor = SoltarBorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Botón de acción principal
        Button(
            onClick = onFinishMessage,
            enabled = draftedMessage.trim().isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("message_simulator_finish_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = SoltarAmber,
                disabledContainerColor = SoltarSurfaceElevated
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
                tint = if (draftedMessage.trim().isNotEmpty()) SoltarBackground else TextMuted,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Finalizar y Procesar Impulso",
                color = if (draftedMessage.trim().isNotEmpty()) SoltarBackground else TextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, SoltarBorderSubtle)
        ) {
            Text("Cancelar", color = TextSecondary)
        }
    }
}

@Composable
private fun ContainmentOptionsStage(
    exName: String,
    message: String,
    contacts: List<Triple<String, String, String>>,
    onStartTenMinutesWait: () -> Unit,
    onOpenSosFunctions: () -> Unit,
    onOpenSupportContact: () -> Unit,
    onSaveToJournal: () -> Unit,
    onDestroyMessage: () -> Unit
) {
    val context = LocalContext.current
    var showMessagePreview by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Tarjeta de felicitación y dignidad
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = SoltarSage.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, SoltarSage.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = SoltarSage.copy(alpha = 0.2f),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = SoltarSage, modifier = Modifier.size(22.dp))
                    }
                }
                Column {
                    Text(
                        text = "¡Excelente decisión! Impulso frenado",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarSage,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Has exteriorizado el mensaje sin exponerte a una respuesta dolorosa ni quebrar tu Contacto Cero. Tu dignidad está a salvo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary,
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Resumen plegable del mensaje contenido
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SoltarSurface,
            border = BorderStroke(1.dp, SoltarBorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mensaje contenido para $exName",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { showMessagePreview = !showMessagePreview }) {
                        Text(if (showMessagePreview) "Ocultar" else "Ver texto", fontSize = 11.sp, color = SoltarAmber)
                    }
                }
                if (showMessagePreview) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "«$message»",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Text(
            text = "¿Qué deseas hacer ahora para consolidar tu calma?",
            style = MaterialTheme.typography.titleSmall,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )

        // -------------------------------------------------------------
        // OPCIÓN 1: ESPERAR 10 MINUTOS (PAUSA CONSCIENTE)
        // -------------------------------------------------------------
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
            border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = SoltarAmber.copy(alpha = 0.15f),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.HourglassTop, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "1. Esperar 10 minutos (Pausa Consciente)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "La ola neuroquímica del impulso baja un 80% en 10 min",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onStartTenMinutesWait,
                    modifier = Modifier.fillMaxWidth().height(40.dp).testTag("option_ten_minutes_wait"),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Timer, contentDescription = null, tint = SoltarBackground, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Iniciar Pausa de 10 Minutos", color = SoltarBackground, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                }
            }
        }

        // -------------------------------------------------------------
        // OPCIÓN 2: FUNCIONES SOS / PROTOCOLO DE URGENCIA
        // -------------------------------------------------------------
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
            border = BorderStroke(1.dp, UrgeAlertRed.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = UrgeAlertRed.copy(alpha = 0.15f),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = UrgeAlertRed, modifier = Modifier.size(18.dp))
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "2. Realizar las funciones del SOS",
                            style = MaterialTheme.typography.bodyMedium,
                            color = UrgeAlertRed,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Modo Impulso de 20 min, regulación somática o EMDR",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onOpenSosFunctions,
                    modifier = Modifier.fillMaxWidth().height(40.dp).testTag("option_open_sos_functions"),
                    colors = ButtonDefaults.buttonColors(containerColor = UrgeAlertRed),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Emergency, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Abrir Protocolo SOS de Urgencia", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                }
            }
        }

        // -------------------------------------------------------------
        // OPCIÓN 3: ENVIAR A UN CONTACTO DE APOYO
        // -------------------------------------------------------------
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
            border = BorderStroke(1.dp, SoltarSage.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = SoltarSage.copy(alpha = 0.15f),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Groups, contentDescription = null, tint = SoltarSage, modifier = Modifier.size(18.dp))
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "3. Compartir con un contacto de apoyo",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SoltarSage,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Pide respaldo a alguien de tu confianza en vez de a tu ex",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (contacts.isNotEmpty()) {
                    contacts.forEach { (name, phone, rel) ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SoltarSurface,
                            border = BorderStroke(1.dp, SoltarBorderSubtle),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(name, style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                                    if (rel.isNotBlank()) {
                                        Text(rel, style = MaterialTheme.typography.labelSmall, color = SoltarSage, fontSize = 10.sp)
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    // WhatsApp a contacto de apoyo
                                    IconButton(
                                        onClick = {
                                            val clean = phone.replace("+", "").replace(" ", "").replace("-", "").trim()
                                            val text = "Hola $name, estoy pasando por un momento difícil con el impulso de escribirle a mi expareja. ¿Podemos hablar un momento?"
                                            val encoded = Uri.encode(text)
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$clean?text=$encoded"))
                                            context.startActivity(intent)
                                        },
                                        modifier = Modifier.size(32.dp).background(SoltarAmber.copy(alpha = 0.15f), CircleShape)
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "WhatsApp a $name", tint = SoltarAmber, modifier = Modifier.size(16.dp))
                                    }

                                    // Llamada a contacto de apoyo
                                    IconButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                            context.startActivity(intent)
                                        },
                                        modifier = Modifier.size(32.dp).background(SoltarSage.copy(alpha = 0.15f), CircleShape)
                                    ) {
                                        Icon(Icons.Default.Phone, contentDescription = "Llamar a $name", tint = SoltarSage, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = onOpenSupportContact,
                        modifier = Modifier.fillMaxWidth().height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, SoltarSage)
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, tint = SoltarSage, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Configurar contactos de apoyo", color = SoltarSage, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Acciones finales de resolución
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onSaveToJournal,
                modifier = Modifier.weight(1f).height(40.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, SoltarBorderSubtle)
            ) {
                Icon(Icons.Default.Book, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Guardar en diario", color = TextPrimary, fontSize = 11.5.sp)
            }

            Button(
                onClick = onDestroyMessage,
                modifier = Modifier.weight(1f).height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SoltarSurfaceElevated),
                border = BorderStroke(1.dp, UrgeAlertRed.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.DeleteForever, contentDescription = null, tint = UrgeAlertRed, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Destruir mensaje", color = UrgeAlertRed, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun TenMinutesWaitStage(
    secondsRemaining: Int,
    isTimerRunning: Boolean,
    onToggleTimer: () -> Unit,
    onFinishWait: () -> Unit,
    onOpenSos: () -> Unit
) {
    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    val progress = 1f - (secondsRemaining / 600f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SoltarSurfaceElevated,
            border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.3f))
        ) {
            Text(
                text = "La ciencia demuestra que el pico de dopamina y la urgencia impulsiva duran entre 8 y 12 minutos. Si resistes este tiempo, tu corteza prefrontal retomará el control racional.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 11.5.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(12.dp),
                textAlign = TextAlign.Center
            )
        }

        // Reloj central y progreso
        Surface(
            shape = CircleShape,
            color = SoltarSurface,
            border = BorderStroke(2.dp, SoltarAmber.copy(alpha = 0.6f)),
            modifier = Modifier.size(170.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = timeFormatted,
                        style = MaterialTheme.typography.headlineLarge,
                        color = if (secondsRemaining <= 60) SoltarSage else SoltarAmber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 38.sp
                    )
                    Text(
                        text = if (isTimerRunning) "Respirando..." else "En pausa",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = SoltarAmber,
            trackColor = SoltarSurfaceElevated
        )

        // Guía somática mientras espera
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = SoltarSurface,
            border = BorderStroke(1.dp, SoltarBorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Patrón de respiración 4-7-8",
                    style = MaterialTheme.typography.titleSmall,
                    color = SoltarAmber,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Inhala por la nariz en 4 segundos • Sostén 7 segundos • Exhala suavemente por la boca en 8 segundos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    fontSize = 11.5.sp,
                    lineHeight = 16.sp
                )
            }
        }

        // Acciones de control
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onToggleTimer,
                modifier = Modifier.weight(1f).height(44.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Icon(
                    imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isTimerRunning) "Pausar" else "Reanudar", color = TextPrimary, fontSize = 12.sp)
            }

            Button(
                onClick = onFinishWait,
                modifier = Modifier.weight(1f).height(44.dp).testTag("timer_complete_button"),
                colors = ButtonDefaults.buttonColors(containerColor = SoltarSage),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = SoltarBackground, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Ya me calmé", color = SoltarBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        OutlinedButton(
            onClick = onOpenSos,
            modifier = Modifier.fillMaxWidth().height(42.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = UrgeAlertRed),
            border = BorderStroke(1.dp, UrgeAlertRed.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Default.Bolt, contentDescription = null, tint = UrgeAlertRed, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Ir a funciones SOS ahora", color = UrgeAlertRed, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

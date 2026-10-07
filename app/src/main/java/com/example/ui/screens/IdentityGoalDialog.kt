package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.SoltarFramework
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun IdentityGoalDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val areas = listOf("Cuerpo y Salud", "Proyectos y Trabajo", "Amistades y Red", "Mente y Espacio Propio")
    val frequencies = listOf("Diario", "3 veces por semana", "Semanal")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = SoltarBackground,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SoltarSurface)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("goal_dialog_close")) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }

                    Text(
                        text = "OBJETIVO DE IDENTIDAD",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    TextButton(
                        onClick = {
                            viewModel.saveIdentityGoal()
                            onDismiss()
                        },
                        enabled = uiState.newGoalTitleInput.isNotBlank(),
                        modifier = Modifier.testTag("goal_save_button")
                    ) {
                        Text(
                            "Guardar",
                            color = if (uiState.newGoalTitleInput.isNotBlank()) SoltarAmber else TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Flag, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                            Text("Reconstrucción de Autonomía", color = SoltarAmber, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Cuando una relación termina, recuperas tiempo y energía. ¿En qué persona eliges convertirte ahora?",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                val journals by viewModel.journalEntries.collectAsState()
                val settings by viewModel.settings.collectAsState()
                val framework = SoltarFramework.fromKey(settings?.preferredFramework)
                val suggestions = remember(journals, framework) {
                    generateIdentityGoalSuggestionsLocal(
                        journals = journals,
                        onboardingAnswers = mapOf(
                            "breakupReason" to (settings?.breakupReason ?: ""),
                            "userName" to (settings?.userName ?: "")
                        ),
                        currentPhase = "Reconstrucción",
                        framework = framework
                    )
                }

                if (suggestions.isNotEmpty()) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(16.dp))
                            Text(
                                text = "Sugerencias según tu proceso (IA On-Device):",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        suggestions.forEach { sug ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable {
                                        viewModel.setNewGoalTitle(sug.actionTitle)
                                        viewModel.setWhoIWantToBe(sug.whoIWantToBe)
                                        if (areas.contains(sug.area)) {
                                            viewModel.setIdentityArea(sug.area)
                                        }
                                    },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SoltarBorder)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = sug.actionTitle,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = sug.area,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SoltarSage,
                                            fontSize = 11.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Identidad: ${sug.whoIWantToBe}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Área
                Column {
                    Text("Área de Vida", style = MaterialTheme.typography.labelMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        areas.take(2).forEach { area ->
                            val selected = uiState.identityAreaSelected == area
                            FilterChip(
                                selected = selected,
                                onClick = { viewModel.setIdentityArea(area) },
                                label = { Text(area, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SoltarAmber,
                                    selectedLabelColor = SoltarBackground,
                                    containerColor = SoltarSurfaceElevated,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        areas.drop(2).forEach { area ->
                            val selected = uiState.identityAreaSelected == area
                            FilterChip(
                                selected = selected,
                                onClick = { viewModel.setIdentityArea(area) },
                                label = { Text(area, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SoltarAmber,
                                    selectedLabelColor = SoltarBackground,
                                    containerColor = SoltarSurfaceElevated,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }
                }

                // Título de la meta / hábito
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Acción o Hábito Concreto", style = MaterialTheme.typography.labelMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        TextButton(
                            onClick = {
                                val habits = suggestIdentityHabitsLocal(
                                    lifeArea = uiState.identityAreaSelected,
                                    whoIWantToBe = uiState.whoIWantToBeInput,
                                    framework = framework
                                )
                                if (habits.isNotEmpty()) {
                                    viewModel.setNewGoalTitle(habits.first())
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(14.dp))
                                Text("Sugerir hábito (IA)", style = MaterialTheme.typography.labelSmall, color = SoltarAmber, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.newGoalTitleInput,
                        onValueChange = viewModel::setNewGoalTitle,
                        placeholder = { Text("Ej: Ir a nadar 30 min, retomar mis clases de dibujo", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("goal_input_title"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarAmber,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Quién elijo ser con esta acción
                Column {
                    Text("Identidad deseada ('Elijo ser una persona que...')", style = MaterialTheme.typography.labelMedium, color = SoltarSage, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.whoIWantToBeInput,
                        onValueChange = viewModel::setWhoIWantToBe,
                        placeholder = { Text("Ej: Que cuida su salud y no descuida sus pasiones por nadie", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("goal_input_who_i_want_to_be"),
                        minLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarSage,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Frecuencia
                Column {
                    Text("Frecuencia", style = MaterialTheme.typography.labelMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        frequencies.forEach { freq ->
                            val selected = uiState.newGoalFrequencyInput == freq
                            FilterChip(
                                selected = selected,
                                onClick = { viewModel.setNewGoalFrequency(freq) },
                                label = { Text(freq, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SoltarAmber,
                                    selectedLabelColor = SoltarBackground,
                                    containerColor = SoltarSurfaceElevated,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        viewModel.saveIdentityGoal()
                        onDismiss()
                    },
                    enabled = uiState.newGoalTitleInput.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("goal_submit_cta"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber, contentColor = SoltarBackground)
                ) {
                    Text("Registrar objetivo de identidad", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

private fun generateIdentityGoalSuggestionsLocal(
    journals: List<com.example.data.JournalEntryEntity>,
    onboardingAnswers: Map<String, String>,
    currentPhase: String,
    framework: SoltarFramework
): List<com.example.ai.IdentityGoalSuggestion> {
    return when (framework) {
        SoltarFramework.ESTOICO -> listOf(
            com.example.ai.IdentityGoalSuggestion("Entrenar disciplina física", "Pilar de carácter", "Cuerpo y Salud"),
            com.example.ai.IdentityGoalSuggestion("Focalizar en mi carrera", "Recuperar autonomía soberana", "Proyectos y Trabajo"),
            com.example.ai.IdentityGoalSuggestion("Frecuentar amigos virtuosos", "Valorar virtud y razón", "Amistades y Red")
        )
        SoltarFramework.CATOLICO -> listOf(
            com.example.ai.IdentityGoalSuggestion("Rezar el Santo Rosario", "Pedir fortaleza y conversión", "Mente y Espacio Propio"),
            com.example.ai.IdentityGoalSuggestion("Cuidar el templo de Dios", "Descanso ordenado", "Cuerpo y Salud"),
            com.example.ai.IdentityGoalSuggestion("Obras de caridad", "Participación en la comunidad", "Amistades y Red")
        )
        else -> listOf(
            com.example.ai.IdentityGoalSuggestion("Respiración consciente", "Practicar ante rumia", "Mente y Espacio Propio"),
            com.example.ai.IdentityGoalSuggestion("Caminar al aire libre", "Sin distracciones", "Cuerpo y Salud"),
            com.example.ai.IdentityGoalSuggestion("Horario estricto personal", "Proyectos personales diarios", "Proyectos y Trabajo")
        )
    }
}

private fun suggestIdentityHabitsLocal(
    lifeArea: String,
    whoIWantToBe: String,
    framework: SoltarFramework
): List<String> {
    return when (framework) {
        SoltarFramework.ESTOICO -> listOf(
            "Escribir un diario de gratitud estoica al amanecer",
            "Realizar 15 minutos de ejercicio de resistencia",
            "Bloquear distractores durante 3 horas de trabajo profundo"
        )
        SoltarFramework.CATOLICO -> listOf(
            "Realizar una oración de ofrecimiento del día al despertar",
            "Hacer una visita breve al Santísimo Sacramento",
            "Evitar quejas y practicar la paciencia en el hogar"
        )
        else -> listOf(
            "Hacer 10 respiraciones diafragmáticas al empezar el día",
            "Estirar el cuerpo durante 15 minutos al llegar la noche",
            "Limitar el uso de redes sociales a 30 minutos diarios"
        )
    }
}


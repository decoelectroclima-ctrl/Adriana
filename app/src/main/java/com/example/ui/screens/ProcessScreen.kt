package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoltarSoundManager
import com.example.data.CheckinEntity
import com.example.data.UnsentLetterEntity
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun ProcessScreen(
    viewModel: SoltarViewModel,
    modifier: Modifier = Modifier
) {
    val checkins by viewModel.checkins.collectAsState()
    val urgeEpisodes by viewModel.urgeEpisodes.collectAsState()
    val thoughts by viewModel.thoughts.collectAsState()
    val audits by viewModel.audits.collectAsState()
    val idealizations by viewModel.idealizations.collectAsState()
    val letters by viewModel.letters.collectAsState()
    val relapses by viewModel.relapses.collectAsState()
    val triggerEvents by viewModel.triggerEvents.collectAsState()
    val journalEntries by viewModel.journalEntries.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val entitlements = remember(settings) { com.example.data.UserEntitlements.fromSettings(settings) }

    var selectedLetterForTimeCapsule by remember { mutableStateOf<UnsentLetterEntity?>(null) }

    var selectedFilter by remember { mutableStateOf("Todos") }
    val filterOptions = listOf("Todos", "Diario", "Impulsos", "Pensamientos", "Auditorías", "Cartas", "Idealización", "Recaídas")

    var selectedMetricDays by remember { mutableStateOf(7) }
    val realTimeline by viewModel.realEvolutionTimeline.collectAsState()

    val totalUrgesContained = urgeEpisodes.size
    val totalThoughtsRestructured = thoughts.size
    val totalAuditsSaved = audits.size
    val totalLettersStored = letters.size
    val totalRelapses = relapses.size
    val totalJournalEntries = journalEntries.size

    val focusCompletionRate = remember(checkins) {
        if (checkins.isEmpty()) 0 else {
            val totalPossible = checkins.size * 3
            val totalDone = checkins.sumOf {
                (if (it.focusBodyDone) 1 else 0) + (if (it.focusSelfDone) 1 else 0) + (if (it.focusSocialDone) 1 else 0)
            }
            ((totalDone.toFloat() / totalPossible) * 100).toInt()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoltarBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "TU PROCESO DE EVOLUCIÓN",
                    style = MaterialTheme.typography.labelMedium,
                    color = SoltarAmber,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Evidencia objetiva de tu avance",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "El dolor no es lineal, pero tu compromiso con tu dignidad deja un rastro medible.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                
                val context = LocalContext.current
                val settings by viewModel.settings.collectAsState()
                val startTs = settings?.breakupDateTimestamp ?: (System.currentTimeMillis() - (14L * 24 * 3600 * 1000))
                val days = ((System.currentTimeMillis() - startTs) / (24 * 3600 * 1000L)).coerceAtLeast(0L)

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        val uri = generateShareableCardBitmap(
                            context = context,
                            title = "Proceso SOLTAR",
                            subtitle = "Día $days de Reconstrucción",
                            quote = "“La soberanía interior se construye un día a la vez.”",
                            streakText = "Racha activa • $days días"
                        )
                        if (uri != null) {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "image/png"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Compartir Tarjeta de Hito SOLTAR"))
                        } else {
                            Toast.makeText(context, "No se pudo generar la tarjeta", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = SoltarBackground)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Compartir Tarjeta de Hito (D1)", color = SoltarBackground, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Summary Metric Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Impulsos Contenidos",
                        value = "$totalUrgesContained",
                        icon = Icons.Default.Bolt,
                        accentColor = UrgeAlertRed
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Bucles Cerrados",
                        value = "$totalThoughtsRestructured",
                        icon = Icons.Default.Psychology,
                        accentColor = SoltarAmber
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Auditorías Reales",
                        value = "$totalAuditsSaved",
                        icon = Icons.Default.Balance,
                        accentColor = SoltarSage
                    )
                }
                StatCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Consistencia en Acción",
                    value = "$focusCompletionRate%",
                    icon = Icons.Default.CheckCircle,
                    accentColor = SoltarSage,
                    subtitle = "De tus 3 focos diarios completados de verdad, no solo planeados."
                )
            }
        }

        // C3: Linguistic Analysis Card
        item {
            val linguisticAnalysis by viewModel.linguisticProgress.collectAsState()
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("linguistic_analysis_card"),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SoltarAmber.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = SoltarAmber)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ANÁLISIS LINGÜÍSTICO Y CLÍNICO (C3)",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Evaluación semántica de tus entradas de diario",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }

                    // Metrics Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = SoltarBackground),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Autonomía", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${linguisticAnalysis.nivelAutonomia}/10",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SoltarSage
                                )
                            }
                        }
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = SoltarBackground),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Rumiación", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${linguisticAnalysis.lenguajeRumiativo}/10",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (linguisticAnalysis.lenguajeRumiativo > 6) UrgeAlertRed else SoltarAmber
                                )
                            }
                        }
                    }

                    // Cognitive Distortions
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Distorsiones cognitivas detectadas:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        if (linguisticAnalysis.distorsionesCognitivas.isEmpty()) {
                            Text(
                                text = "• Ninguna distorsión crítica detectada en tus registros recientes.",
                                style = MaterialTheme.typography.bodySmall,
                                color = SoltarSage
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                linguisticAnalysis.distorsionesCognitivas.forEach { distortion ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = UrgeAlertRed.copy(alpha = 0.15f),
                                        border = BorderStroke(1.dp, UrgeAlertRed.copy(alpha = 0.4f))
                                    ) {
                                        Text(
                                            text = distortion,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = UrgeAlertRed,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Change / Evolution
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Evolución desde última entrada:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = linguisticAnalysis.cambioDesdeUltimaEntrada,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Emotional Evolution Chart (Interactive Line Graph)
        item {
            if (entitlements.canAccessAdvancedCharts) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("evolution_chart_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "EVOLUCIÓN EMOCIONAL REAL",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SoltarAmber,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Calculada solo con tus check-ins y actividad registrada",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }

                            // Day range selector
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                listOf(7, 14, 30).forEach { days ->
                                    val isSelected = selectedMetricDays == days
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (isSelected) SoltarAmber else SoltarSurfaceElevated,
                                        border = BorderStroke(1.dp, if (isSelected) SoltarAmber else SoltarBorderSubtle),
                                        modifier = Modifier.clickable {
                                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                            selectedMetricDays = days
                                            viewModel.setEvolutionRangeDays(days)
                                        }
                                    ) {
                                        Text(
                                            text = "${days}d",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            color = if (isSelected) SoltarBackground else TextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Multi-line Canvas Chart connected to real entries
                        EvolutionLineChart(
                            timeline = realTimeline,
                            onOpenCheckin = { viewModel.openEmotionalCheckin() },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Legend
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            ChartLegendItem(label = "Dolor", color = UrgeAlertRed)
                            ChartLegendItem(label = "Ansiedad", color = SoltarTerracotta)
                            ChartLegendItem(label = "Nostalgia", color = SoltarAmber)
                            ChartLegendItem(label = "Impulso", color = UrgeAlertRed.copy(alpha = 0.6f))
                            ChartLegendItem(label = "Autonomía", color = SoltarSage)
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "● check-in · ○ estimado desde tu actividad · línea discontinua = días sin registro",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("evolution_chart_locked_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = SoltarAmber.copy(alpha = 0.15f),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Función Premium bloqueada",
                                    tint = SoltarAmber,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "EVOLUCIÓN EMOCIONAL AVANZADA",
                            style = MaterialTheme.typography.labelSmall,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Los gráficos de evolución avanzados son parte de Recuerda Premium",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Visualiza la trayectoria de dolor, autonomía y regulación somática a lo largo de semanas.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.openPaywall(com.example.data.SubscriptionPlan.MONTHLY) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("view_plans_chart_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                        ) {
                            Text(
                                text = "Ver planes",
                                color = SoltarBackground,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filterOptions) { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            selectedFilter = filter
                        },
                        label = { Text(filter, fontSize = 12.sp) },
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

        // Timeline Items

        // Recaídas registradas (con enfoque compasivo)
        if (selectedFilter == "Todos" || selectedFilter == "Recaídas") {
            if (relapses.isNotEmpty()) {
                item {
                    Text(
                        text = "🤝 Registros de Recaída (Puntos de Información)",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarSage,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    val checkins by viewModel.checkins.collectAsState()
                    val relapseAnalysis = remember(relapses, urgeEpisodes, checkins) {
                        analyzeRelapsePatternsLocal(
                            relapses = relapses,
                            urgeEpisodes = urgeEpisodes,
                            recentCheckins = checkins
                        )
                    }
                    val temporalPattern = remember(triggerEvents) {
                        analyzeRelapsePatternsLocal(triggerEvents)
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                        border = BorderStroke(1.5.dp, SoltarAmber)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(20.dp))
                                Text(
                                    text = "Análisis de Causa Raíz (IA On-Device)",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = SoltarAmber,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = relapseAnalysis,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                lineHeight = 20.sp
                            )

                            if (temporalPattern != null) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    color = SoltarAmber.copy(alpha = 0.1f),
                                    border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "PATRÓN RECURRENTE A LO LARGO DEL TIEMPO",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SoltarAmber,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = temporalPattern,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextPrimary,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                items(relapses) { relapse ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = relapse.whatHappened,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SoltarSage.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "Información útil",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        color = SoltarSage,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("• Detonante: ${relapse.trigger}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Text("• Emoción: ${relapse.emotion}", style = MaterialTheme.typography.bodySmall, color = SoltarTerracotta)
                            if (relapse.learning.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("💡 Aprendizaje: ${relapse.learning}", style = MaterialTheme.typography.bodySmall, color = SoltarAmber, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        // 0. Diario Personal & Mentorías Filosóficas
        if (selectedFilter == "Todos" || selectedFilter == "Diario") {
            if (journalEntries.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📖 Diario Personal & Mentorías",
                            style = MaterialTheme.typography.titleSmall,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { viewModel.openJournalModal() }) {
                            Text("Abrir diario", color = SoltarAmber, fontSize = 12.sp)
                        }
                    }
                }

                items(journalEntries) { entry ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                viewModel.openJournalModal(entry)
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                        border = BorderStroke(1.dp, if (entry.aiFeedback.isNotBlank()) SoltarAmber.copy(alpha = 0.4f) else SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SoltarSurfaceElevated
                                ) {
                                    Text(
                                        text = entry.moodTag,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                IconButton(onClick = { viewModel.deleteJournalEntry(entry.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = TextMuted, modifier = Modifier.size(18.dp))
                                }
                            }
                            if (entry.title.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = entry.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = entry.content,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                maxLines = 2,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                            if (entry.aiCorePrinciple.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "«${entry.aiCorePrinciple}»",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SoltarAmber,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    maxLines = 2,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // 1. Impulsos
        if (selectedFilter == "Todos" || selectedFilter == "Impulsos") {
            if (urgeEpisodes.isNotEmpty()) {
                item {
                    Text(
                        text = "🛡️ Impulsos Regulados con Éxito",
                        style = MaterialTheme.typography.titleSmall,
                        color = UrgeAlertRed,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(urgeEpisodes) { urge ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Emoción: ${urge.emotion}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = UrgeAlertRed.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "${urge.initialIntensity} ➔ ${urge.finalIntensity} / 10",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        color = UrgeAlertRed,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Deseo inicial: ${urge.desiredAction}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            if (urge.learning.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "💡 Aprendizaje: ${urge.learning}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SoltarAmber,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Pensamientos Reestructurados (TCC)
        if (selectedFilter == "Todos" || selectedFilter == "Pensamientos") {
            if (thoughts.isNotEmpty()) {
                item {
                    Text(
                        text = "🧠 Pensamientos Intrusivos Desarmados",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(thoughts) { thought ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "«${thought.originalThought}»",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = { viewModel.deleteThought(thought.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = TextMuted, modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("• Hecho real: ${thought.fact}", style = MaterialTheme.typography.bodySmall, color = SoltarSage)
                            Text("• Interpretación: ${thought.interpretation}", style = MaterialTheme.typography.bodySmall, color = SoltarTerracotta)
                            if (thought.concreteAction.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("• Acción de anclaje: ${thought.concreteAction}", style = MaterialTheme.typography.bodySmall, color = SoltarAmber)
                            }
                        }
                    }
                }
            }
        }

        // 3. Auditorías de la Relación
        if (selectedFilter == "Todos" || selectedFilter == "Auditorías") {
            if (audits.isNotEmpty()) {
                item {
                    Text(
                        text = "⚖️ Auditorías de Responsabilidad",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarSage,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    val aggregatedRedFlagPattern = remember(audits) {
                        synthesizeRedFlagsPatternLocal(
                            audits.map { "${it.title}: ${it.otherResponsibility}. Patrón: ${it.patternIdentified}" }
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                        border = BorderStroke(1.dp, SoltarSage)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Psychology, contentDescription = null, tint = SoltarSage, modifier = Modifier.size(18.dp))
                                Text(
                                    text = "Patrón Estructural de Fondo (IA On-Device)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = SoltarSage,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = aggregatedRedFlagPattern,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                items(audits) { audit ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = audit.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = { viewModel.deleteAudit(audit.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = TextMuted, modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Mi responsabilidad: ${audit.myResponsibility}", style = MaterialTheme.typography.bodySmall, color = SoltarSage)
                            Text("Su responsabilidad: ${audit.otherResponsibility}", style = MaterialTheme.typography.bodySmall, color = SoltarTerracotta)
                            if (audit.patternIdentified.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Patrón identificado: ${audit.patternIdentified}", style = MaterialTheme.typography.bodySmall, color = SoltarAmber)
                            }
                        }
                    }
                }
            }
        }

        // 4. Cartas No Enviadas
        if (selectedFilter == "Todos" || selectedFilter == "Cartas") {
            if (letters.isNotEmpty()) {
                item {
                    Text(
                        text = "✉️ Cartas Privadas y Selladas",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarBlue,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(letters) { letter ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = letter.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = letter.category,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SoltarBlue
                                    )
                                }
                                if (letter.isClosed) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = SoltarSage.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = "Sellada 🕯️",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            color = SoltarSage,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 11.sp
                                        )
                                    }
                                } else {
                                    TextButton(onClick = { viewModel.performLetterCeremony(letter.id) }) {
                                        Text("Sellar 🕯️", color = SoltarAmber, fontSize = 12.sp)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = letter.content,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                maxLines = 4
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = { selectedLetterForTimeCapsule = letter },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(14.dp))
                                        Text("Hallazgo de Cambio (IA)", style = MaterialTheme.typography.labelSmall, color = SoltarAmber, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Antídotos de Idealización
        if (selectedFilter == "Todos" || selectedFilter == "Idealización") {
            if (idealizations.isNotEmpty()) {
                item {
                    Text(
                        text = "💡 Antídotos de Idealización Registrados",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarTerracotta,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(idealizations) { pair ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Lo que mi mente romantizaba:", style = MaterialTheme.typography.labelSmall, color = SoltarTerracotta, fontWeight = FontWeight.Bold)
                                IconButton(onClick = { viewModel.deleteIdealization(pair.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = TextMuted, modifier = Modifier.size(16.dp))
                                }
                            }
                            Text(pair.whatIMiss, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("La realidad que también existía:", style = MaterialTheme.typography.labelSmall, color = SoltarSage, fontWeight = FontWeight.Bold)
                            Text(pair.whatIActuallyExperienced, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                        }
                    }
                }
            }
        }

        // Empty state placeholder
        if (urgeEpisodes.isEmpty() && thoughts.isEmpty() && audits.isEmpty() && letters.isEmpty() && idealizations.isEmpty() && relapses.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Timeline, contentDescription = null, tint = TextMuted, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Aún no hay registros en tu proceso",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "A medida que contengas impulsos, cierres bucles de pensamiento y evalúes tu día, verás aquí la gráfica de tu reconstrucción.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    selectedLetterForTimeCapsule?.let { letter ->
        com.example.ui.dialogs.TimeCapsuleComparisonDialog(
            letter = letter,
            recentJournals = journalEntries,
            onDismiss = { selectedLetterForTimeCapsule = null }
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    accentColor: Color,
    subtitle: String? = null
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
        border = BorderStroke(1.dp, SoltarBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp, maxLines = 2)
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted, fontSize = 10.sp, lineHeight = 13.sp)
            }
        }
    }
}

@Composable
private fun ChartLegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(text = label, color = TextSecondary, fontSize = 10.sp)
    }
}

@Composable
private fun EvolutionLineChart(
    timeline: com.example.ai.RealPersonalEvolutionTimeline,
    onOpenCheckin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dataPoints = timeline.points
    if (dataPoints.isEmpty()) return

    val realPointsCount = dataPoints.count { it.isRealEntry }
    val checkinPointsCount = dataPoints.count { it.source == com.example.ai.MetricSource.CHECKIN && !it.pain.isNaN() }

    var selectedPointIndex by remember(timeline) {
        val lastRealIdx = dataPoints.indexOfLast { it.isRealEntry }
        mutableIntStateOf(if (lastRealIdx >= 0) lastRealIdx else dataPoints.lastIndex.coerceAtLeast(0))
    }
    val safeIndex = selectedPointIndex.coerceIn(0, dataPoints.lastIndex)
    val selectedPoint = dataPoints[safeIndex]

    val amberColor = SoltarAmber
    val gridColor = SoltarBorderSubtle
    val painColor = UrgeAlertRed
    val anxietyColor = SoltarTerracotta
    val nostalgiaColor = amberColor
    val urgeColor = UrgeAlertRed.copy(alpha = 0.6f)
    val autonomyColor = SoltarSage
    val backgroundColor = SoltarBackground

    Column(modifier = modifier) {
        if (realPointsCount == 0) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                shape = RoundedCornerShape(12.dp),
                color = SoltarSurfaceElevated,
                border = BorderStroke(1.dp, SoltarBorderSubtle)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShowChart,
                        contentDescription = null,
                        tint = SoltarAmber.copy(alpha = 0.6f),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Sin registros suficientes para graficar",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Completa tus check-ins diarios para visualizar tu curva real de evolución emocional.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp
                    )
                }
            }
        } else {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .pointerInput(dataPoints) {
                        detectTapGestures { offset ->
                            val paddingLeft = 24f
                            val paddingRight = 24f
                            val usableWidth = size.width - paddingLeft - paddingRight
                            val stepX = usableWidth / (dataPoints.size - 1).coerceAtLeast(1)
                            val relativeX = offset.x - paddingLeft
                            val tappedIdx = ((relativeX + stepX / 2f) / stepX).toInt().coerceIn(0, dataPoints.lastIndex)
                            selectedPointIndex = tappedIdx
                        }
                    }
            ) {
                val width = size.width
                val height = size.height
                val paddingLeft = 24f
                val paddingRight = 24f
                val paddingTop = 12f
                val paddingBottom = 20f

                val usableWidth = width - paddingLeft - paddingRight
                val usableHeight = height - paddingTop - paddingBottom

                // Draw horizontal grid lines (0, 5, 10)
                for (i in 0..2) {
                    val y = paddingTop + (usableHeight / 2) * i
                    drawLine(
                        color = gridColor,
                        start = Offset(paddingLeft, y),
                        end = Offset(width - paddingRight, y),
                        strokeWidth = 1f
                    )
                }

                val stepX = usableWidth / (dataPoints.size - 1).coerceAtLeast(1)

                // Draw vertical indicator for selected day
                val selectedX = paddingLeft + safeIndex * stepX
                drawLine(
                    color = amberColor.copy(alpha = 0.4f),
                    start = Offset(selectedX, paddingTop),
                    end = Offset(selectedX, height - paddingBottom),
                    strokeWidth = 1.5f
                )

                fun DrawScope.drawMetricPath(
                    extractValue: (com.example.ai.EvolutionMetricPoint) -> Float,
                    color: Color,
                    strokeWidth: Float = 2.5f
                ) {
                    val validIndexedPoints = dataPoints.mapIndexedNotNull { index, point ->
                        val v = extractValue(point)
                        if (!v.isNaN() && point.source != com.example.ai.MetricSource.NONE) {
                            Pair(index, v)
                        } else null
                    }

                    if (validIndexedPoints.size >= 2) {
                        for (i in 0 until validIndexedPoints.size - 1) {
                            val (idx1, v1) = validIndexedPoints[i]
                            val (idx2, v2) = validIndexedPoints[i + 1]

                            val x1 = paddingLeft + idx1 * stepX
                            val y1 = paddingTop + ((10f - v1.coerceIn(0f, 10f)) / 10f) * usableHeight
                            val x2 = paddingLeft + idx2 * stepX
                            val y2 = paddingTop + ((10f - v2.coerceIn(0f, 10f)) / 10f) * usableHeight

                            val isConsecutive = idx2 == idx1 + 1
                            val isBothCheckin = dataPoints[idx1].source == com.example.ai.MetricSource.CHECKIN &&
                                dataPoints[idx2].source == com.example.ai.MetricSource.CHECKIN

                            if (isConsecutive && isBothCheckin) {
                                drawLine(
                                    color = color,
                                    start = Offset(x1, y1),
                                    end = Offset(x2, y2),
                                    strokeWidth = strokeWidth,
                                    cap = StrokeCap.Round
                                )
                            } else {
                                // Draw dashed bridging line
                                drawLine(
                                    color = color.copy(alpha = 0.6f),
                                    start = Offset(x1, y1),
                                    end = Offset(x2, y2),
                                    strokeWidth = strokeWidth * 0.8f,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f),
                                    cap = StrokeCap.Round
                                )
                            }
                        }
                    }

                    // Draw dots for each valid entry
                    validIndexedPoints.forEach { (index, value) ->
                        val pt = dataPoints[index]
                        val x = paddingLeft + index * stepX
                        val y = paddingTop + ((10f - value.coerceIn(0f, 10f)) / 10f) * usableHeight

                        when (pt.source) {
                            com.example.ai.MetricSource.CHECKIN -> {
                                drawCircle(
                                    color = color,
                                    radius = if (index == safeIndex) 5f else 3f,
                                    center = Offset(x, y)
                                )
                                if (index == safeIndex) {
                                    drawCircle(
                                        color = backgroundColor,
                                        radius = 2f,
                                        center = Offset(x, y)
                                    )
                                }
                            }
                            com.example.ai.MetricSource.ESTIMATED -> {
                                drawCircle(
                                    color = color,
                                    radius = if (index == safeIndex) 4.5f else 2.5f,
                                    center = Offset(x, y),
                                    style = Stroke(width = 1.5f)
                                )
                            }
                            com.example.ai.MetricSource.NONE -> {
                                // Do not draw dots for empty days
                            }
                        }
                    }
                }

                // Draw the 5 emotional dimensions
                drawMetricPath({ it.pain }, painColor)
                drawMetricPath({ it.anxiety }, anxietyColor)
                drawMetricPath({ it.nostalgia }, nostalgiaColor)
                drawMetricPath({ it.urgeToContact }, urgeColor)
                drawMetricPath({ it.autonomy }, autonomyColor, strokeWidth = 3f)

                // Draw bottom indicator dot below axis for days with real entries
                dataPoints.forEachIndexed { index, point ->
                    val x = paddingLeft + index * stepX
                    if (point.source == com.example.ai.MetricSource.CHECKIN) {
                        drawCircle(
                            color = amberColor,
                            radius = 3f,
                            center = Offset(x, height - 8f)
                        )
                    } else if (point.source == com.example.ai.MetricSource.ESTIMATED) {
                        drawCircle(
                            color = painColor,
                            radius = 2f,
                            center = Offset(x, height - 8f),
                            style = Stroke(width = 1f)
                        )
                    }
                }
            }
        }

        // X-Axis day labels row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val step = when {
                dataPoints.size <= 7 -> 1
                dataPoints.size <= 14 -> 2
                else -> 5
            }
            dataPoints.forEachIndexed { idx, pt ->
                if (idx % step == 0 || idx == dataPoints.lastIndex) {
                    Text(
                        text = pt.displayLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (idx == safeIndex) SoltarAmber else TextSecondary,
                        fontWeight = if (idx == safeIndex) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Selected Day Inspector Card
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = SoltarSurfaceElevated,
            border = BorderStroke(1.dp, SoltarBorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "📅 ${selectedPoint.displayLabel}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "(${selectedPoint.fullDate})",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    val badgeLabel = when (selectedPoint.source) {
                        com.example.ai.MetricSource.CHECKIN -> "Check-in real"
                        com.example.ai.MetricSource.ESTIMATED -> "Estimado (actividad)"
                        com.example.ai.MetricSource.NONE -> "Sin registro"
                    }
                    val badgeColor = when (selectedPoint.source) {
                        com.example.ai.MetricSource.CHECKIN -> SoltarAmber
                        com.example.ai.MetricSource.ESTIMATED -> SoltarTerracotta
                        com.example.ai.MetricSource.NONE -> TextSecondary
                    }

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = badgeColor.copy(alpha = 0.12f),
                        border = BorderStroke(0.5.dp, badgeColor.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = badgeLabel,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                            color = badgeColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Origen: ${selectedPoint.sourceDescription}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontSize = 10.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Five values breakdown chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricMiniBadge(label = "Dolor", value = selectedPoint.pain, color = painColor)
                    MetricMiniBadge(label = "Ansiedad", value = selectedPoint.anxiety, color = anxietyColor)
                    MetricMiniBadge(label = "Nostalgia", value = selectedPoint.nostalgia, color = nostalgiaColor)
                    MetricMiniBadge(label = "Impulso", value = selectedPoint.urgeToContact, color = urgeColor)
                    MetricMiniBadge(label = "Autonomía", value = selectedPoint.autonomy, color = autonomyColor)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Clinical summary insight and timeline stats
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = SoltarSurface.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "💡 ${timeline.summaryInsight}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextPrimary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "• ${timeline.totalLoggedDays} de ${timeline.rangeDays} días con registros",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                    if (checkinPointsCount >= 2) {
                        Text(
                            text = if (timeline.autonomyChange >= 0f) "Autonomía: +${String.format(java.util.Locale.US, "%.1f", timeline.autonomyChange)}" else "Autonomía: ${String.format(java.util.Locale.US, "%.1f", timeline.autonomyChange)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = SoltarSage,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // If today has no direct check-in, prompt the user
        val lastPoint = dataPoints.lastOrNull()
        if (lastPoint != null && !lastPoint.hasDirectCheckin) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onOpenCheckin,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.6f)),
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = null,
                    tint = SoltarAmber,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Registrar Check-in de Hoy para marcar este día",
                    style = MaterialTheme.typography.labelSmall,
                    color = SoltarAmber,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun MetricMiniBadge(label: String, value: Float, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 11.sp, color = TextSecondary)
        Text(
            text = if (value.isNaN()) "—" else String.format(java.util.Locale.US, "%.1f", value),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (value.isNaN()) TextSecondary.copy(alpha = 0.6f) else color
        )
    }
}

fun generateShareableCardBitmap(
    context: android.content.Context,
    title: String,
    subtitle: String,
    quote: String,
    streakText: String
): Uri? {
    try {
        val width = 1080
        val height = 1080
        val bitmap = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)

        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#1E293B")
            style = android.graphics.Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        paint.color = android.graphics.Color.parseColor("#F59E0B")
        paint.strokeWidth = 12f
        canvas.drawRect(60f, 60f, (width - 60).toFloat(), (height - 60).toFloat(), paint)

        paint.color = android.graphics.Color.parseColor("#0F172A")
        canvas.drawRect(72f, 72f, (width - 72).toFloat(), (height - 72).toFloat(), paint)

        val textPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#F59E0B")
            textSize = 52f
            isAntiAlias = true
            isFakeBoldText = true
        }
        canvas.drawText(title.uppercase(), 120f, 180f, textPaint)

        textPaint.color = android.graphics.Color.parseColor("#94A3B8")
        textPaint.textSize = 38f
        textPaint.isFakeBoldText = false
        canvas.drawText(streakText, 120f, 250f, textPaint)

        textPaint.color = android.graphics.Color.parseColor("#FFFFFF")
        textPaint.textSize = 56f
        textPaint.isFakeBoldText = true

        val x = 120f
        var y = 440f
        val maxWidth = width - 240f
        val words = quote.split(" ")
        var line = ""
        for (word in words) {
            val testLine = if (line.isEmpty()) word else "$line $word"
            if (textPaint.measureText(testLine) > maxWidth) {
                canvas.drawText(line, x, y, textPaint)
                line = word
                y += 80f
            } else {
                line = testLine
            }
        }
        if (line.isNotEmpty()) {
            canvas.drawText(line, x, y, textPaint)
        }

        textPaint.color = android.graphics.Color.parseColor("#F59E0B")
        textPaint.textSize = 34f
        canvas.drawText("• SOLTAR • soltar.app", 120f, (height - 150).toFloat(), textPaint)

        val cachePath = java.io.File(context.cacheDir, "shared_card.png")
        val stream = java.io.FileOutputStream(cachePath)
        bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        return androidx.core.content.FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", cachePath)
    } catch (e: Exception) {
        return null
    }
}

private fun analyzeRelapsePatternsLocal(
    relapses: List<com.example.data.RelapseEntity>,
    urgeEpisodes: List<com.example.data.UrgeEpisodeEntity>,
    recentCheckins: List<com.example.data.CheckinEntity>
): String {
    val totalRelapses = relapses.size
    val totalUrges = urgeEpisodes.size
    if (totalRelapses == 0) {
        return "No hay registros de recaída todavía. Continúa manteniendo tu contacto cero con firmeza."
    }
    
    val lastRelapse = relapses.firstOrNull()
    val trigger = lastRelapse?.trigger ?: "desconocido"
    val notes = lastRelapse?.whatHappened ?: ""
    
    return """
        📊 DIAGNÓSTICO DE SÍNDROME DE ABSTINENCIA (LOCAL):
        - Total de recaídas registradas: $totalRelapses
        - Total de picos de ansiedad (impulsos contenidos): $totalUrges
        
        🔍 ANÁLISIS DE CAUSA RAÍZ:
        El último desencadenante registrado fue de tipo "$trigger". ${if (notes.isNotBlank()) "Las notas indican: \"$notes\"." else ""}
        Los patrones sugieren que los picos de abstinencia cognitiva ocurren cuando los niveles de estrés son altos o en momentos de inactividad. 
        
        💡 ESTRATEGIA DE PREVENCIÓN:
        1. Alerta Temprana: Activa la esfera EMDR ante el primer indicio de urgencia (antes de que la ansiedad supere el nivel 5).
        2. Compartimentación: Cuando surja el impulso, comprométete a esperar 15 minutos utilizando la caja de herramientas (binaural, diario) antes de tomar cualquier decisión.
    """.trimIndent()
}

private fun analyzeRelapsePatternsLocal(triggerEvents: List<com.example.data.TriggerEventEntity>): String {
    if (triggerEvents.isEmpty()) {
        return "No hay suficientes datos de detonantes registrados para identificar un patrón temporal."
    }
    
    val count = triggerEvents.size
    return """
        🕒 PATRÓN TEMPORAL Y DETONANTES COMUNES:
        Se han registrado $count eventos detonantes en total.
        El análisis de frecuencia indica que la vulnerabilidad aumenta en horas de la tarde y noche, especialmente bajo fatiga cognitiva acumulada o tras exposición a estímulos digitales indirectos (redes sociales).
        
        💡 RECOMENDACIÓN: Refuerza tus rutinas protectoras en el bloque de las 18:00 a las 22:00 horas.
    """.trimIndent()
}

private fun synthesizeRedFlagsPatternLocal(redFlags: List<String>): String {
    if (redFlags.isEmpty()) {
        return "No hay suficientes auditorías para consolidar un patrón estructural de fondo."
    }
    return """
        🛡️ PATRÓN ESTRUCTURAL DETECTADO (COMPENSACIÓN):
        Al consolidar tus auditorías, se identifican comportamientos recurrentes de desvalorización, control o invalidación. 
        Este patrón de fondo confirma que el dolor actual no es por la pérdida de un vínculo sano, sino el síndrome de abstinencia de la intermitencia emocional.
        
        💡 RECORDATORIO SOBERANO: Mantener el Contacto Cero no es un castigo para tu expareja, sino el único escudo para proteger tu reconstrucción y salud mental.
    """.trimIndent()
}



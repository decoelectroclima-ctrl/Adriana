package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.QuickReflectionCard
import com.example.ai.SoltarAiEngine
import com.example.audio.SoltarSoundManager
import com.example.data.SoltarFramework
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import android.os.PowerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

@Composable
fun AiCompanionDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    var selectedTag by remember { mutableStateOf<QuickTag?>(null) }
    var currentCard by remember { mutableStateOf<QuickReflectionCard?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Handle back button: if viewing a card, go back to tag list; otherwise dismiss
    BackHandler {
        if (selectedTag != null) {
            selectedTag = null
            currentCard = null
        } else {
            onDismiss()
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val userContext = remember(settings) { viewModel.buildUserPersonalizationContext() }
    val isLifeCoach = settings?.journeyStage == "LIFE_COACH"

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = SoltarBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            Surface(
                color = SoltarSurface,
                tonalElevation = 4.dp,
                border = BorderStroke(1.dp, SoltarBorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (selectedTag != null) {
                                selectedTag = null
                                currentCard = null
                            } else {
                                onDismiss()
                            }
                        },
                        modifier = Modifier.testTag("ai_dialog_close")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = SoltarAmber
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "LO QUE NECESITAS AHORA",
                            style = MaterialTheme.typography.titleSmall,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "100 % Local • ${uiState.preferredFramework.title}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Placeholder to balance the top bar
                    Box(modifier = Modifier.size(48.dp))
                }
            }
        }
    ) { innerPadding ->
            val card = currentCard
            val tag = selectedTag
            if (card != null && tag != null) {
                QuickReflectionDetailView(
                    card = card,
                    tag = tag,
                    onSaveToJournal = {
                        viewModel.saveQuickReflectionToJournal(card)
                    },
                    onSelectAnother = {
                        selectedTag = null
                        currentCard = null
                    },
                    onAnotherView = {
                        currentCard = SoltarAiEngine.quickReflection(
                            category = card.category,
                            framework = uiState.preferredFramework,
                            userContext = userContext,
                            fallbackAction = tag.group.defaultAction,
                            excludeAtomIds = card.atomIds.toSet(),
                            countAsNewUse = false
                        )
                    },
                    onDismiss = onDismiss,
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            } else {
                // View: Tag Selection Grid / List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
                ) {
                // Info Banner
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Default.Psychology,
                                contentDescription = null,
                                tint = SoltarAmber,
                                modifier = Modifier.size(24.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Elige lo que estás sintiendo ahora",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Recibe un anclaje clínico y filosófico directo, sin esperas ni límites.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                // Search / Filter Input
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                "Buscar situación o emoción...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Borrar búsqueda",
                                        tint = TextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tag_search_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarAmber,
                            unfocusedBorderColor = SoltarBorder,
                            focusedContainerColor = SoltarSurface,
                            unfocusedContainerColor = SoltarSurface,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = SoltarAmber
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Crisis Handling
                val isCrisis = SoltarAiEngine.checkSelfHarmTrigger(searchQuery)
                if (isCrisis) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Si estás pensando en hacerte daño, hay ayuda ahora mismo.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        onDismiss()
                                        viewModel.openNeedHelpSheet()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Text("Ver ayuda inmediata")
                                }
                            }
                        }
                    }
                } else if (searchQuery.isNotBlank()) {
                    // Only show chip if not in crisis and search has results
                } else {
                    item {
                         Surface(
                            onClick = {
                                onDismiss()
                                viewModel.openNeedHelpSheet()
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .testTag("crisis_chip")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Pienso en hacerme daño",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Tag Groups
                val groupOrder = QuickReflectionTags.getGroupOrder(isLifeCoach)
                val allTags = QuickReflectionTags.allTags

                val filteredTags = if (searchQuery.isBlank()) {
                    allTags
                } else {
                    val q = searchQuery.trim().lowercase()
                    allTags.filter { it.label.lowercase().contains(q) || it.group.title.lowercase().contains(q) }
                }

                groupOrder.forEach { group ->
                    val tagsInGroup = filteredTags.filter { it.group == group }
                    if (tagsInGroup.isNotEmpty()) {
                        item(key = group.name) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val groupIcon = when (group) {
                                        QuickGroup.AHORA_MISMO -> Icons.Default.Bolt
                                        QuickGroup.MENTE -> Icons.Default.Psychology
                                        QuickGroup.CUERPO -> Icons.Default.Favorite
                                        QuickGroup.EMOCIONES -> Icons.Default.Mood
                                        QuickGroup.SITUACIONES -> Icons.Default.AccountTree
                                        QuickGroup.AVANCE -> Icons.Default.TrendingUp
                                    }
                                    Icon(
                                        groupIcon,
                                        contentDescription = null,
                                        tint = SoltarAmber,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = group.title.uppercase(),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = "(${tagsInGroup.size})",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }

                                // Flow-like tag layout
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    tagsInGroup.forEach { tag ->
                                        Surface(
                                            onClick = {
                                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                val resolved = SoltarAiEngine.quickReflection(
                                                    tag.category,
                                                    uiState.preferredFramework,
                                                    userContext,
                                                    fallbackAction = tag.group.defaultAction
                                                )
                                                currentCard = resolved
                                                selectedTag = tag
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            color = SoltarSurfaceElevated,
                                            border = BorderStroke(1.dp, SoltarBorder),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("quick_tag_${tag.category.name}")
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = tag.label,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = TextPrimary,
                                                    fontWeight = FontWeight.Medium,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Icon(
                                                    Icons.Default.ChevronRight,
                                                    contentDescription = null,
                                                    tint = SoltarAmber,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickReflectionDetailView(
    card: QuickReflectionCard,
    tag: QuickTag,
    onSaveToJournal: () -> Unit,
    onSelectAnother: () -> Unit,
    onAnotherView: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: SoltarViewModel,
    modifier: Modifier = Modifier
) {
    var isSaved by remember { mutableStateOf(false) }
    var isRewriting by remember { mutableStateOf(false) }
    var rewriteFailed by remember { mutableStateOf(false) }
    var originalCard by remember { mutableStateOf<com.example.ai.QuickReflectionCard?>(null) }
    var displayedCard by remember(card) { mutableStateOf(card) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Tag Header Badge
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SoltarAmber.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.35f))
                ) {
                    Text(
                        text = tag.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 11.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SoltarSurfaceElevated,
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Text(
                        text = card.framework.title,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Main Reflection Body Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (displayedCard.headline.isNotBlank()) {
                        Text(
                            text = displayedCard.headline,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Psychology,
                            contentDescription = null,
                            tint = SoltarAmber,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "ANCLAJE REFLEXIVO",
                            style = MaterialTheme.typography.labelSmall,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Text(
                        text = formatMarkdownToAnnotatedString(displayedCard.body),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary,
                        lineHeight = 24.sp
                    )
                }
            }
        }

        // Quote / Principle Box (if present)
        if (displayedCard.quoteOrSource.isNotBlank()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SoltarSurface,
                    border = BorderStroke(1.dp, SoltarBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "«${displayedCard.quoteOrSource}»",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SoltarAmber,
                            fontStyle = FontStyle.Italic,
                            lineHeight = 20.sp
                        )
                        if (displayedCard.author.isNotBlank()) {
                            Text(
                                text = "— ${displayedCard.author}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }

        // Concrete Action Box (if present)
        if (displayedCard.concreteAction.isNotBlank()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SoltarSage.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, SoltarSage.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircleOutline,
                            contentDescription = null,
                            tint = SoltarSage,
                            modifier = Modifier.size(18.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "MICRO-ACCIÓN CONCRETA",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarSage,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = displayedCard.concreteAction,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }

        // Action Buttons
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // New Buttons
                Button(
                    onClick = onAnotherView,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("another_view_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarBorderSubtle)
                ) {
                    Text("Otra mirada", color = TextPrimary)
                }
                
                if (tag.group == QuickGroup.AHORA_MISMO || tag.group == QuickGroup.CUERPO) {
                    Button(
                        onClick = {
                            onDismiss()
                            viewModel.openNoThinkingSheet()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Respirar")
                    }
                }

                if (tag.group == QuickGroup.AHORA_MISMO) {
                    Button(
                        onClick = {
                            onDismiss()
                            viewModel.openUrgeMode()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Modo Impulso")
                    }
                }

                Button(
                    onClick = {
                        onSaveToJournal()
                        isSaved = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_reflection_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaved) SoltarSage else SoltarAmber
                    )
                ) {
                    Icon(
                        if (isSaved) Icons.Default.Check else Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = SoltarBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSaved) "Guardada en tu diario" else "Guardar en mi diario",
                        color = SoltarBackground,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onSelectAnother,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("select_another_tag_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Elegir otra situación",
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

private fun formatMarkdownToAnnotatedString(text: String): AnnotatedString {
    return buildAnnotatedString {
        val cleaned = text.replace("### ", "").replace("## ", "").replace("# ", "")
        val parts = cleaned.split("**")
        for (i in parts.indices) {
            if (i % 2 == 1 && i < parts.size) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(parts[i])
                }
            } else {
                val subParts = parts[i].split("*")
                for (j in subParts.indices) {
                    if (j % 2 == 1 && j < subParts.size) {
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(subParts[j])
                        }
                    } else {
                        append(subParts[j])
                    }
                }
            }
        }
    }
}

package com.example.ai

import org.json.JSONObject

data class ReadingPill(
    val id: String,
    val author: String,
    val category: String, // Estoicismo | Neurociencia | Psicoanálisis | Salmos
    val textBody: String
)

data class ReadingChapter(
    val id: String,
    val title: String,
    val author: String,
    val estimatedReadTimeMin: Int,
    val category: String,
    val textBody: String,
    val isCoreOnly: Boolean = false
)

data class ClosingRitualStepAi(
    val phaseName: String,
    val title: String,
    val guidance: String,
    val reflectionPrompt: String
)

@kotlinx.serialization.Serializable
data class RitualQuestion(
    val questionNumber: Int,
    val questionText: String,
    val emotionalCore: String,
    val category: String = "",
    val isInterviewComplete: Boolean = false,
    val questionType: String = "TEXTO"
)

@kotlinx.serialization.Serializable
data class RitualQAPair(
    val questionText: String,
    val answerText: String
)

@kotlinx.serialization.Serializable
data class IdentityGoalSuggestion(
    val actionTitle: String,
    val whoIWantToBe: String,
    val area: String
)

package com.example.ui.managers

import com.example.data.CheckinEntity
import com.example.data.RelapseEntity
import com.example.data.SoltarSettingsEntity

object JourneyStageEvaluator {
    data class EvaluationResult(
        val shouldUpgradeToLifeCoach: Boolean,
        val shouldPromptRecoveryRegression: Boolean,
        val transitionMessage: String?,
        val wasBlockedByQualitativeCheck: Boolean = false
    )

    enum class QualitativeSignal { ESTABILIDAD_GENUINA, NEGACION_O_EVITACION, RESIGNACION_FORZADA, NO_EVALUADO }

    private fun assessQualitativeState(recentFreeText: List<String>): QualitativeSignal {
        val joined = recentFreeText.filter { it.isNotBlank() }.takeLast(5).joinToString("\n---\n")
        // Sin motor de IA no hay forma fiable de clasificar el estado cualitativo por texto libre.
        // NO_EVALUADO no bloquea el avance de fase (solo bloquean NEGACION_O_EVITACION y RESIGNACION_FORZADA),
        // así que este es el valor seguro por defecto: mejor no evaluar que evaluar mal.
        return QualitativeSignal.NO_EVALUADO
    }

    suspend fun evaluate(
        settings: SoltarSettingsEntity?,
        checkins: List<CheckinEntity>,
        relapses: List<RelapseEntity>,
        hasCompletedClosingRitual: Boolean,
        recentFreeText: List<String> = emptyList()
    ): EvaluationResult {
        if (settings == null) return EvaluationResult(false, false, null)
        val currentStage = settings.journeyStage
        val now = System.currentTimeMillis()

        if (currentStage == "RECOVERY") {
            val recentCheckins = checkins.take(14)
            val hasEnoughHistory = recentCheckins.size >= 5
            val lowPainSustained = recentCheckins.isNotEmpty() &&
                (recentCheckins.map { it.pain + it.anxiety + it.rumination }.average() < 12.0)
            val goodAutonomy = recentCheckins.isNotEmpty() &&
                (recentCheckins.map { it.autonomy }.average() >= 5.0)
            val closingRitualDone = hasCompletedClosingRitual
            val recentRetrogradeRelapse = relapses.any { r ->
                (now - r.timestamp) < (28L * 24 * 3600 * 1000) && (r.interpretation == "retroceso" || r.isRestartingFromZero)
            }
            val minimumTimeElapsed = (now - settings.breakupDateTimestamp) > (60L * 24 * 3600 * 1000)
            val realDataSupportsHealing = hasEnoughHistory && lowPainSustained && goodAutonomy
            val quantitativeGreenLight = closingRitualDone && !recentRetrogradeRelapse && minimumTimeElapsed && realDataSupportsHealing

            if (quantitativeGreenLight) {
                if (recentFreeText.any { it.isNotBlank() }) {
                    val qualitative = assessQualitativeState(recentFreeText)
                    if (qualitative == QualitativeSignal.NEGACION_O_EVITACION || qualitative == QualitativeSignal.RESIGNACION_FORZADA) {
                        return EvaluationResult(
                            shouldUpgradeToLifeCoach = false,
                            shouldPromptRecoveryRegression = false,
                            transitionMessage = "Tus números muestran avance, pero tu propio proceso escrito sugiere que aún hay algo pendiente de mirar de frente. Sigamos un poco más en Recovery antes de dar el salto.",
                            wasBlockedByQualitativeCheck = true
                        )
                    }
                }
                return EvaluationResult(
                    shouldUpgradeToLifeCoach = true,
                    shouldPromptRecoveryRegression = false,
                    transitionMessage = "Has recorrido un largo camino. Ahora podemos trabajar en quién quieres ser."
                )
            }
        } else if (currentStage == "LIFE_COACH") {
            val severeRecentRelapse = relapses.any { r ->
                (now - r.timestamp) < (7L * 24 * 3600 * 1000) && (r.interpretation == "retroceso" || r.isRestartingFromZero)
            }
            if (severeRecentRelapse) {
                return EvaluationResult(false, true, null)
            }
        }
        return EvaluationResult(false, false, null)
    }
}

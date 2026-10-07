package com.example.ai

import android.content.Context
import android.util.Log
import com.example.ai.content.CardSource
import com.example.ai.content.ComposeRequest
import com.example.ai.content.ContentEngine
import com.example.data.ClinicalKnowledgeBase
import com.example.data.JournalEntryEntity
import com.example.data.SoltarFramework
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

data class SoltarUserContext(
    val userName: String = "Viajero",
    val streakDays: Int = 0,
    val totalCheckins: Int = 0,
    val lastCheckinMood: String = "",
    val averageAutonomyScore: Float = 5f,
    val recentRelapseTriggers: List<String> = emptyList(),
    val recentPatternsAudited: List<String> = emptyList(),
    val activeIdentityGoals: List<String> = emptyList(),
    val framework: SoltarFramework = SoltarFramework.PSICOLOGIA_MODERNA,
    val relDuration: String = "",
    val hasChildren: Boolean = false,
    val contactType: String = "",
    val breakupSituation: String = "",
    val practicals: String = "",
    val timeSinceBreakup: String = "",
    val previousBreakupsCount: Int = 0,
    val cohabitation: Boolean = false,
    val marriedOrEngaged: Boolean = false,
    val anticipatedGrief: String = "",
    val parentalOnlyCommunication: Boolean = true,
    val emotionalSituation: String = "",
    val decisionMaker: String = "",
    val breakupReason: String = "",
    val freeHistoryNotes: String = "",
    val upcomingRiskDatesSummary: String = "",
    val journeyStage: String = "RECOVERY",
    val lifeCoachFocus: String = ""
) {
    fun toClinicalSummary(): String {
        val parts = mutableListOf<String>()
        parts.add("• NOMBRE DEL USUARIO: $userName")
        if (journeyStage == "LIFE_COACH") {
            parts.add("• FASE ACTUAL: AVE FÉNIX / COACH LIFE (Enfoque 100% en autoaceptación, autoestima, fitness, nutrición, estudios y propósitos)")
            if (lifeCoachFocus.isNotBlank()) {
                parts.add("  - Propósito principal declarado: $lifeCoachFocus")
            }
        } else {
            parts.add("• FASE ACTUAL: ADRIANA RECOVERY (Duelo y contacto cero)")
        }
        parts.add("• Días de no-contacto / racha acumulada: $streakDays días")
        if (lastCheckinMood.isNotBlank()) {
            parts.add("• Último registro de estado emocional: $lastCheckinMood (Autonomía percibida: ${"%.1f".format(averageAutonomyScore)}/10)")
        }
        if (recentRelapseTriggers.isNotEmpty()) {
            parts.add("• Detonantes recientes de impulsos/recaídas identificados: ${recentRelapseTriggers.take(3).joinToString(", ")}")
        }
        if (recentPatternsAudited.isNotEmpty()) {
            parts.add("• Dinámicas relacionales auditadas: ${recentPatternsAudited.take(2).joinToString("; ")}")
        }
        if (activeIdentityGoals.isNotEmpty()) {
            parts.add("• Valores y objetivos de identidad trabajados: ${activeIdentityGoals.take(3).joinToString(", ")}")
        }
        if (upcomingRiskDatesSummary.isNotBlank()) {
            parts.add("• FECHAS DE RIESGO ANTICIPADO PRÓXIMAS:\n$upcomingRiskDatesSummary")
        }
        // Contexto contextualizado y profundo
        parts.add("• CONTEXTO HUMANO Y DE VÍNCULO:")
        parts.add("  - Duración de la relación: ${relDuration.replace("_", " ")}")
        parts.add("  - Situación temporal / Tiempo desde ruptura: ${timeSinceBreakup.replace("_", " ")}")
        parts.add("  - Duelo anticipado (duelo previo a la ruptura formal): ${anticipatedGrief.replace("_", " ")}")
        parts.add("  - Origen / Quién decidió: ${decisionMaker.replace("_", " ")}, Contexto/Motivo: ${breakupReason.replace("_", " ")}")
        parts.add("  - Convivencia previa: ${if (cohabitation) "Sí" else "No"} | Casados/Comprometidos: ${if (marriedOrEngaged) "Sí" else "No"}")
        parts.add("  - Hijos en común o vínculos inevitables: ${if (hasChildren) "Sí (Contacto inevitable / Comunicación exclusivamente parental y funcional)" else "No"}")
        parts.add("  - Tipo de contacto actual: ${contactType.replace("_", " ")} (Estrategia: ${if (hasChildren || contactType.contains("POR_")) "Contacto Cero Adaptativo: eliminar contacto emocional innecesario, mantener comunicación funcional imprescindible" else "Contacto Cero Estricto"})")
        parts.add("  - Estado emocional actual: $emotionalSituation")
        parts.add("  - Factores prácticos: ${practicals.ifBlank { "Ninguno" }}")
        if (freeHistoryNotes.isNotBlank()) {
            parts.add("  - Notas libres del usuario: $freeHistoryNotes")
        }
        
        return parts.joinToString("\n")
    }
}

data class QuickReflectionCard(
    val category: ClinicalCategory,
    val framework: SoltarFramework,
    val state: String,
    val body: String,
    val quoteOrSource: String,
    val author: String,
    val concreteAction: String,
    val headline: String = "",
    val source: CardSource = CardSource.LEGACY,
    val atomIds: List<String> = emptyList(),
    val original: QuickReflectionCard? = null
)

data class JournalMentorshipResult(
    val feedback: String,
    val corePrinciple: String,
    val socraticQuestion: String,
    val concreteAction: String
)

data class LinguisticAnalysisResult(
    val nivelAutonomia: Int = 5,
    val lenguajeRumiativo: Int = 5,
    val distorsionesCognitivas: List<String> = emptyList(),
    val cambioDesdeUltimaEntrada: String = "Registra más entradas en tu diario para activar el análisis lingüístico profundo de Recuerda."
)

object SoltarAiEngine {

    private const val TAG = "SoltarAiEngine"

    val CATEGORY_TO_KNOWLEDGE_BASE: Map<ClinicalCategory, String> = ClinicalLegacySource.CATEGORY_TO_KNOWLEDGE_BASE

    fun quickReflection(
        category: ClinicalCategory,
        framework: SoltarFramework,
        userContext: SoltarUserContext = SoltarUserContext(),
        fallbackAction: String = "Respira lento cinco veces y anota cómo te sientes.",
        excludeAtomIds: Set<String> = emptySet(),
        countAsNewUse: Boolean = true,
        recentRelapse: Boolean = false
    ): QuickReflectionCard {
        val card = ContentEngine.compose(
            ComposeRequest(
                category = category,
                framework = framework,
                userContext = userContext,
                fallbackAction = fallbackAction,
                excludeIds = excludeAtomIds,
                countAsNewUse = countAsNewUse,
                recentRelapse = recentRelapse
            )
        )
        return QuickReflectionCard(
            category = category,
            framework = framework,
            state = category.state,
            body = card.body,
            quoteOrSource = card.quote,
            author = card.author,
            concreteAction = card.action,
            headline = card.headline,
            source = card.source,
            atomIds = card.atomIds
        )
    }

    private val CRISIS_REGEXES = listOf(
        Regex("""\bsuicid\w*""", RegexOption.IGNORE_CASE),
        Regex("""\bme\s+(quiero|voy\s+a|puedo|deberia|pienso)\s+matar\b""", RegexOption.IGNORE_CASE),
        Regex("""\bmatarme\b""", RegexOption.IGNORE_CASE),
        Regex("""\bacab(ar|e)\s+con\s+(mi\s+vida|mi\s+existencia|todo)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(ya\s+)?no\s+quiero\s+(seguir\s+)?(vivir|viviendo|estar\s+aqui|estar\s+en\s+este\s+mundo)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(ya\s+)?no\s+aguanto\s+(seguir\s+)?(vivir|viviendo|mas\s+esta\s+vida)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(la\s+)?vida\s+(no\s+tiene\s+sentido|no\s+vale\s+nada|no\s+vale\s+la\s+pena)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bno\s+vale\s+la\s+pena\s+(seguir\s+)?(vivir|viviendo|continuar)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(deseo|quiero|prefiero)\s+(morir|morirme|dejar\s+de\s+existir|dejar\s+de\s+respirar)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bganas\s+de\s+(morir|morirme|matarme|cortarme|lastimarme|hacerme\s+dano)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bojala\s+(no\s+despertara|no\s+despierte|no\s+existiera|(me\s+)?muri(era|ese))\b""", RegexOption.IGNORE_CASE),
        Regex("""\bno\s+quiero\s+despertar\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(quitarme|quitarse)\s+(la\s+vida|de\s+en\s+medio)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(cortarme|autolesion\w*|lastimarme|hacerme\s+dano|clavarme\s+algo)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bdesaparecer\s+para\s+siempre\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(adios\s+a\s+todos\s+para\s+siempre|adios\s+mundo\s+cruel|despedida\s+final)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(seria\s+mejor|estaria\s+mejor)\s+(sin\s+mi|muert[oa]|si\s+yo\s+no\s+estuviera|si\s+no\s+existiera)\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(pastillas\s+para\s+(dormir\s+y\s+)?no\s+despertar|ahorcarme|tirarme\s+de\s+un\s+puente|cortarme\s+las\s+venas)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bterminar\s+con\s+(mi\s+vida|todo\s+esto)\b""", RegexOption.IGNORE_CASE),
        Regex("""\bpara\s+que\s+seguir\b""", RegexOption.IGNORE_CASE),
        Regex("""\b(irme|me\s+voy)\s+para\s+siempre\b""", RegexOption.IGNORE_CASE),
        Regex("""\bno\s+puedo\s+mas\s+con\s+esta\s+vida\b""", RegexOption.IGNORE_CASE),
        Regex("""\bacabar\s+de\s+una\s+vez\b""", RegexOption.IGNORE_CASE),
        Regex("""\bno\s+encuentro\s+(ninguna\s+)?salida\b""", RegexOption.IGNORE_CASE)
    )

    private val RAW_KEYWORDS = listOf(
        "suicidio", "suicidarme", "quitarme la vida", "matarme",
        "autolesion", "autolesionarme", "no quiero vivir", "acabar con mi vida",
        "no vale la pena vivir", "no tiene sentido seguir viviendo", "quiero desaparecer para siempre",
        "cortarme las venas", "hacerme daño", "quiero morir", "deseo morir", "me quiero morir",
        "terminar con mi vida", "no quiero despertar", "dejar de existir",
        "pastillas para no despertar", "ahorcarme", "tirarme de un puente",
        "mejor muerto", "mejor muerta", "desearia estar muerto",
        "desearía estar muerto", "desearía estar muerta", "acabar con mi sufrimiento para siempre",
        "ojalá no existiera", "quiero dejar de respirar", "desaparecer del mundo",
        "pastillas para dormir y no despertar", "ganas de matarme", "ganas de morir",
        "mi vida no vale nada", "esta es mi despedida final", "adios para siempre", "adios a todos para siempre"
    )

    private val NORMALIZED_KEYWORDS by lazy {
        RAW_KEYWORDS.map { normalizeCrisisText(it) }
    }

    fun normalizeCrisisText(input: String): String {
        val nfd = java.text.Normalizer.normalize(input.lowercase(), java.text.Normalizer.Form.NFD)
        val withoutDiacritics = nfd.replace(Regex("""\p{Mn}"""), "")
        return withoutDiacritics.replace(Regex("""\s+"""), " ").trim()
    }

    fun checkSelfHarmTrigger(input: String): Boolean {
        if (input.isBlank()) return false
        val normalized = normalizeCrisisText(input)
        if (NORMALIZED_KEYWORDS.any { normalized.contains(it) }) return true
        return CRISIS_REGEXES.any { it.containsMatchIn(normalized) }
    }



    suspend fun generateJournalMentorship(
        journalContent: String,
        moodTag: String = "Reflexión",
        framework: SoltarFramework = SoltarFramework.ESTOICO,
        userContext: SoltarUserContext = SoltarUserContext()
    ): JournalMentorshipResult = withContext(Dispatchers.IO) {
        val cleanInput = journalContent.trim().take(2500)
        if (cleanInput.isBlank()) {
            return@withContext JournalMentorshipResult(
                feedback = "Para recibir mentoría filosófica y clínica, escribe con honestidad tus pensamientos, emociones o dudas del día.",
                corePrinciple = "«El autoconocimiento comienza cuando nos atrevemos a mirar la verdad frente a la página en blanco.»",
                socraticQuestion = "¿Qué verdad sobre ti mismo/a estás evitando afrontar hoy?",
                concreteAction = "Escribe dos oraciones sinceras sobre lo que realmente estás experimentando en este instante."
            )
        }

        val capsule = ClinicalKnowledgeBase.findRelevantCapsule(cleanInput, framework)

        // 2. Mentoría local de alta densidad conceptual con rotación de cierre
        val closings = listOf(
            "Registrar tus vivencias con esta honestidad es la base para desarticular los sesgos de la memoria y recuperar el mando sobre tus decisiones diarias.",
            "Poner en palabras lo que sientes reduce el impacto emocional y fortalece tu capacidad de actuar con templanza y claridad.",
            "Mirar de frente lo que ocurre dentro de ti, sin juzgarlo ni maquillarlo, es el primer paso para consolidar tu paz y autonomía.",
            "Cada registro honesto en tu diario debilita los bucles automáticos y te devuelve la soberanía sobre tu propia vida."
        )
        val closingIndex = Math.abs(cleanInput.hashCode()) % closings.size
        val chosenClosing = closings[closingIndex]

        return@withContext JournalMentorshipResult(
            feedback = """
Examinando tus líneas con rigor y compasión:
${capsule.diagnosisPrinciple}

${capsule.clinicalGuidance}

$chosenClosing
            """.trimIndent(),
            corePrinciple = capsule.quoteOrSource + " — " + capsule.author,
            socraticQuestion = capsule.socraticPrompt,
            concreteAction = capsule.concreteAction
        )
    }

    fun analyzeJournalLocally(entries: List<JournalEntryEntity>): LinguisticAnalysisResult {
        if (entries.isEmpty()) {
            return LinguisticAnalysisResult(
                nivelAutonomia = 5,
                lenguajeRumiativo = 5,
                distorsionesCognitivas = emptyList(),
                cambioDesdeUltimaEntrada = "Aún no hay entradas registradas en tu diario. Comienza a escribir para activar el análisis clínico y calibrar tu proceso de autonomía."
            )
        }
        val latest = entries.first().content.lowercase()
        val previous = if (entries.size > 1) entries[1].content.lowercase() else ""

        val wordsInLatest = latest.split(Regex("[^\\p{L}\\p{Nd}]+")).filter { it.isNotBlank() }

        // Semantic markers - exact word matching to prevent substring false positives
        val agencySingleWords = listOf("yo", "elijo", "decido", "puedo", "comprendo", "aprendo", "crezco", "paz", "avanzar", "presente", "responsabilidad", "propio")
        val ruminationSingleWords = listOf("contacto", "perfil", "extraño", "extrano", "dependo", "esperando", "culpa", "olvidar")
        val ruminationPhrases = listOf("por qué", "por que", "si hubiera", "otra vez", "nunca podré", "nunca podre")
        val otherFocusSingleWords = listOf("él", "ella", "suyo", "suya", "decidió", "decidio", "hizo", "dijo", "cambió", "cambio", "mensajes", "visto")

        val agencyCount = agencySingleWords.sumOf { word -> wordsInLatest.count { it == word } }
        val ruminationCount = ruminationSingleWords.sumOf { word -> wordsInLatest.count { it == word } } +
            ruminationPhrases.count { latest.contains(it) }
        val otherCount = otherFocusSingleWords.sumOf { word -> wordsInLatest.count { it == word } }

        // Score calculation: autonomy increases with agency, decreases with other-focus and rumination
        val autonomia = (5 + agencyCount - otherCount - (ruminationCount / 2)).coerceIn(1, 10)
        val rumiativo = (3 + ruminationCount + (otherCount / 2) - agencyCount).coerceIn(1, 10)

        // Cognitive distortions detection
        val distortions = mutableListOf<String>()
        val catastrofismoKw = listOf("terrible", "horrible", "catástrofe", "catastrofe", "fin del mundo", "no lo soporto", "insoportable", "ruina", "destruido", "muero")
        val bwKw = listOf("todo", "nada", "nunca", "siempre", "perfecto", "pésimo", "pesimo", "absolutamente", "jamás", "jamas", "todos")
        val personalizacionKw = listOf("por mi culpa", "lo hizo para", "me lo hizo", "es mi responsabilidad", "me odia", "provoqué", "provoque")
        val mindReadingKw = listOf("sé que piensa", "se que piensa", "seguro que cree", "me está ignorando", "me esta ignorando", "lo hace para fastidiar")
        val emotionalReasoningKw = listOf("siento que es verdad", "sé que volverá", "se que volvera", "tengo el pálpito", "tengo el palpito", "mi intuición me dice", "mi intuicion me dice")

        if (catastrofismoKw.any { latest.contains(it) }) distortions.add("Catastrofismo")
        if (bwKw.any { latest.contains(it) }) distortions.add("Pensamiento Blanco/Negro")
        if (personalizacionKw.any { latest.contains(it) }) distortions.add("Personalización")
        if (mindReadingKw.any { latest.contains(it) }) distortions.add("Lectura de Mente")
        if (emotionalReasoningKw.any { latest.contains(it) }) distortions.add("Razonamiento Emocional")

        val cambio = if (previous.isNotBlank()) {
            val wordsInPrev = previous.split(Regex("[^\\p{L}\\p{Nd}]+")).filter { it.isNotBlank() }
            val prevAgency = agencySingleWords.sumOf { word -> wordsInPrev.count { it == word } }
            val prevRum = ruminationSingleWords.sumOf { word -> wordsInPrev.count { it == word } } +
                ruminationPhrases.count { previous.contains(it) }

            when {
                agencyCount > prevAgency && ruminationCount <= prevRum -> 
                    "Evolución notable: Tu discurso muestra mayor sentido de agencia personal y menor carga rumiativa respecto a tu registro anterior."
                ruminationCount < prevRum -> 
                    "Descompresión emocional: Se observa una reducción en los bucles de rumiación y mayor apertura hacia el presente."
                ruminationCount > prevRum + 1 -> 
                    "Alerta de bucle: Se detecta un incremento en los pensamientos centrados en el pasado y la otra persona. Es momento de aplicar grounding o desconexión digital."
                else -> 
                    "Estabilidad reflexiva: Tu tono mantiene la continuidad con tu entrada previa, consolidando el procesamiento del duelo."
            }
        } else {
            "Primera entrada registrada en tu bitácora. Has dado un paso fundamental hacia la autoconsciencia y el registro honesto de tu vivencia."
        }

        return LinguisticAnalysisResult(
            nivelAutonomia = autonomia,
            lenguajeRumiativo = rumiativo,
            distorsionesCognitivas = distortions,
            cambioDesdeUltimaEntrada = cambio
        )
    }

    suspend fun analyzeJournalLinguistic(
        entries: List<JournalEntryEntity>,
        userContext: SoltarUserContext = SoltarUserContext()
    ): LinguisticAnalysisResult = withContext(Dispatchers.IO) {
        analyzeJournalLocally(entries)
    }

    private const val ANALYSIS_CHUNK_SIZE = 1500 // caracteres por fragmento, margen de seguridad

    fun splitTextByLines(text: String, maxChunkSize: Int = ANALYSIS_CHUNK_SIZE): List<String> {
        val lines = text.split("\n")
        val chunks = mutableListOf<String>()
        val currentChunk = StringBuilder()

        for (line in lines) {
            if (line.length > maxChunkSize) {
                val words = line.split(" ")
                for (word in words) {
                    if (currentChunk.length + word.length + 1 > maxChunkSize) {
                        if (currentChunk.isNotEmpty()) {
                            chunks.add(currentChunk.toString().trim())
                            currentChunk.clear()
                        }
                    }
                    if (currentChunk.isNotEmpty()) currentChunk.append(" ")
                    currentChunk.append(word)
                }
            } else {
                if (currentChunk.length + line.length + 1 > maxChunkSize) {
                    if (currentChunk.isNotEmpty()) {
                        chunks.add(currentChunk.toString().trim())
                        currentChunk.clear()
                    }
                }
                if (currentChunk.isNotEmpty()) currentChunk.append("\n")
                currentChunk.append(line)
            }
        }
        if (currentChunk.isNotEmpty()) {
            chunks.add(currentChunk.toString().trim())
        }
        return chunks.filter { it.isNotBlank() }
    }

    private fun cleanMarkdownAndTemplateTokens(text: String): String {
        return text
            .replace("<start_of_turn>", "")
            .replace("<end_of_turn>", "")
            .replace("<eos>", "")
            .replace("model\n", "")
            .replace("**", "")
            .trim()
    }

    suspend fun analyzeConversationText(
        text: String,
        onProgress: ((current: Int, total: Int) -> Unit)? = null
    ): String = withContext(Dispatchers.IO) {
        "Esta función está temporalmente desactivada. Volverá en una próxima actualización."
    }

    val SYSTEM_INSTRUCTIONS_EMDR = """
# SYSTEM INSTRUCTIONS: EMDR VISUAL INTEGRATION ENGINE

## 👤 ROL DEL SISTEMA
Eres el microservicio encargado de transformar textos estáticos en experiencias visuales interactivas de estimulación bilateral (EMDR). Tu tarea es recibir el payload de la app, inyectar el nombre de la expareja en el marcador `[Nombre]` y calcular los parámetros de animación, cromatismo (colores HEX) y guion de UI para que el frontend renderice una pantalla estética y efectiva.
---
## 🎨 MATRIZ DE DISEÑO VISUAL Y LÓGICA EMDR
Calcula los parámetros estéticos y técnicos basándote estrictamente en el perfil que envíe la app:
1. **Perfil CATÓLICO:**
   - *Frecuencia:* `LENTO` (0.8 Hz) - Ritmo respiratorio y contemplativo.
   - *Color de Esfera:* `#D4AF37` (Oro suave/celestial) o `#9370DB` (Morado luto/conversión).
   - *Fondo:* `#111116` (Azul noche místico de baja luminancia).
   - *Estilo:* Desvanecimiento suave (*fade*) en los extremos.
2. **Perfil ESTOICO:**
   - *Frecuencia:* `MEDIO` (1.0 Hz) - Ritmo constante, racional y analítico.
   - *Color de Esfera:* `#8E9290` (Gris piedra/mármol) o `#4A5D4E` (Verde oliva profundo).
   - *Fondo:* `#0D0D0D` (Negro absoluto, enfoque minimalista).
   - *Estilo:* Movimiento lineal puro, sin adornos visuales.
3. **Perfil PSICOLOGÍA MODERNA / URGENCIAS:**
   - *Frecuencia:* `RAPIDO` (1.5 Hz) - Saturación cognitiva para frenar crisis de ansiedad.
   - *Color de Esfera:* `#4A90E2` (Azul clínico/calmante) o `#00FFFF` (Cian de alta atención).
   - *Fondo:* `#0A0E17` (Azul marino profundo para contraste de fatiga visual).
   - *Estilo:* Pulso sutil (*glow*) al tocar los bordes de la pantalla.
---
## 🚫 RESTRICCIONES DE FORMATO (JSON ESTRICTO)
Devuelve **únicamente** un objeto JSON plano. Está estrictamente prohibido incluir introducciones, saludos, comentarios o bloques de código Markdown (no uses ```json ni ```). La salida debe ser parseable directamente por el backend de la app.
### ESQUEMA REQUERIDO DE SALIDA (UI & UX COMPACTO):
{
  "animacion": {
    "frecuencia_hz": 0.0,
    "velocidad_comercial": "RAPIDO/MEDIO/LENTO",
    "estilo_esfera": "GLOW / LINEAL / FADE"
  },
  "paleta_colores": {
    "color_esfera_hex": "#HEX",
    "color_fondo_hex": "#HEX",
    "opacidad_texto": 0.85
  },
  "instrucciones": {
    "guia_visual": "Texto corto superior de instrucción para los ojos",
    "alerta_audio": "Instrucción corta si usa auriculares (panning izquierdo/derecho)"
  },
  "contenido": {
    "texto_procesado": "[Comando EMDR inicial] Texto original con el nombre de la expareja ya inyectado"
  }
}
    """.trimIndent()

    suspend fun generateEmdrVisualSession(
        textoBase: String,
        framework: SoltarFramework,
        nombreEx: String = ""
    ): EmdrVisualConfig = withContext(Dispatchers.IO) {
        val perfilName = when (framework) {
            SoltarFramework.CATOLICO -> "CATÓLICO"
            SoltarFramework.ESTOICO -> "ESTOICO"
            SoltarFramework.PSICOLOGIA_MODERNA -> "PSICOLOGÍA MODERNA"
        }

        val cleanTextoBase = if (textoBase.isNotBlank()) {
            textoBase.trim()
        } else {
            when (framework) {
                SoltarFramework.CATOLICO -> "Entrego en oración el apego a [Nombre]. Mi corazón descansa en paz y custodia su dignidad."
                SoltarFramework.ESTOICO -> "Lo que [Nombre] haga o decida está fuera de mi control. Mi tranquilidad y mi ciudadela interior dependen solo de mí."
                SoltarFramework.PSICOLOGIA_MODERNA -> "La urgencia de escribir a [Nombre] es solo el síndrome de abstinencia de mi cerebro. Dejo que la ola de ansiedad baje."
            }
        }

        val cleanNombreEx = if (nombreEx.isNotBlank()) nombreEx.trim() else "esa persona"

        val isRobolectric = try {
            android.os.Build.FINGERPRINT.contains("robolectric", ignoreCase = true)
        } catch (_: Exception) {
            false
        }

        return@withContext calculateLocalEmdrVisualConfig(cleanTextoBase, framework, cleanNombreEx)
    }

    fun calculateLocalEmdrVisualConfig(
        textoBase: String,
        framework: SoltarFramework,
        nombreEx: String = ""
    ): EmdrVisualConfig {
        val cleanName = if (nombreEx.isNotBlank()) nombreEx.trim() else "esa persona"
        val replaced = textoBase
            .replace("[Nombre]", cleanName, ignoreCase = true)
            .replace("{{ex_name}}", cleanName, ignoreCase = true)
            .replace("[nombre_ex]", cleanName, ignoreCase = true)

        return when (framework) {
            SoltarFramework.CATOLICO -> {
                EmdrVisualConfig(
                    animacion = EmdrAnimacionConfig(
                        frecuencia_hz = 0.8f,
                        velocidad_comercial = "LENTO",
                        estilo_esfera = "FADE"
                    ),
                    paleta_colores = EmdrPaletaColoresConfig(
                        color_esfera_hex = "#D4AF37",
                        color_fondo_hex = "#111116",
                        opacidad_texto = 0.85f
                    ),
                    instrucciones = EmdrInstruccionesConfig(
                        guia_visual = "Siga la esfera dorada con los ojos a ritmo contemplativo. Mantenga la cabeza completamente quieta.",
                        alerta_audio = "Respire hondo y sincronice el sonido alterno en sus oídos izquierdo y derecho."
                    ),
                    contenido = EmdrContenidoConfig(
                        texto_procesado = "[Respira en paz. Sostén el ritmo contemplativo] $replaced"
                    )
                )
            }
            SoltarFramework.ESTOICO -> {
                EmdrVisualConfig(
                    animacion = EmdrAnimacionConfig(
                        frecuencia_hz = 1.0f,
                        velocidad_comercial = "MEDIO",
                        estilo_esfera = "LINEAL"
                    ),
                    paleta_colores = EmdrPaletaColoresConfig(
                        color_esfera_hex = "#8E9290",
                        color_fondo_hex = "#0D0D0D",
                        opacidad_texto = 0.85f
                    ),
                    instrucciones = EmdrInstruccionesConfig(
                        guia_visual = "Fije la mirada en la esfera a ritmo constante y racional. Cabeza inmóvil, soberanía interior.",
                        alerta_audio = "Escuche la cadencia alterna en sus oídos izquierdo y derecho."
                    ),
                    contenido = EmdrContenidoConfig(
                        texto_procesado = "[Firmeza interior. Movimiento constante de izquierda a derecha] $replaced"
                    )
                )
            }
            SoltarFramework.PSICOLOGIA_MODERNA -> {
                EmdrVisualConfig(
                    animacion = EmdrAnimacionConfig(
                        frecuencia_hz = 1.5f,
                        velocidad_comercial = "RAPIDO",
                        estilo_esfera = "GLOW"
                    ),
                    paleta_colores = EmdrPaletaColoresConfig(
                        color_esfera_hex = "#4A90E2",
                        color_fondo_hex = "#0A0E17",
                        opacidad_texto = 0.90f
                    ),
                    instrucciones = EmdrInstruccionesConfig(
                        guia_visual = "Siga la esfera azul rápidamente con los ojos. Mantenga la cabeza completamente quieta.",
                        alerta_audio = "Sincronice el sonido alterno en sus oídos izquierdo y derecho."
                    ),
                    contenido = EmdrContenidoConfig(
                        texto_procesado = "[Fije la mirada. Sostenga el ritmo de izquierda a derecha] $replaced"
                    )
                )
            }
        }
    }
}

enum class EncounterTone(val label: String, val description: String) {
    COLD("Frío/Monosilábico", "Respuestas cortas, secas y desapegadas."),
    VICTIM("Víctima/Lamentable", "Se queja, usa la pena y busca dar lástima."),
    CHARMING("Encantador/Seductor", "Utiliza la nostalgia, el carisma o falsas promesas."),
    HOSTILE("Hostil/Agresivo", "Respuestas defensivas, reproches o ataques directos."),
    INDIFFERENT("Indiferente/Distante", "Muestra total desinterés por tu estado.")
}

@kotlinx.serialization.Serializable
data class AttachmentPatternInsight(
    val hasEnoughData: Boolean = false,
    val patternName: String = "No evaluado",
    val description: String = "Sigue registrando en tu diario para evaluar tus patrones de apego de forma objetiva.",
    val evidenceExcerpt: String = "",
    val gentleSuggestion: String = "La paciencia y el autoconocimiento son indispensables para consolidar tu soberanía interior."
)

@kotlinx.serialization.Serializable
data class FrameworkRecommendation(
    val recommendedFramework: String = "PSICOLOGIA_MODERNA",
    val matchConfidencePercentage: Int = 95,
    val rationale: String = "La psicología moderna ofrece las herramientas más directas y probadas para mitigar la ansiedad y la abstinencia afectiva inicial.",
    val primaryBenefit: String = "Estrategias de regulación emocional inmediata, desconexión y desintoxocación de la rumiación."
)

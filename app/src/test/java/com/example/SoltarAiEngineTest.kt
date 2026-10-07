package com.example

import com.example.ai.SoltarAiEngine
import com.example.ai.SoltarUserContext
import com.example.data.ClinicalKnowledgeBase
import com.example.data.SoltarFramework
import kotlinx.coroutines.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SoltarAiEngineTest {

    @Before
    fun setUp() {
        com.example.ai.ClinicalVariantRegistry.clearMemory()
    }

    @Test
    fun testSelfHarmTriggerDetection() {
        val criticalPhrases = listOf(
            "quiero morir",
            "tengo ganas de suicidarme",
            "no quiero vivir más",
            "acabar con todo de una vez",
            "no encuentro salida y quiero dejar de respirar",
            "pastillas para no despertar",
            "hacerme daño y cortarme"
        )

        criticalPhrases.forEach { phrase ->
            assertTrue("Debe detectar ideación crítica en: '$phrase'", SoltarAiEngine.checkSelfHarmTrigger(phrase))
        }

        val benignPhrases = listOf(
            "hoy me siento triste pero quiero salir a caminar",
            "extraño a mi ex pareja",
            "tengo el impulso de escribirle",
            "¿cómo aplico el estoicismo hoy?"
        )

        benignPhrases.forEach { phrase ->
            assertFalse("No debe disparar falso positivo en: '$phrase'", SoltarAiEngine.checkSelfHarmTrigger(phrase))
        }
    }

    @Test
    fun testCategoryMappingReconstruccion() {
        val mapping = com.example.ai.SoltarAiEngine.CATEGORY_TO_KNOWLEDGE_BASE
        assertEquals("RECONSTRUCCION", mapping[com.example.ai.ClinicalCategory.RECONSTRUIR_GENERAL])
    }

    @Test
    fun testHeadlineExtraction() {
        val context = SoltarUserContext()
        val tag = com.example.ui.screens.QuickTag(com.example.ai.ClinicalCategory.RUMIACION_BUCLE, "Rumiación", com.example.ui.screens.QuickGroup.MENTE)
        val reflection = com.example.ai.SoltarAiEngine.quickReflection(tag.category, com.example.data.SoltarFramework.PSICOLOGIA_MODERNA, context, "id")
        assertNotNull(reflection)
        assertTrue(reflection.body.isNotBlank())
    }

    @Test
    fun testClinicalKnowledgeBaseMultiFramework() {
        val stoicCapsule = ClinicalKnowledgeBase.findRelevantCapsule("impulso de escribir", SoltarFramework.ESTOICO)
        assertNotNull(stoicCapsule)
        assertTrue(stoicCapsule.author.contains("Epicteto") || stoicCapsule.author.contains("Marco Aurelio") || stoicCapsule.author.contains("Séneca"))

        val catholicCapsule = ClinicalKnowledgeBase.findRelevantCapsule("dolor y soledad", SoltarFramework.CATOLICO)
        assertNotNull(catholicCapsule)

        val modernCapsule = ClinicalKnowledgeBase.findRelevantCapsule("abstinencia y culpa", SoltarFramework.PSICOLOGIA_MODERNA)
        assertNotNull(modernCapsule)
    }

    @Test
    fun testAllFifteenNewCategoriesClassification() {
        val testCases = listOf(
            "tiene pareja nueva y está con otra persona" to com.example.ai.ClinicalCategory.NUEVA_PAREJA_EX,
            "nunca voy a encontrar a nadie me voy a quedar solo para siempre" to com.example.ai.ClinicalCategory.MIEDO_FUTURO_SOLEDAD,
            "anoche le escribí y rompí el contacto" to com.example.ai.ClinicalCategory.RECAIDA_OCURRIDA,
            "soy un desastre no tengo fuerza de voluntad" to com.example.ai.ClinicalCategory.AUTOCRITICA_RECAIDA,
            "hoy me sentí bien y creo que voy mejorando" to com.example.ai.ClinicalCategory.PROGRESO_POSITIVO,
            "tengo que verlo por los niños y la custodia" to com.example.ai.ClinicalCategory.CONTACTO_INEVITABLE,
            "descubrí que me engañó y me fue infiel" to com.example.ai.ClinicalCategory.TRAICION_INFIDELIDAD,
            "lo quiero y lo odio tengo sentimientos contradictorios" to com.example.ai.ClinicalCategory.AMBIVALENCIA_EMOCIONAL,
            "no tengo apetito y siento un nudo en el pecho" to com.example.ai.ClinicalCategory.SINTOMAS_FISICOS,
            "por la noche es peor me desvelo pensando en la cama" to com.example.ai.ClinicalCategory.RUMIACION_NOCTURNA,
            "cuánto va a durar esto es normal sentir esto" to com.example.ai.ClinicalCategory.METAPREGUNTAS_PROCESO,
            "dime que hice lo correcto e hice bien en bloquearlo" to com.example.ai.ClinicalCategory.BUSQUEDA_REAFIRMACION,
            "qué hago con los regalos y borrar las fotos" to com.example.ai.ClinicalCategory.OBJETOS_RECUERDOS,
            "llevo meses y sigo igual siento que no avanzo" to com.example.ai.ClinicalCategory.ESTANCAMIENTO_PROCESO,
            "fui yo quien lo dejó y terminé yo la relación" to com.example.ai.ClinicalCategory.DUDA_HABER_TERMINADO
        )

        testCases.forEach { (phrase, expectedCategory) ->
            val detectedCategory = com.example.ai.ClinicalCategoryClassifier.classify(phrase, false)
            assertEquals("La frase '$phrase' debe clasificarse como $expectedCategory", expectedCategory, detectedCategory)
        }
    }

    @Test
    fun testSplitTextByLinesPreservesLinesAndChunkBudget() {
        val lines = (1..50).map { "Línea de prueba $it con texto representativo para evaluar la división correcta." }
        val fullText = lines.joinToString("\n")
        val maxChunk = 300

        val chunks = SoltarAiEngine.splitTextByLines(fullText, maxChunk)
        assertTrue("Debe generar más de un fragmento", chunks.size > 1)
        
        chunks.forEach { chunk ->
            assertTrue("Ningún fragmento debe exceder el límite permitido", chunk.length <= maxChunk + 100) // margen de línea
            assertFalse("Los fragmentos no deben comenzar ni terminar con saltos de línea sueltos", chunk.startsWith("\n") || chunk.endsWith("\n"))
        }

        val reconstructed = chunks.joinToString("\n")
        assertEquals("La reconstrucción de líneas debe coincidir con el texto original", fullText, reconstructed)

        // Línea única larga sin saltos de línea (4000 caracteres)
        val singleLongLine = (1..400).joinToString(" ") { "palabra$it" }
        val lineChunks = SoltarAiEngine.splitTextByLines(singleLongLine, 1500)
        assertTrue("Debe dividir la línea única larga", lineChunks.size > 1)
        lineChunks.forEach { c ->
            assertTrue("El chunk de línea larga no debe superar 1600 caracteres", c.length <= 1600)
        }
    }
}

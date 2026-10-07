package com.example.ai

import com.example.ai.content.FreshnessStore
import com.example.ai.content.LegacySource
import com.example.ai.content.QuoteChoice
import com.example.ai.content.QuoteSource
import com.example.ai.content.pickFresh
import com.example.ai.content.LegacyVariant
import com.example.data.ClinicalKnowledgeBase
import com.example.data.SoltarFramework
import com.example.data.WisdomBank
import kotlin.random.Random

/**
 * Puente entre el compositor y el contenido clínico ya existente: las 216 variantes completas, las cápsulas
 * (cita + acción por categoría) y el banco de sabiduría. Sin estado propio: la frescura vive en FreshnessStore.
 */
object ClinicalLegacySource : LegacySource, QuoteSource {

    /** Categoría de cápsula (RUMIACION | IMPULSO | DUELO | AUTOESTIMA | LIMITES | SOLEDAD | CULPA | RECONSTRUCCION) de cada etiqueta. */
    
    val CATEGORY_TO_KNOWLEDGE_BASE: Map<ClinicalCategory, String> = mapOf(
        ClinicalCategory.IMPULSO_CONTACTAR to "IMPULSO",
        ClinicalCategory.SENALES_DIGITALES to "LIMITES",
        ClinicalCategory.RECAIDA_OCURRIDA to "IMPULSO",
        ClinicalCategory.AUTOCRITICA_RECAIDA to "CULPA",
        ClinicalCategory.CONTACTO_INEVITABLE to "LIMITES",
        ClinicalCategory.ENCUENTRO_CASUAL to "LIMITES",
        ClinicalCategory.RECUPERAR_PAREJA to "IMPULSO",
        ClinicalCategory.RUMIACION_BUCLE to "RUMIACION",
        ClinicalCategory.RUMIACION_NOCTURNA to "RUMIACION",
        ClinicalCategory.NOSTALGIA_IDEALIZACION to "DUELO",
        ClinicalCategory.DUDA_HABER_TERMINADO to "RUMIACION",
        ClinicalCategory.AMBIVALENCIA_EMOCIONAL to "DUELO",
        ClinicalCategory.METAPREGUNTAS_PROCESO to "RUMIACION",
        ClinicalCategory.ESTANCAMIENTO_PROCESO to "RUMIACION",
        ClinicalCategory.MIEDO_FUTURO_SOLEDAD to "SOLEDAD",
        ClinicalCategory.ANSIEDAD_SOMATICA to "IMPULSO",
        ClinicalCategory.SINTOMAS_FISICOS to "DUELO",
        ClinicalCategory.INSOMNIO_NOCHE to "RUMIACION",
        ClinicalCategory.CULPA_RENCOR_RABIA to "CULPA",
        ClinicalCategory.SOLEDAD_VACIO to "SOLEDAD",
        ClinicalCategory.AUTOESTIMA_RECHAZO to "AUTOESTIMA",
        ClinicalCategory.DEPENDENCIA_EMOCIONAL to "AUTOESTIMA",
        ClinicalCategory.BUSQUEDA_REAFIRMACION to "AUTOESTIMA",
        ClinicalCategory.TRAICION_INFIDELIDAD to "CULPA",
        ClinicalCategory.NUEVA_PAREJA_EX to "DUELO",
        ClinicalCategory.FECHAS_SIGNIFICATIVAS to "DUELO",
        ClinicalCategory.OBJETOS_RECUERDOS to "DUELO",
        ClinicalCategory.COPARENTALIDAD_LOGISTICA to "LIMITES",
        ClinicalCategory.CONTACTO_CERO_LIMITES to "LIMITES",
        ClinicalCategory.ETIQUETAS_DIAGNOSTICAS to "LIMITES",
        ClinicalCategory.PROGRESO_POSITIVO to "RECONSTRUCCION",
        ClinicalCategory.RECONSTRUIR_GENERAL to "RECONSTRUCCION"
    )
    override fun variants(
        category: ClinicalCategory,
        framework: SoltarFramework,
        userContext: SoltarUserContext
    ): List<LegacyVariant> =
        ClinicalVariantRegistry.getVariantsForCategoryAndFramework(category, framework).mapIndexed { index, variant ->
            val enriched = ClinicalVariantRegistry.applyContextualNuances(variant.bodyText, category, userContext)
            val header = variant.headerGreeting.replace("**", "").trim()
            LegacyVariant(
                id = "legacy_${framework.name}_${category.name}_$index",
                headline = if (header.isNotBlank() && !header.startsWith("Hola", ignoreCase = true)) header else "",
                body = cleanText(enriched)
            )
        }

    override fun choose(
        category: ClinicalCategory,
        framework: SoltarFramework,
        store: FreshnessStore,
        exclude: Set<String>,
        random: Random
    ): QuoteChoice {
        val kbCategory = CATEGORY_TO_KNOWLEDGE_BASE[category] ?: "RECONSTRUCCION"
        val direct = ClinicalKnowledgeBase.capsules.filter {
            it.framework == framework && it.category.equals(kbCategory, ignoreCase = true)
        }
        if (direct.isNotEmpty()) {
            val allowed = direct.filter { "cap_${it.id}" !in exclude }.ifEmpty { direct }
            val capsule = pickFresh(allowed, { "cap_${it.id}" }, { 1 }, store, random)!!
            return QuoteChoice(
                id = "cap_${capsule.id}",
                quote = clean(capsule.quoteOrSource),
                author = clean(capsule.author),
                action = clean(capsule.concreteAction).ifBlank { null }
            )
        }
        val recent = store.recentIds("wis_", 20).map { it.removePrefix("wis_") } + exclude.filter { it.startsWith("wis_") }.map { it.removePrefix("wis_") }
        val card = WisdomBank.getRandomCard(framework, recent)
        return QuoteChoice(id = "wis_${card.id}", quote = clean(card.quote), author = clean(card.author), action = null)
    }

    private fun clean(text: String) = text.replace("**", "").trim()

    /** Mismo saneado que aplicaba quickReflection: sin markdown ni el saludo genérico. */
    private fun cleanText(text: String) = text
        .replace("**", "")
        .replace("*", "")
        .replace("Hola, amigo/a.", "")
        .replace("Hola, amigo/a", "")
        .replace("amigo/a", "")
        .trim()
}

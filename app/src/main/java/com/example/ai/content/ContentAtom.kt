package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.data.SoltarFramework
import java.security.MessageDigest

/** Ranuras de una tarjeta compuesta. */
enum class Slot { RECONOCIMIENTO, REENCUADRE, CITA, ACCION, CIERRE }

/** Grupos de etiquetas de "Lo Que Necesitas Ahora". Debe coincidir con QuickReflectionTags (lo comprueba un test). */
enum class ContentGroup {
    AHORA_MISMO, MENTE, CUERPO, EMOCIONES, SITUACIONES, AVANCE;

    companion object {
        private val BY_TAG: Map<ClinicalCategory, ContentGroup> = mapOf(
            ClinicalCategory.IMPULSO_CONTACTAR to AHORA_MISMO,
            ClinicalCategory.SENALES_DIGITALES to AHORA_MISMO,
            ClinicalCategory.RECAIDA_OCURRIDA to AHORA_MISMO,
            ClinicalCategory.AUTOCRITICA_RECAIDA to AHORA_MISMO,
            ClinicalCategory.CONTACTO_INEVITABLE to AHORA_MISMO,
            ClinicalCategory.ENCUENTRO_CASUAL to AHORA_MISMO,
            ClinicalCategory.RECUPERAR_PAREJA to AHORA_MISMO,

            ClinicalCategory.RUMIACION_BUCLE to MENTE,
            ClinicalCategory.RUMIACION_NOCTURNA to MENTE,
            ClinicalCategory.NOSTALGIA_IDEALIZACION to MENTE,
            ClinicalCategory.DUDA_HABER_TERMINADO to MENTE,
            ClinicalCategory.AMBIVALENCIA_EMOCIONAL to MENTE,
            ClinicalCategory.METAPREGUNTAS_PROCESO to MENTE,
            ClinicalCategory.ESTANCAMIENTO_PROCESO to MENTE,
            ClinicalCategory.MIEDO_FUTURO_SOLEDAD to MENTE,

            ClinicalCategory.ANSIEDAD_SOMATICA to CUERPO,
            ClinicalCategory.SINTOMAS_FISICOS to CUERPO,
            ClinicalCategory.INSOMNIO_NOCHE to CUERPO,

            ClinicalCategory.CULPA_RENCOR_RABIA to EMOCIONES,
            ClinicalCategory.SOLEDAD_VACIO to EMOCIONES,
            ClinicalCategory.AUTOESTIMA_RECHAZO to EMOCIONES,
            ClinicalCategory.DEPENDENCIA_EMOCIONAL to EMOCIONES,
            ClinicalCategory.BUSQUEDA_REAFIRMACION to EMOCIONES,
            ClinicalCategory.TRAICION_INFIDELIDAD to EMOCIONES,

            ClinicalCategory.NUEVA_PAREJA_EX to SITUACIONES,
            ClinicalCategory.FECHAS_SIGNIFICATIVAS to SITUACIONES,
            ClinicalCategory.OBJETOS_RECUERDOS to SITUACIONES,
            ClinicalCategory.COPARENTALIDAD_LOGISTICA to SITUACIONES,
            ClinicalCategory.CONTACTO_CERO_LIMITES to SITUACIONES,
            ClinicalCategory.ETIQUETAS_DIAGNOSTICAS to SITUACIONES,

            ClinicalCategory.PROGRESO_POSITIVO to AVANCE,
            ClinicalCategory.RECONSTRUIR_GENERAL to AVANCE
        )

        /** Etiquetas con grupo asignado (un test exige que sean todas las de [ClinicalCategory]). */
        val mapped: Set<ClinicalCategory> get() = BY_TAG.keys

        /** Grupo de una etiqueta. Si apareciera una categoría nueva sin asignar, cae en MENTE en vez de fallar. */
        fun of(tag: ClinicalCategory): ContentGroup = BY_TAG[tag] ?: MENTE

        fun tagsOf(group: ContentGroup): List<ClinicalCategory> = BY_TAG.filterValues { it == group }.keys.toList()
    }
}

/** Franja horaria local: 0-5, 6-12, 13-19, 20-23. */
enum class TimeOfDay {
    MADRUGADA, MANANA, TARDE, NOCHE;

    companion object {
        fun of(hour: Int): TimeOfDay = when (hour) {
            in 0..5 -> MADRUGADA
            in 6..12 -> MANANA
            in 13..19 -> TARDE
            else -> NOCHE
        }
    }
}

/** Fase temporal situacional según los días de racha. NO implica mejora ni curación. */
enum class TimePhase {
    PRIMEROS_DIAS, PRIMERA_SEMANA, PRIMER_MES, TRAS_UN_MES;

    companion object {
        fun of(days: Int): TimePhase = when {
            days <= 3 -> PRIMEROS_DIAS
            days <= 7 -> PRIMERA_SEMANA
            days <= 30 -> PRIMER_MES
            else -> TRAS_UN_MES
        }
    }
}

/** Condiciones opcionales de un átomo. null = no importa. Todas las no nulas deben cumplirse. */
data class AtomContext(
    val timeOfDay: TimeOfDay? = null,
    val phase: TimePhase? = null,
    val repeated: Boolean? = null,
    val recentRelapse: Boolean? = null
) {
    val isNeutral: Boolean get() = timeOfDay == null && phase == null && repeated == null && recentRelapse == null
}

/** Señales reales del momento en que se compone la tarjeta. */
data class ContentSignals(
    val timeOfDay: TimeOfDay,
    val phase: TimePhase,
    val repeated: Boolean,
    val recentRelapse: Boolean
) {
    fun satisfies(c: AtomContext): Boolean =
        (c.timeOfDay == null || c.timeOfDay == timeOfDay) &&
            (c.phase == null || c.phase == phase) &&
            (c.repeated == null || c.repeated == repeated) &&
            (c.recentRelapse == null || c.recentRelapse == recentRelapse)
}

/** Pieza mínima de contenido. Autónoma: se puede combinar en cualquier orden con otras de su ranura. */
data class ContentAtom(
    val id: String,
    val slot: Slot,
    val text: String,
    val tags: Set<ClinicalCategory> = emptySet(),
    val groups: Set<ContentGroup> = emptySet(),
    val frameworks: Set<SoltarFramework> = SoltarFramework.values().toSet(),
    val context: AtomContext = AtomContext(),
    val author: String = "",
    val source: String = ""
) {
    fun appliesTo(category: ClinicalCategory, framework: SoltarFramework): Boolean =
        framework in frameworks && (category in tags || (groups.isNotEmpty() && ContentGroup.of(category) in groups))
}

/** Crea un átomo con id estable (sha1 del contenido y su ámbito; cambiar el texto cambia el id). */
fun atom(
    slot: Slot,
    text: String,
    tags: Set<ClinicalCategory> = emptySet(),
    groups: Set<ContentGroup> = emptySet(),
    frameworks: Set<SoltarFramework> = SoltarFramework.values().toSet(),
    context: AtomContext = AtomContext(),
    author: String = "",
    source: String = ""
): ContentAtom {
    val key = listOf(
        slot.name,
        tags.map { it.name }.sorted().joinToString(","),
        groups.map { it.name }.sorted().joinToString(","),
        frameworks.map { it.name }.sorted().joinToString(","),
        context.toString(),
        text
    ).joinToString("|")
    val digest = MessageDigest.getInstance("SHA-1").digest(key.toByteArray(Charsets.UTF_8))
    val id = digest.joinToString("") { "%02x".format(it) }.take(10)
    return ContentAtom(id, slot, text, tags, groups, frameworks, context, author, source)
}

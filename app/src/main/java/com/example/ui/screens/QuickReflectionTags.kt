package com.example.ui.screens

import com.example.ai.ClinicalCategory

enum class QuickGroup(val title: String, val defaultAction: String) {
    AHORA_MISMO("Ahora mismo", "Antes de dar el siguiente paso, respira lento cinco veces y espera diez minutos. Después decide con la cabeza más fría."),
    MENTE("Mente", "Escribe en una frase el pensamiento que se repite y, debajo, otra más realista y amable sobre lo mismo."),
    CUERPO("Cuerpo", "Suelta el aire despacio: inhala cuatro segundos, exhala seis, durante dos minutos. Después bebe agua y estira los hombros."),
    EMOCIONES("Emociones", "Ponle nombre a lo que sientes en una frase y déjala escrita sin juzgarla."),
    SITUACIONES("Situaciones", "Elige un solo paso pequeño y concreto para hoy sobre esta situación y anótalo."),
    AVANCE("Avance", "Anota una cosa que hayas hecho hoy por ti y guárdala en tu diario.")
}

data class QuickTag(
    val category: ClinicalCategory,
    val label: String,
    val group: QuickGroup
)

object QuickReflectionTags {
    val allTags: List<QuickTag> = listOf(
        // Ahora mismo (7)
        QuickTag(ClinicalCategory.IMPULSO_CONTACTAR, "Tengo ganas de escribirle", QuickGroup.AHORA_MISMO),
        QuickTag(ClinicalCategory.SENALES_DIGITALES, "Quiero mirar sus redes", QuickGroup.AHORA_MISMO),
        QuickTag(ClinicalCategory.RECAIDA_OCURRIDA, "Acabo de recaer", QuickGroup.AHORA_MISMO),
        QuickTag(ClinicalCategory.AUTOCRITICA_RECAIDA, "Me castigo por haber recaído", QuickGroup.AHORA_MISMO),
        QuickTag(ClinicalCategory.CONTACTO_INEVITABLE, "Tengo que verle o hablarle", QuickGroup.AHORA_MISMO),
        QuickTag(ClinicalCategory.ENCUENTRO_CASUAL, "Me lo he cruzado", QuickGroup.AHORA_MISMO),
        QuickTag(ClinicalCategory.RECUPERAR_PAREJA, "Quiero recuperarle", QuickGroup.AHORA_MISMO),

        // Mente (8)
        QuickTag(ClinicalCategory.RUMIACION_BUCLE, "No paro de darle vueltas", QuickGroup.MENTE),
        QuickTag(ClinicalCategory.RUMIACION_NOCTURNA, "Me ronda de noche", QuickGroup.MENTE),
        QuickTag(ClinicalCategory.NOSTALGIA_IDEALIZACION, "Solo recuerdo lo bueno", QuickGroup.MENTE),
        QuickTag(ClinicalCategory.DUDA_HABER_TERMINADO, "¿Hice bien en terminar?", QuickGroup.MENTE),
        QuickTag(ClinicalCategory.AMBIVALENCIA_EMOCIONAL, "Siento cosas opuestas a la vez", QuickGroup.MENTE),
        QuickTag(ClinicalCategory.METAPREGUNTAS_PROCESO, "¿Voy bien? ¿Cuánto falta?", QuickGroup.MENTE),
        QuickTag(ClinicalCategory.ESTANCAMIENTO_PROCESO, "Siento que no avanzo", QuickGroup.MENTE),
        QuickTag(ClinicalCategory.MIEDO_FUTURO_SOLEDAD, "Me da miedo quedarme solo/a", QuickGroup.MENTE),

        // Cuerpo (3)
        QuickTag(ClinicalCategory.ANSIEDAD_SOMATICA, "Ansiedad en el cuerpo", QuickGroup.CUERPO),
        QuickTag(ClinicalCategory.SINTOMAS_FISICOS, "Me duele el cuerpo / no como", QuickGroup.CUERPO),
        QuickTag(ClinicalCategory.INSOMNIO_NOCHE, "No puedo dormir", QuickGroup.CUERPO),

        // Emociones (6)
        QuickTag(ClinicalCategory.CULPA_RENCOR_RABIA, "Culpa, rabia o rencor", QuickGroup.EMOCIONES),
        QuickTag(ClinicalCategory.SOLEDAD_VACIO, "Vacío y soledad", QuickGroup.EMOCIONES),
        QuickTag(ClinicalCategory.AUTOESTIMA_RECHAZO, "Me siento poco valioso/a", QuickGroup.EMOCIONES),
        QuickTag(ClinicalCategory.DEPENDENCIA_EMOCIONAL, "Siento que no puedo sin esa persona", QuickGroup.EMOCIONES),
        QuickTag(ClinicalCategory.BUSQUEDA_REAFIRMACION, "Necesito que me digan que estoy bien", QuickGroup.EMOCIONES),
        QuickTag(ClinicalCategory.TRAICION_INFIDELIDAD, "Me traicionó", QuickGroup.EMOCIONES),

        // Situaciones (6)
        QuickTag(ClinicalCategory.NUEVA_PAREJA_EX, "Tiene pareja nueva", QuickGroup.SITUACIONES),
        QuickTag(ClinicalCategory.FECHAS_SIGNIFICATIVAS, "Se acerca una fecha difícil", QuickGroup.SITUACIONES),
        QuickTag(ClinicalCategory.OBJETOS_RECUERDOS, "Objetos y recuerdos", QuickGroup.SITUACIONES),
        QuickTag(ClinicalCategory.COPARENTALIDAD_LOGISTICA, "Hijos y logística", QuickGroup.SITUACIONES),
        QuickTag(ClinicalCategory.CONTACTO_CERO_LIMITES, "Poner límites / contacto cero", QuickGroup.SITUACIONES),
        QuickTag(ClinicalCategory.ETIQUETAS_DIAGNOSTICAS, "Quiero ponerle una etiqueta (narcisista, tóxico…)", QuickGroup.SITUACIONES),

        // Avance (2)
        QuickTag(ClinicalCategory.PROGRESO_POSITIVO, "Hoy me siento mejor", QuickGroup.AVANCE),
        QuickTag(ClinicalCategory.RECONSTRUIR_GENERAL, "Quiero reconstruirme", QuickGroup.AVANCE)
    )

    fun getGroupOrder(isLifeCoach: Boolean): List<QuickGroup> {
        return if (isLifeCoach) {
            listOf(
                QuickGroup.AVANCE,
                QuickGroup.AHORA_MISMO,
                QuickGroup.MENTE,
                QuickGroup.CUERPO,
                QuickGroup.EMOCIONES,
                QuickGroup.SITUACIONES
            )
        } else {
            listOf(
                QuickGroup.AHORA_MISMO,
                QuickGroup.MENTE,
                QuickGroup.CUERPO,
                QuickGroup.EMOCIONES,
                QuickGroup.SITUACIONES,
                QuickGroup.AVANCE
            )
        }
    }
}

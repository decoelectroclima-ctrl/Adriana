package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.data.SoltarFramework

/**
 * Contenido del grupo AVANCE (D4, parte 3): PROGRESO_POSITIVO, RECONSTRUIR_GENERAL.
 */
object ContentDatabaseAvance {

    private val PROGRESO = setOf(ClinicalCategory.PROGRESO_POSITIVO)
    private val RECONSTRUIR = setOf(ClinicalCategory.RECONSTRUIR_GENERAL)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)

    private fun reco(tags: Set<ClinicalCategory>, text: String) = atom(Slot.RECONOCIMIENTO, text, tags = tags)
    private fun reen(tags: Set<ClinicalCategory>, fw: Set<SoltarFramework>, text: String) =
        atom(Slot.REENCUADRE, text, tags = tags, frameworks = fw)
    private fun acc(tags: Set<ClinicalCategory>, text: String) = atom(Slot.ACCION, text, tags = tags)

    val atoms: List<ContentAtom> = listOf(

        // ================= PROGRESO_POSITIVO =================
        reco(PROGRESO, "Sentirte mejor hoy es una buena señal, y también algo que merece reconocerse."),
        reco(PROGRESO, "Los días buenos también forman parte del proceso, no solo los difíciles."),
        reco(PROGRESO, "Notar que hoy pesa menos no significa que el resto del camino haya sido en vano."),
        reco(PROGRESO, "Permitirte sentirte bien hoy no traiciona lo vivido antes."),
        reco(PROGRESO, "Un buen día es una señal real de que algo está cambiando, aunque mañana vuelva a costar."),

        reen(PROGRESO, ESTOICO, "Reconocer un buen día con serenidad, sin aferrarte a que se repita igual mañana, es sabiduría."),
        reen(PROGRESO, ESTOICO, "Sentirte mejor hoy es el resultado de las acciones sostenidas que sí dependían de ti."),
        reen(PROGRESO, ESTOICO, "Puedes disfrutar este momento sin necesidad de que sea perfecto ni definitivo."),
        reen(PROGRESO, ESTOICO, "El buen ánimo de hoy no anula el trabajo hecho en los días difíciles."),
        reen(PROGRESO, ESTOICO, "Aceptar los días buenos con la misma calma que los difíciles es parte del equilibrio."),
        reen(PROGRESO, ESTOICO, "Reconocer tu propio esfuerzo es también un acto de justicia contigo mismo/a."),

        reen(PROGRESO, CATOLICO, "Puedes agradecer este momento de alivio como un regalo, sin exigirte que dure para siempre."),
        reen(PROGRESO, CATOLICO, "Los días buenos también son ocasión de gratitud por el camino recorrido."),
        reen(PROGRESO, CATOLICO, "Permitirte sentir alegría hoy no significa haber olvidado lo vivido."),
        reen(PROGRESO, CATOLICO, "Este momento de paz puede ser también una señal de que el proceso sigue su curso."),
        reen(PROGRESO, CATOLICO, "Puedes dar gracias por este alivio, aunque sepas que mañana puede costar de nuevo."),
        reen(PROGRESO, CATOLICO, "Reconocer el bien recibido hoy fortalece la esperanza para los días que vengan."),

        reen(PROGRESO, PSICOLOGIA, "Registrar los días buenos ayuda a ver el progreso real del proceso con más claridad."),
        reen(PROGRESO, PSICOLOGIA, "Sentirte mejor hoy es el resultado acumulado de las rutinas de cuidado que has sostenido."),
        reen(PROGRESO, PSICOLOGIA, "Permitirte disfrutar un buen día, sin culpa, fortalece la recuperación a largo plazo."),
        reen(PROGRESO, PSICOLOGIA, "El progreso emocional no es lineal; los días buenos también son parte válida de la curva."),
        reen(PROGRESO, PSICOLOGIA, "Anotar qué hiciste distinto hoy ayuda a identificar qué te está funcionando."),
        reen(PROGRESO, PSICOLOGIA, "Un buen día no garantiza que mañana lo sea, pero sí confirma que el cambio es posible."),

        acc(PROGRESO, "Anota en tu diario qué hizo que hoy fuera un día más ligero."),
        acc(PROGRESO, "Comparte este buen momento con alguien de tu red de apoyo."),
        acc(PROGRESO, "Haz algo que disfrutes hoy, sin culpa, como parte de este momento de calma."),
        acc(PROGRESO, "Guarda este día como referencia para los momentos en que sientas que no avanzas."),
        acc(PROGRESO, "Reconoce en voz alta o por escrito un esfuerzo concreto que hiciste para llegar hasta aquí."),
        acc(PROGRESO, "Dedica un momento a agradecer, a tu manera, este alivio de hoy."),

        // ================= RECONSTRUIR_GENERAL =================
        reco(RECONSTRUIR, "Querer reconstruirte es un paso importante, aunque no siempre sepas por dónde empezar."),
        reco(RECONSTRUIR, "Reconstruir tu vida no significa borrar lo vivido, sino integrar una nueva etapa."),
        reco(RECONSTRUIR, "El deseo de empezar de nuevo puede convivir con la incertidumbre de no saber cómo."),
        reco(RECONSTRUIR, "No hace falta tener un plan completo para empezar a reconstruirte poco a poco."),
        reco(RECONSTRUIR, "Reconstruir es un proceso gradual, hecho de pasos pequeños más que de grandes decisiones únicas."),

        reen(RECONSTRUIR, ESTOICO, "Reconstruir tu vida depende de las acciones diarias que sí están en tu mano, no de un plan perfecto."),
        reen(RECONSTRUIR, ESTOICO, "Puedes empezar por lo pequeño: un hábito, una rutina, una decisión concreta cada día."),
        reen(RECONSTRUIR, ESTOICO, "El carácter que construyes hoy, con constancia, es la base de la vida que estás reconstruyendo."),
        reen(RECONSTRUIR, ESTOICO, "No necesitas tener todas las respuestas para dar el siguiente paso razonable."),
        reen(RECONSTRUIR, ESTOICO, "Reconstruir con calma, sin prisa, suele dar resultados más sólidos que hacerlo con urgencia."),
        reen(RECONSTRUIR, ESTOICO, "Cada acción pequeña y constante pesa más que una gran decisión tomada una sola vez."),

        reen(RECONSTRUIR, CATOLICO, "Puedes confiar en que este proceso de reconstrucción tiene su propio tiempo, guiado paso a paso."),
        reen(RECONSTRUIR, CATOLICO, "Reconstruirte también es una forma de agradecer la vida que aún tienes por delante."),
        reen(RECONSTRUIR, CATOLICO, "No estás solo/a en esta reconstrucción, aunque a veces se sienta así."),
        reen(RECONSTRUIR, CATOLICO, "Puedes pedir claridad para saber qué pasos dar, sin necesidad de verlo todo de golpe."),
        reen(RECONSTRUIR, CATOLICO, "Cada pequeño paso hacia adelante también es un acto de esperanza."),
        reen(RECONSTRUIR, CATOLICO, "Confiar en el proceso no significa saber el resultado, sino dar el paso de hoy."),

        reen(RECONSTRUIR, PSICOLOGIA, "Reconstruir la identidad después de una ruptura importante lleva tiempo y ocurre en pasos pequeños."),
        reen(RECONSTRUIR, PSICOLOGIA, "Definir metas concretas y alcanzables ayuda a que la reconstrucción no se sienta abrumadora."),
        reen(RECONSTRUIR, PSICOLOGIA, "Probar cosas nuevas, aunque sean pequeñas, ayuda a redescubrir intereses propios."),
        reen(RECONSTRUIR, PSICOLOGIA, "La reconstrucción no es lineal; incluye avances, pausas y ajustes de rumbo."),
        reen(RECONSTRUIR, PSICOLOGIA, "Apoyarte en tu red social facilita sostener este proceso de reconstrucción."),
        reen(RECONSTRUIR, PSICOLOGIA, "Celebrar pequeños logros a lo largo del camino refuerza la motivación para seguir."),

        acc(RECONSTRUIR, "Elige una sola área de tu vida (trabajo, salud, amistades) para dar un paso pequeño esta semana."),
        acc(RECONSTRUIR, "Escribe tres cosas que te gustaría retomar o empezar en esta nueva etapa."),
        acc(RECONSTRUIR, "Prueba una actividad nueva este mes, aunque sea pequeña, solo para explorar."),
        acc(RECONSTRUIR, "Anota un logro reciente, por pequeño que sea, relacionado con esta reconstrucción."),
        acc(RECONSTRUIR, "Habla con alguien de confianza sobre cómo imaginas esta nueva etapa."),
        acc(RECONSTRUIR, "Dedica un rato a cuidar algo que sea solo tuyo: un espacio, un hábito o un proyecto.")
    )
}

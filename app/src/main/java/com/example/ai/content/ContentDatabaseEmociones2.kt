package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.data.SoltarFramework

/**
 * Contenido del grupo EMOCIONES (D3, parte 2 de 2): DEPENDENCIA_EMOCIONAL, BUSQUEDA_REAFIRMACION, TRAICION_INFIDELIDAD.
 */
object ContentDatabaseEmociones2 {

    private val DEPENDENCIA = setOf(ClinicalCategory.DEPENDENCIA_EMOCIONAL)
    private val REAFIRMACION = setOf(ClinicalCategory.BUSQUEDA_REAFIRMACION)
    private val TRAICION = setOf(ClinicalCategory.TRAICION_INFIDELIDAD)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)

    private fun reco(tags: Set<ClinicalCategory>, text: String) = atom(Slot.RECONOCIMIENTO, text, tags = tags)
    private fun reen(tags: Set<ClinicalCategory>, fw: Set<SoltarFramework>, text: String) =
        atom(Slot.REENCUADRE, text, tags = tags, frameworks = fw)
    private fun acc(tags: Set<ClinicalCategory>, text: String) = atom(Slot.ACCION, text, tags = tags)

    val atoms: List<ContentAtom> = listOf(

        // ================= DEPENDENCIA_EMOCIONAL =================
        reco(DEPENDENCIA, "Sentir que no puedes sin esa persona es una emoción intensa, no una verdad sobre tu capacidad."),
        reco(DEPENDENCIA, "El apego fuerte hace sentir que necesitas a alguien para funcionar, aunque no sea así en realidad."),
        reco(DEPENDENCIA, "Notar cuánto dependías emocionalmente de esa relación es parte de entender lo que estás soltando."),
        reco(DEPENDENCIA, "Sentir vacío al pensar en seguir sin esa persona es doloroso, pero no significa que no puedas."),
        reco(DEPENDENCIA, "La dependencia emocional se construye con el tiempo, y también se puede soltar con el tiempo."),

        reen(DEPENDENCIA, ESTOICO, "Tu bienestar no puede depender por completo de algo que no controlas, como la presencia de otra persona."),
        reen(DEPENDENCIA, ESTOICO, "Puedes reconocer el apego sin dejar que decida cada una de tus acciones."),
        reen(DEPENDENCIA, ESTOICO, "Construir autosuficiencia emocional es un ejercicio de fortaleza, no de frialdad."),
        reen(DEPENDENCIA, ESTOICO, "Necesitar a alguien de forma sana es distinto de no poder funcionar sin esa persona."),
        reen(DEPENDENCIA, ESTOICO, "Lo que hoy sientes como imposible de sostener, con práctica, se vuelve más llevadero."),
        reen(DEPENDENCIA, ESTOICO, "Recuperar tu centro propio es un trabajo diario, no un estado que llega de golpe."),

        reen(DEPENDENCIA, CATOLICO, "Tu valor y tu sostén último no dependen de ninguna persona, por importante que haya sido."),
        reen(DEPENDENCIA, CATOLICO, "Puedes pedir fuerza para sostenerte en este tiempo de aprender a estar sin esa presencia."),
        reen(DEPENDENCIA, CATOLICO, "El amor sano no anula tu capacidad de sostenerte por ti mismo/a."),
        reen(DEPENDENCIA, CATOLICO, "Confiar te ayuda a soltar la idea de que necesitas a una sola persona para estar bien."),
        reen(DEPENDENCIA, CATOLICO, "Puedes apoyarte en la oración y en tu comunidad mientras reconstruyes tu propio centro."),
        reen(DEPENDENCIA, CATOLICO, "Aprender a sostenerte a ti mismo/a también es parte de tu crecimiento como persona."),

        reen(DEPENDENCIA, PSICOLOGIA, "La dependencia emocional intensa suele estar ligada al estilo de apego que desarrollaste, no a un fallo personal."),
        reen(DEPENDENCIA, PSICOLOGIA, "El cerebro asocia esa persona con seguridad; soltarla activa una respuesta de alarma real, aunque temporal."),
        reen(DEPENDENCIA, PSICOLOGIA, "Construir otras fuentes de seguridad (amistades, rutinas, logros propios) ayuda a reducir la dependencia con el tiempo."),
        reen(DEPENDENCIA, PSICOLOGIA, "Sentir que no puedes sin esa persona es una emoción intensa, no una evaluación objetiva de tu capacidad."),
        reen(DEPENDENCIA, PSICOLOGIA, "La autosuficiencia emocional se entrena con pequeñas acciones repetidas, no con un solo esfuerzo de voluntad."),
        reen(DEPENDENCIA, PSICOLOGIA, "Notar el patrón de dependencia es el primer paso para empezar a modificarlo."),

        acc(DEPENDENCIA, "Haz una lista de tres cosas que sabes hacer bien tú solo/a, sin ayuda de nadie."),
        acc(DEPENDENCIA, "Dedica un rato a una actividad que dependa solo de ti para disfrutarla."),
        acc(DEPENDENCIA, "Contacta a alguien de tu red de apoyo, para recordar que tienes más vínculos además de esa relación."),
        acc(DEPENDENCIA, "Anota una decisión pequeña que puedas tomar hoy tú solo/a, sin consultarlo con nadie."),
        acc(DEPENDENCIA, "Practica una rutina de cuidado personal que puedas sostener sin depender de otra persona."),
        acc(DEPENDENCIA, "Escribe qué necesitas emocionalmente ahora y qué parte de eso puedes darte tú mismo/a."),

        // ================= BUSQUEDA_REAFIRMACION =================
        reco(REAFIRMACION, "Necesitar que te digan que estás bien es comprensible cuando la seguridad interna está tambaleando."),
        reco(REAFIRMACION, "Buscar validación externa una y otra vez suele calmar un momento y dejar la misma duda después."),
        reco(REAFIRMACION, "Querer escuchar que vas bien no significa que no confíes en ti; es parte del proceso."),
        reco(REAFIRMACION, "La necesidad de reafirmación constante suele intensificarse en momentos de mayor incertidumbre."),
        reco(REAFIRMACION, "Pedir apoyo es sano; pedirlo de forma constante buscando certeza total puede no aportar la calma que buscas."),

        reen(REAFIRMACION, ESTOICO, "La certeza que buscas fuera no depende de ti; la calma que puedes construir dentro, sí."),
        reen(REAFIRMACION, ESTOICO, "Ninguna cantidad de confirmación externa sustituye el juicio propio bien entrenado."),
        reen(REAFIRMACION, ESTOICO, "Puedes pedir apoyo sin depender de él para sentirte bien."),
        reen(REAFIRMACION, ESTOICO, "La validación de otros es útil, pero no es la base sobre la que debe sostenerse tu calma."),
        reen(REAFIRMACION, ESTOICO, "Entrenar la confianza en tu propio juicio reduce la necesidad de preguntarlo todo hacia fuera."),
        reen(REAFIRMACION, ESTOICO, "Puedes notar la necesidad de reafirmación sin actuarla cada vez que aparece."),

        reen(REAFIRMACION, CATOLICO, "Puedes pedir a Dios la paz que buscas fuera y que a veces no encuentras en la confirmación de otros."),
        reen(REAFIRMACION, CATOLICO, "Buscar apoyo en tu comunidad es bueno; no tienes que cargar solo/a con esta necesidad."),
        reen(REAFIRMACION, CATOLICO, "Tu valor no depende de que alguien te lo confirme constantemente."),
        reen(REAFIRMACION, CATOLICO, "Confiar en que eres sostenido/a, más allá de la opinión de otros, también es un camino de fe."),
        reen(REAFIRMACION, CATOLICO, "Puedes ofrecer esta inseguridad en oración en lugar de perseguir certezas externas."),
        reen(REAFIRMACION, CATOLICO, "La paz interior se construye también en silencio, no solo en la confirmación de los demás."),

        reen(REAFIRMACION, PSICOLOGIA, "La búsqueda constante de reafirmación suele aliviar un momento y reforzar la inseguridad después."),
        reen(REAFIRMACION, PSICOLOGIA, "Este patrón suele intensificarse cuando la autoestima está afectada por una pérdida reciente."),
        reen(REAFIRMACION, PSICOLOGIA, "Notar la necesidad sin actuarla de inmediato ayuda a entrenar la tolerancia a la incertidumbre."),
        reen(REAFIRMACION, PSICOLOGIA, "Preguntarte qué necesitas realmente, más allá de la confirmación, ayuda a identificar la emoción de fondo."),
        reen(REAFIRMACION, PSICOLOGIA, "Construir autoconfianza requiere práctica; no sustituye del todo el apoyo social, pero lo complementa."),
        reen(REAFIRMACION, PSICOLOGIA, "Pedir apoyo puntual es sano; pedirlo de forma repetida sin que calme indica que conviene otra estrategia."),

        acc(REAFIRMACION, "Antes de pedir confirmación, escribe qué es lo que realmente temes que sea cierto."),
        acc(REAFIRMACION, "Espera diez minutos antes de buscar validación y observa si la necesidad cambia."),
        acc(REAFIRMACION, "Anota una decisión que hayas tomado bien tú solo/a recientemente, sin ayuda externa."),
        acc(REAFIRMACION, "Habla con alguien de confianza sobre esta necesidad, en lugar de solo pedirle que te confirme algo puntual."),
        acc(REAFIRMACION, "Practica decirte a ti mismo/a una frase de apoyo antes de buscarla en otra persona."),
        acc(REAFIRMACION, "Dedica un rato a una actividad en la que confíes en tu propio criterio, sin consultarlo con nadie."),

        // ================= TRAICION_INFIDELIDAD =================
        reco(TRAICION, "Sentirte traicionado/a es una herida distinta a otras rupturas; cuesta más volver a confiar."),
        reco(TRAICION, "La traición no solo duele por la pérdida, también por la confianza que se rompió."),
        reco(TRAICION, "Sentir rabia, humillación o desconfianza después de una infidelidad es una respuesta comprensible."),
        reco(TRAICION, "Reconstruir la confianza en ti mismo/a, no solo en otros, es parte de sanar esta herida."),
        reco(TRAICION, "El dolor de la traición puede tardar más en cerrar que otras despedidas, y eso tiene sentido."),

        reen(TRAICION, ESTOICO, "Lo que la otra persona hizo no depende de ti; cómo reconstruyes tu confianza a partir de ahora, sí."),
        reen(TRAICION, ESTOICO, "La traición revela algo sobre quien la hizo, no sobre tu propio valor."),
        reen(TRAICION, ESTOICO, "Puedes procesar la rabia sin dejar que decida cada una de tus acciones futuras."),
        reen(TRAICION, ESTOICO, "No necesitas venganza para recuperar tu propio equilibrio."),
        reen(TRAICION, ESTOICO, "Reconstruir la confianza en ti mismo/a es un trabajo que sí está en tu mano."),
        reen(TRAICION, ESTOICO, "El buen juicio distingue entre aprender de lo ocurrido y generalizar el dolor a toda relación futura."),

        reen(TRAICION, CATOLICO, "El perdón, si llega, será un proceso, no una obligación inmediata; puedes tomarte tu tiempo."),
        reen(TRAICION, CATOLICO, "Tu dignidad no se rompió con la traición de otra persona."),
        reen(TRAICION, CATOLICO, "Puedes entregar esta herida en oración, sin que eso signifique minimizar lo ocurrido."),
        reen(TRAICION, CATOLICO, "La justicia y la misericordia pueden convivir; no tienes que elegir entre sentir dolor y sanar."),
        reen(TRAICION, CATOLICO, "Confiar de nuevo, en el momento adecuado, no te hace ingenuo/a, te hace libre del rencor."),
        reen(TRAICION, CATOLICO, "Dios sostiene tu corazón herido mientras procesas esta traición a tu propio ritmo."),

        reen(TRAICION, PSICOLOGIA, "La infidelidad activa una herida de confianza que suele necesitar más tiempo de procesamiento que otras rupturas."),
        reen(TRAICION, PSICOLOGIA, "Sentir hipervigilancia o desconfianza después de una traición es una respuesta esperable del sistema de alarma."),
        reen(TRAICION, PSICOLOGIA, "Reconstruir la confianza básica en ti mismo/a suele preceder a poder confiar de nuevo en otra persona."),
        reen(TRAICION, PSICOLOGIA, "Procesar la rabia de forma segura (hablar, escribir) ayuda más que reprimirla o actuarla impulsivamente."),
        reen(TRAICION, PSICOLOGIA, "No todas las relaciones futuras repetirán este patrón; generalizarlo puede aumentar el aislamiento."),
        reen(TRAICION, PSICOLOGIA, "El acompañamiento profesional puede ayudar especialmente cuando la traición ha sido muy dolorosa."),

        acc(TRAICION, "Escribe todo lo que sientes sobre lo ocurrido, sin filtrar ni suavizarlo."),
        acc(TRAICION, "Habla con alguien de confianza que pueda escucharte sin juzgar ni minimizar lo vivido."),
        acc(TRAICION, "Haz algo que te reconecte con tu propio criterio: una decisión pequeña que solo dependa de ti."),
        acc(TRAICION, "Si el dolor es muy intenso o persistente, considera hablarlo con un profesional de salud mental."),
        acc(TRAICION, "Respira despacio cuando aparezca la rabia, y decide con calma cómo quieres actuar después."),
        acc(TRAICION, "Anota una cosa que hoy dependa de ti recuperar, como tu rutina, tu descanso o tu tranquilidad.")
    )
}

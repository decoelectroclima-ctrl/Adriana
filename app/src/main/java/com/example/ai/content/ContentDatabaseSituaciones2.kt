package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.data.SoltarFramework

/**
 * Contenido del grupo SITUACIONES (D4, parte 2 de 3): COPARENTALIDAD_LOGISTICA, CONTACTO_CERO_LIMITES, ETIQUETAS_DIAGNOSTICAS.
 */
object ContentDatabaseSituaciones2 {

    private val COPARENTALIDAD = setOf(ClinicalCategory.COPARENTALIDAD_LOGISTICA)
    private val LIMITES = setOf(ClinicalCategory.CONTACTO_CERO_LIMITES)
    private val ETIQUETAS = setOf(ClinicalCategory.ETIQUETAS_DIAGNOSTICAS)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)

    private fun reco(tags: Set<ClinicalCategory>, text: String) = atom(Slot.RECONOCIMIENTO, text, tags = tags)
    private fun reen(tags: Set<ClinicalCategory>, fw: Set<SoltarFramework>, text: String) =
        atom(Slot.REENCUADRE, text, tags = tags, frameworks = fw)
    private fun acc(tags: Set<ClinicalCategory>, text: String) = atom(Slot.ACCION, text, tags = tags)

    val atoms: List<ContentAtom> = listOf(

        // ================= COPARENTALIDAD_LOGISTICA =================
        reco(COPARENTALIDAD, "Coordinar temas de hijos o logística con tu ex es distinto de mantener la relación abierta emocionalmente."),
        reco(COPARENTALIDAD, "Tener que hablar por los hijos no significa que el proceso de soltar se detenga."),
        reco(COPARENTALIDAD, "Mantener el contacto necesario por logística puede convivir con estar cerrando la relación como pareja."),
        reco(COPARENTALIDAD, "Es normal que estos intercambios remuevan algo, aunque sean solo prácticos."),
        reco(COPARENTALIDAD, "No hace falta que cada contacto por los hijos sea cómodo para que sea funcional."),

        reen(COPARENTALIDAD, ESTOICO, "Puedes mantener el trato centrado en lo práctico, sin que eso implique reabrir lo emocional."),
        reen(COPARENTALIDAD, ESTOICO, "Lo que él diga o haga en estas coordinaciones no depende de ti; tu forma de responder, sí."),
        reen(COPARENTALIDAD, ESTOICO, "La claridad en los acuerdos reduce la fricción más que la buena voluntad improvisada."),
        reen(COPARENTALIDAD, ESTOICO, "Puedes ser firme en los límites sin dejar de ser cordial en lo necesario."),
        reen(COPARENTALIDAD, ESTOICO, "El bienestar de tus hijos y tu propio proceso pueden sostenerse a la vez, con orden."),
        reen(COPARENTALIDAD, ESTOICO, "No necesitas que cada intercambio sea perfecto, solo que cumpla su función."),

        reen(COPARENTALIDAD, CATOLICO, "Puedes pedir paciencia para sostener estos contactos con calma, por el bien de tus hijos."),
        reen(COPARENTALIDAD, CATOLICO, "Cuidar la relación con tus hijos no exige reabrir la herida con su otro progenitor."),
        reen(COPARENTALIDAD, CATOLICO, "La caridad hacia los hijos también incluye cuidar tu propio corazón en estos intercambios."),
        reen(COPARENTALIDAD, CATOLICO, "Puedes ofrecer estas coordinaciones en oración, pidiendo claridad y paz para todos."),
        reen(COPARENTALIDAD, CATOLICO, "El respeto en lo práctico no exige sentir cercanía emocional."),
        reen(COPARENTALIDAD, CATOLICO, "Confiar te ayuda a sostener estos contactos sin que te desborden."),

        reen(COPARENTALIDAD, PSICOLOGIA, "Separar el rol de expareja del rol de coprogenitor ayuda a reducir el desgaste emocional."),
        reen(COPARENTALIDAD, PSICOLOGIA, "Establecer canales claros de comunicación (mensajes, calendarios) reduce la carga emocional de cada contacto."),
        reen(COPARENTALIDAD, PSICOLOGIA, "Sentir malestar en estos intercambios no significa que estés haciendo algo mal."),
        reen(COPARENTALIDAD, PSICOLOGIA, "Mantener el foco en el bienestar de los hijos ayuda a filtrar lo que sí necesita tu atención."),
        reen(COPARENTALIDAD, PSICOLOGIA, "Un límite claro y sostenido a tiempo reduce conflictos futuros más que evitarlo por ahora."),
        reen(COPARENTALIDAD, PSICOLOGIA, "Buscar apoyo (mediación, terapia) es una opción válida cuando la coordinación resulta muy difícil."),

        acc(COPARENTALIDAD, "Usa un canal específico (app o mensajes) solo para temas de los hijos, separado de lo personal."),
        acc(COPARENTALIDAD, "Antes de responder, tómate unos minutos si el mensaje te ha alterado."),
        acc(COPARENTALIDAD, "Escribe con antelación los puntos que necesitas tratar, para no alargar el contacto más de lo necesario."),
        acc(COPARENTALIDAD, "Después de una coordinación difícil, haz algo que te ayude a soltar la tensión."),
        acc(COPARENTALIDAD, "Habla con alguien de confianza si un intercambio reciente te ha dejado especialmente afectado/a."),
        acc(COPARENTALIDAD, "Si la coordinación se vuelve muy conflictiva, considera pedir apoyo de mediación familiar."),

        // ================= CONTACTO_CERO_LIMITES =================
        reco(LIMITES, "Poner un límite claro con tu ex es un acto de cuidado hacia ti, no un castigo hacia la otra persona."),
        reco(LIMITES, "Sostener el contacto cero cuesta más algunos días que otros, y eso es parte del proceso."),
        reco(LIMITES, "Un límite no tiene que sentirse cómodo para ser necesario."),
        reco(LIMITES, "Definir tus propios límites es parte de reconstruir tu espacio después de la ruptura."),
        reco(LIMITES, "Que un límite cueste sostenerlo no significa que esté mal puesto."),

        reen(LIMITES, ESTOICO, "Poner un límite es ejercer lo que sí depende de ti: cómo proteges tu propio espacio."),
        reen(LIMITES, ESTOICO, "No necesitas justificar tu límite ante nadie que no respete tu proceso."),
        reen(LIMITES, ESTOICO, "Sostener el límite, aunque cueste, es un ejercicio de disciplina, no de dureza."),
        reen(LIMITES, ESTOICO, "El malestar de sostener un límite es temporal; el beneficio de protegerte, más duradero."),
        reen(LIMITES, ESTOICO, "Un límite claro hoy te ahorra confusión y dolor repetido mañana."),
        reen(LIMITES, ESTOICO, "Puedes ser firme en tu límite sin necesidad de ser hostil."),

        reen(LIMITES, CATOLICO, "Cuidar tu propio corazón también es parte de amar bien, no solo dar sin medida."),
        reen(LIMITES, CATOLICO, "Puedes pedir fuerza para sostener este límite con paz, no con rencor."),
        reen(LIMITES, CATOLICO, "Un límite bien puesto puede ser también un acto de caridad hacia ti mismo/a."),
        reen(LIMITES, CATOLICO, "No tienes que sentirte culpable por proteger tu propio proceso de sanación."),
        reen(LIMITES, CATOLICO, "Confiar en que este límite te ayuda a sanar también es un acto de fe en tu propio proceso."),
        reen(LIMITES, CATOLICO, "Puedes ofrecer en oración la dificultad de sostener este límite hoy."),

        reen(LIMITES, PSICOLOGIA, "Los límites claros reducen la exposición a estímulos que reactivan el apego y dificultan sanar."),
        reen(LIMITES, PSICOLOGIA, "Es normal sentir ambivalencia al sostener un límite, incluso sabiendo que es lo correcto."),
        reen(LIMITES, PSICOLOGIA, "Cada vez que sostienes el límite, refuerzas tu capacidad de regularte a ti mismo/a."),
        reen(LIMITES, PSICOLOGIA, "Romper el límite una vez no invalida el esfuerzo sostenido hasta ahora; puedes retomarlo."),
        reen(LIMITES, PSICOLOGIA, "Tener un plan claro de qué hacer cuando el límite se pone a prueba ayuda a sostenerlo mejor."),
        reen(LIMITES, PSICOLOGIA, "El malestar de sostener un límite suele disminuir con la práctica repetida."),

        acc(LIMITES, "Escribe en una frase cuál es tu límite y por qué lo necesitas, para recordarlo cuando dudes."),
        acc(LIMITES, "Si el límite se pone a prueba hoy, avisa a alguien de confianza para que te ayude a sostenerlo."),
        acc(LIMITES, "Ajusta las notificaciones o el acceso que facilita romper el límite, si es posible."),
        acc(LIMITES, "Anota cada vez que logras sostener el límite, para ver el progreso acumulado."),
        acc(LIMITES, "Si rompiste el límite, vuelve a él con calma en el siguiente momento, sin castigarte."),
        acc(LIMITES, "Prepara una alternativa concreta para los momentos en que el límite es más difícil de sostener."),

        // ================= ETIQUETAS_DIAGNOSTICAS =================
        reco(ETIQUETAS, "Querer ponerle una etiqueta clínica a tu ex es una forma de intentar entender lo ocurrido."),
        reco(ETIQUETAS, "Buscar una palabra que explique todo el dolor es comprensible, aunque rara vez lo explica del todo."),
        reco(ETIQUETAS, "No hace falta un diagnóstico para reconocer que una conducta te hizo daño."),
        reco(ETIQUETAS, "Etiquetar puede dar una sensación de control, aunque no cambie lo vivido."),
        reco(ETIQUETAS, "Puedes nombrar lo que te dolió sin necesidad de un término clínico exacto."),

        reen(ETIQUETAS, ESTOICO, "Lo que importa no es el nombre que le pongas, sino qué haces tú a partir de ahora."),
        reen(ETIQUETAS, ESTOICO, "Una etiqueta no depende de ti verificarla; tu proceso de seguir adelante, sí."),
        reen(ETIQUETAS, ESTOICO, "Puedes describir la conducta que te dolió sin necesidad de diagnosticar a la persona."),
        reen(ETIQUETAS, ESTOICO, "Buscar la etiqueta exacta rara vez cambia el trabajo que tienes por hacer en ti."),
        reen(ETIQUETAS, ESTOICO, "El juicio sobre otra persona no está en tu mano confirmarlo con certeza."),
        reen(ETIQUETAS, ESTOICO, "Centrarte en tu propio proceso es más útil que resolver quién era exactamente él."),

        reen(ETIQUETAS, CATOLICO, "Puedes reconocer el daño sufrido sin necesidad de juzgar el alma de la otra persona."),
        reen(ETIQUETAS, CATOLICO, "Nombrar el dolor con honestidad no requiere un diagnóstico clínico."),
        reen(ETIQUETAS, CATOLICO, "Puedes entregar esta necesidad de entenderlo todo en oración, y descansar un poco de ella."),
        reen(ETIQUETAS, CATOLICO, "El perdón no depende de tener la etiqueta correcta para lo ocurrido."),
        reen(ETIQUETAS, CATOLICO, "Tu sanación no está condicionada a resolver quién era él exactamente."),
        reen(ETIQUETAS, CATOLICO, "Puedes confiar en que lo importante ahora es tu propio camino, más que la etiqueta que le pongas a él."),

        reen(ETIQUETAS, PSICOLOGIA, "Un diagnóstico formal solo puede darlo un profesional tras una evaluación adecuada, no una impresión a distancia."),
        reen(ETIQUETAS, PSICOLOGIA, "Describir conductas concretas, como no cumplía lo que decía o me hacía sentir mal, es más útil que una etiqueta general."),
        reen(ETIQUETAS, PSICOLOGIA, "Etiquetar puede dar alivio momentáneo, pero no sustituye el trabajo de procesar lo vivido."),
        reen(ETIQUETAS, PSICOLOGIA, "Centrarte en el patrón de conducta, más que en el nombre clínico, ayuda a protegerte en el futuro."),
        reen(ETIQUETAS, PSICOLOGIA, "La necesidad de etiquetar suele disminuir a medida que el propio proceso avanza."),
        reen(ETIQUETAS, PSICOLOGIA, "Si el patrón fue muy dañino, hablarlo con un profesional puede ayudarte a entenderlo mejor sin necesidad de autodiagnosticar a otra persona."),

        acc(ETIQUETAS, "Describe en tu diario las conductas concretas que te dolieron, sin usar etiquetas clínicas."),
        acc(ETIQUETAS, "Habla con alguien de confianza sobre lo vivido, centrándote en hechos concretos."),
        acc(ETIQUETAS, "Si necesitas más claridad, considera hablarlo con un profesional de salud mental."),
        acc(ETIQUETAS, "Anota qué necesitas aprender de esta experiencia para cuidarte mejor en el futuro."),
        acc(ETIQUETAS, "Haz una actividad que te aleje de seguir buscando la etiqueta exacta hoy."),
        acc(ETIQUETAS, "Recuerda que centrarte en tu propio bienestar importa más que resolver quién era él.")
    )
}

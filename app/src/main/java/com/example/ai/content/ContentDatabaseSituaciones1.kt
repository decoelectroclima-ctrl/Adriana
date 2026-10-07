package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.data.SoltarFramework

/**
 * Contenido del grupo SITUACIONES (D4, parte 1 de 3): NUEVA_PAREJA_EX, FECHAS_SIGNIFICATIVAS, OBJETOS_RECUERDOS.
 */
object ContentDatabaseSituaciones1 {

    private val NUEVA_PAREJA = setOf(ClinicalCategory.NUEVA_PAREJA_EX)
    private val FECHAS = setOf(ClinicalCategory.FECHAS_SIGNIFICATIVAS)
    private val OBJETOS = setOf(ClinicalCategory.OBJETOS_RECUERDOS)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)

    private fun reco(tags: Set<ClinicalCategory>, text: String) = atom(Slot.RECONOCIMIENTO, text, tags = tags)
    private fun reen(tags: Set<ClinicalCategory>, fw: Set<SoltarFramework>, text: String) =
        atom(Slot.REENCUADRE, text, tags = tags, frameworks = fw)
    private fun acc(tags: Set<ClinicalCategory>, text: String) = atom(Slot.ACCION, text, tags = tags)

    val atoms: List<ContentAtom> = listOf(

        // ================= NUEVA_PAREJA_EX =================
        reco(NUEVA_PAREJA, "Enterarte de que tiene pareja nueva puede remover algo que creías más tranquilo."),
        reco(NUEVA_PAREJA, "Que él haya rehecho su vida no dice nada sobre el ritmo en que debe ir la tuya."),
        reco(NUEVA_PAREJA, "Sentir dolor o rabia al saberlo es comprensible, aunque el proceso siga avanzando de fondo."),
        reco(NUEVA_PAREJA, "Cada persona procesa una ruptura a su propio ritmo; eso no mide quién estaba más o menos afectado/a."),
        reco(NUEVA_PAREJA, "Esta noticia no borra el camino que ya has recorrido hasta hoy."),

        reen(NUEVA_PAREJA, ESTOICO, "Lo que él haga con su vida no depende de ti; lo que hagas tú con la tuya, sí."),
        reen(NUEVA_PAREJA, ESTOICO, "Comparar tu ritmo con el suyo no cambia los hechos, solo añade otro juicio innecesario."),
        reen(NUEVA_PAREJA, ESTOICO, "Puedes notar el dolor de esta noticia sin dejar que decida tus próximas acciones."),
        reen(NUEVA_PAREJA, ESTOICO, "Que alguien rehaga su vida rápido no dice nada sobre el valor de lo que vivisteis."),
        reen(NUEVA_PAREJA, ESTOICO, "Tu proceso no necesita parecerse al de nadie más para ser válido."),
        reen(NUEVA_PAREJA, ESTOICO, "Lo importante hoy es cómo sigues construyendo tu propio camino, no el suyo."),

        reen(NUEVA_PAREJA, CATOLICO, "Puedes entregar este dolor en oración, sin que eso signifique que no te afecta."),
        reen(NUEVA_PAREJA, CATOLICO, "Tu valor no disminuye porque él haya encontrado a alguien más rápido que tú."),
        reen(NUEVA_PAREJA, CATOLICO, "Confiar en tu propio tiempo también es una forma de paz interior."),
        reen(NUEVA_PAREJA, CATOLICO, "Puedes desear el bien de otra persona sin que eso te obligue a sentirte bien de inmediato tú."),
        reen(NUEVA_PAREJA, CATOLICO, "El dolor de esta noticia también merece ser sostenido con ternura, no con juicio."),
        reen(NUEVA_PAREJA, CATOLICO, "Tu camino tiene su propio tiempo, aunque el de otra persona parezca ir más rápido."),

        reen(NUEVA_PAREJA, PSICOLOGIA, "Es normal que esta noticia active de nuevo emociones que creías más resueltas."),
        reen(NUEVA_PAREJA, PSICOLOGIA, "El ritmo de recuperación varía mucho entre personas; no es un indicador objetivo de nada."),
        reen(NUEVA_PAREJA, PSICOLOGIA, "Compararte con su proceso suele aumentar el malestar sin aportar información útil."),
        reen(NUEVA_PAREJA, PSICOLOGIA, "Sentir celos o tristeza ante esta noticia no contradice el progreso que ya has hecho."),
        reen(NUEVA_PAREJA, PSICOLOGIA, "Una nueva relación de su parte no invalida ni el vínculo que tuvisteis ni tu propio proceso."),
        reen(NUEVA_PAREJA, PSICOLOGIA, "Procesar esta noticia lleva su tiempo; no hace falta que te sientas bien con ella de inmediato."),

        acc(NUEVA_PAREJA, "Silencia o deja de seguir cualquier cuenta que te muestre esta información sin que la busques."),
        acc(NUEVA_PAREJA, "Escribe lo que sientes en tu diario, sin filtrarlo ni suavizarlo."),
        acc(NUEVA_PAREJA, "Habla con alguien de confianza sobre cómo te ha afectado esta noticia."),
        acc(NUEVA_PAREJA, "Haz algo que te reconecte con tu propia vida hoy, distinto de pensar en la suya."),
        acc(NUEVA_PAREJA, "Respira despacio unos minutos y permite que la emoción esté presente sin actuarla."),
        acc(NUEVA_PAREJA, "Anota un recordatorio de que tu proceso no tiene que compararse con el de nadie más."),

        // ================= FECHAS_SIGNIFICATIVAS =================
        reco(FECHAS, "Acercarse a una fecha importante puede remover recuerdos con más fuerza de lo habitual."),
        reco(FECHAS, "Cumpleaños, aniversarios o fechas señaladas pesan de otra forma cuando hay una ausencia reciente."),
        reco(FECHAS, "Es normal anticipar una fecha difícil con más ansiedad que la que trae el día en sí."),
        reco(FECHAS, "Una fecha marcada en el calendario no tiene por qué definir cómo va todo el proceso."),
        reco(FECHAS, "Sentir más dolor cerca de una fecha significativa no es un retroceso, es parte del recuerdo."),

        reen(FECHAS, ESTOICO, "No depende de ti que la fecha llegue; sí depende de ti cómo decides afrontarla."),
        reen(FECHAS, ESTOICO, "Anticipar el dolor de una fecha suele doler más que la fecha misma cuando llega."),
        reen(FECHAS, ESTOICO, "Puedes prepararte con calma para ese día, sin necesidad de que sea perfecto."),
        reen(FECHAS, ESTOICO, "Una fecha marcada es solo un día más en el calendario, aunque el significado la haga sentir distinta."),
        reen(FECHAS, ESTOICO, "Tener un plan sencillo para ese día es un ejercicio de previsión razonable."),
        reen(FECHAS, ESTOICO, "El significado que le des a la fecha depende de tu juicio, no solo del calendario."),

        reen(FECHAS, CATOLICO, "Puedes entregar en oración el peso de esta fecha antes de que llegue."),
        reen(FECHAS, CATOLICO, "No estás solo/a para atravesar este día, aunque a veces se sienta así."),
        reen(FECHAS, CATOLICO, "Una fecha difícil también puede convertirse en una ocasión de encuentro con quienes te acompañan."),
        reen(FECHAS, CATOLICO, "Confiar te ayuda a sostener este día sin que te desborde por completo."),
        reen(FECHAS, CATOLICO, "Puedes pedir la fuerza necesaria para atravesar esta fecha con paz, aunque duela."),
        reen(FECHAS, CATOLICO, "El recuerdo de lo vivido puede convivir con la esperanza de lo que aún está por venir."),

        reen(FECHAS, PSICOLOGIA, "La ansiedad anticipatoria antes de una fecha suele ser más intensa que la experiencia real del día."),
        reen(FECHAS, PSICOLOGIA, "Planificar con antelación cómo pasar ese día reduce la incertidumbre y la carga emocional."),
        reen(FECHAS, PSICOLOGIA, "Las fechas señaladas son disparadores esperables del duelo, no señales de que no has avanzado."),
        reen(FECHAS, PSICOLOGIA, "Rodearte de apoyo ese día suele ayudar más que intentar atravesarlo en soledad."),
        reen(FECHAS, PSICOLOGIA, "Permitir sentir lo que surja ese día, sin forzarte a estar bien, facilita procesarlo mejor."),
        reen(FECHAS, PSICOLOGIA, "El impacto de una fecha suele disminuir con las siguientes veces que se repite."),

        acc(FECHAS, "Planifica con antelación algo para ese día que te dé cierta estructura y compañía."),
        acc(FECHAS, "Avisa a alguien de confianza de que esa fecha se acerca y que te gustaría contar con apoyo."),
        acc(FECHAS, "Escribe en tu diario lo que esperas sentir ese día, para no llegar del todo desprevenido/a."),
        acc(FECHAS, "Prepara una actividad alternativa a lo que solías hacer ese día en la relación."),
        acc(FECHAS, "Permítete sentir lo que surja ese día, sin exigirte estar bien todo el tiempo."),
        acc(FECHAS, "Si el día resulta muy duro, ten a mano el contacto de alguien a quien puedas llamar."),

        // ================= OBJETOS_RECUERDOS =================
        reco(OBJETOS, "Encontrarte con objetos que os recuerdan puede remover emociones de golpe, sin previo aviso."),
        reco(OBJETOS, "Decidir qué hacer con fotos, regalos o recuerdos es parte del proceso, no un trámite menor."),
        reco(OBJETOS, "No hay una única forma correcta de manejar los objetos que quedan de una relación."),
        reco(OBJETOS, "Guardar o soltar un objeto no tiene que decidirse todo de una vez."),
        reco(OBJETOS, "Un objeto puede doler hoy y sentirse neutro más adelante; el significado cambia con el tiempo."),

        reen(OBJETOS, ESTOICO, "El objeto en sí no tiene poder sobre ti; el significado que le das es lo que puedes examinar."),
        reen(OBJETOS, ESTOICO, "Puedes decidir con calma qué hacer con cada objeto, sin necesidad de resolverlo todo hoy."),
        reen(OBJETOS, ESTOICO, "Guardar algo no te ata al pasado si tú decides qué lugar le das en tu vida."),
        reen(OBJETOS, ESTOICO, "Soltar un objeto es un acto simbólico; lo importante es la decisión interna que lo acompaña."),
        reen(OBJETOS, ESTOICO, "No hace falta una decisión perfecta, solo una decisión que puedas sostener hoy."),
        reen(OBJETOS, ESTOICO, "El valor de un recuerdo no depende de si conservas o no el objeto físico."),

        reen(OBJETOS, CATOLICO, "Puedes bendecir este recuerdo y dejarlo ir con paz, sin que eso borre lo vivido."),
        reen(OBJETOS, CATOLICO, "No tienes que decidir hoy qué hacer con cada objeto; puedes darte tiempo."),
        reen(OBJETOS, CATOLICO, "Guardar un recuerdo con gratitud, en vez de con apego, también es una forma de sanar."),
        reen(OBJETOS, CATOLICO, "Puedes pedir paz para saber qué conservar y qué soltar, a tu propio ritmo."),
        reen(OBJETOS, CATOLICO, "Un objeto guardado con intención distinta puede dejar de doler con el tiempo."),
        reen(OBJETOS, CATOLICO, "Soltar algo material también puede ser un gesto de confianza en lo que viene."),

        reen(OBJETOS, PSICOLOGIA, "Los objetos asociados a la relación pueden actuar como disparadores emocionales intensos."),
        reen(OBJETOS, PSICOLOGIA, "No hay una regla única sobre guardar o tirar recuerdos; lo importante es que la decisión te ayude a ti."),
        reen(OBJETOS, PSICOLOGIA, "Posponer una decisión difícil sobre un objeto no es evitación si te da tiempo para procesarlo."),
        reen(OBJETOS, PSICOLOGIA, "Guardar algunos objetos y soltar otros es una estrategia tan válida como cualquier otra."),
        reen(OBJETOS, PSICOLOGIA, "El malestar al ver un objeto suele disminuir con el tiempo, aunque hoy sea intenso."),
        reen(OBJETOS, PSICOLOGIA, "Decidir con calma, no con impulso, suele dar decisiones más sostenibles sobre estos objetos."),

        acc(OBJETOS, "Elige un solo objeto hoy y decide con calma si lo guardas, lo guardas fuera de la vista o lo sueltas."),
        acc(OBJETOS, "Si decides guardar algo, ponlo en un lugar que no veas todos los días."),
        acc(OBJETOS, "Pide ayuda a alguien de confianza si necesitas compañía para ordenar estos objetos."),
        acc(OBJETOS, "No tomes decisiones sobre todos los objetos de golpe; hazlo poco a poco, en varios días."),
        acc(OBJETOS, "Si un objeto te resulta muy doloroso ahora, guárdalo fuera de la vista sin decidir aún qué hacer con él."),
        acc(OBJETOS, "Respira despacio antes de tocar estos objetos, para afrontarlo con más calma.")
    )
}

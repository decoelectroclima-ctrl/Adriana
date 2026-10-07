package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.data.SoltarFramework

/**
 * Contenido del grupo EMOCIONES (D3, parte 1 de 2): CULPA_RENCOR_RABIA, SOLEDAD_VACIO, AUTOESTIMA_RECHAZO.
 */
object ContentDatabaseEmociones1 {

    private val CULPA = setOf(ClinicalCategory.CULPA_RENCOR_RABIA)
    private val SOLEDAD = setOf(ClinicalCategory.SOLEDAD_VACIO)
    private val AUTOESTIMA = setOf(ClinicalCategory.AUTOESTIMA_RECHAZO)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)
    private val NOCHE = AtomContext(timeOfDay = TimeOfDay.NOCHE)

    private fun reco(tags: Set<ClinicalCategory>, text: String) = atom(Slot.RECONOCIMIENTO, text, tags = tags)
    private fun reen(tags: Set<ClinicalCategory>, fw: Set<SoltarFramework>, text: String, ctx: AtomContext = AtomContext()) =
        atom(Slot.REENCUADRE, text, tags = tags, frameworks = fw, context = ctx)
    private fun acc(tags: Set<ClinicalCategory>, text: String) = atom(Slot.ACCION, text, tags = tags)

    val atoms: List<ContentAtom> = listOf(

        // ================= CULPA_RENCOR_RABIA =================
        reco(CULPA, "La rabia y el rencor son también formas de dolor que aún buscan un lugar donde asentarse."),
        reco(CULPA, "Sentir culpa por cosas que hiciste o no hiciste es habitual después de una ruptura importante."),
        reco(CULPA, "El rencor a veces se siente como protección, aunque termine pesando más de lo que protege."),
        reco(CULPA, "No es raro que la culpa y la rabia aparezcan juntas, mezcladas con el mismo dolor."),
        reco(CULPA, "Sentir estas emociones no te hace peor persona; forman parte del proceso de soltar."),

        reen(CULPA, ESTOICO, "El rencor sostenido no daña a la otra persona, te ocupa a ti; examinarlo es un acto de libertad."),
        reen(CULPA, ESTOICO, "No depende de ti lo que ya ocurrió; sí depende de ti cuánto espacio le sigues dando en tu mente."),
        reen(CULPA, ESTOICO, "La culpa útil corrige el rumbo; la culpa que solo castiga no construye nada."),
        reen(CULPA, ESTOICO, "Puedes reconocer un error sin flagelarte indefinidamente por él."),
        reen(CULPA, ESTOICO, "Soltar el rencor no es aprobar lo ocurrido, es dejar de cargarlo tú."),
        reen(CULPA, ESTOICO, "El buen juicio distingue entre aprender del pasado y quedarte atrapado/a en él."),

        reen(CULPA, CATOLICO, "El perdón no es un acto único, es un camino que puedes recorrer a tu propio ritmo."),
        reen(CULPA, CATOLICO, "Puedes entregar esta rabia en oración, en lugar de dejar que se instale en tu corazón."),
        reen(CULPA, CATOLICO, "La misericordia hacia ti mismo/a también es parte de sanar la culpa."),
        reen(CULPA, CATOLICO, "No tienes que resolver hoy todo el rencor; basta con dar un paso pequeño hacia soltarlo."),
        reen(CULPA, CATOLICO, "Dios conoce tu corazón herido; puedes descansar en esa certeza mientras procesas esto."),
        reen(CULPA, CATOLICO, "El perdón no borra lo ocurrido, pero te libera del peso de cargarlo para siempre."),

        reen(CULPA, PSICOLOGIA, "La rabia y la culpa suelen ser capas de un mismo dolor no resuelto todavía."),
        reen(CULPA, PSICOLOGIA, "Sostener el rencor mantiene activado el sistema de estrés más tiempo del necesario."),
        reen(CULPA, PSICOLOGIA, "Nombrar la emoción exacta (rabia, culpa, tristeza) ayuda a procesarla mejor que mezclarla todo junto."),
        reen(CULPA, PSICOLOGIA, "El perdón, cuando llega, suele ser un proceso gradual, no una decisión de un solo momento."),
        reen(CULPA, PSICOLOGIA, "La autocompasión reduce la culpa excesiva mejor que la autocrítica severa."),
        reen(CULPA, PSICOLOGIA, "Expresar la rabia de forma segura (escribir, hablar) ayuda más que reprimirla o actuarla impulsivamente."),

        acc(CULPA, "Escribe una carta que no vas a enviar, diciendo todo lo que sientes sin filtrar."),
        acc(CULPA, "Haz ejercicio físico breve para soltar la activación que trae la rabia."),
        acc(CULPA, "Habla con alguien de confianza sobre lo que sientes, sin buscar que te dé la razón."),
        acc(CULPA, "Anota una cosa concreta que puedas perdonarte a ti mismo/a hoy, aunque sea pequeña."),
        acc(CULPA, "Respira despacio varias veces mientras nombras en voz baja qué emoción sientes exactamente."),
        acc(CULPA, "Escribe qué necesitarías para empezar a soltar este rencor, sin exigirte hacerlo todo hoy."),

        // ================= SOLEDAD_VACIO =================
        reco(SOLEDAD, "El vacío que sientes ahora es real, aunque con el tiempo suele cambiar de forma."),
        reco(SOLEDAD, "Sentirte solo/a, aunque estés rodeado/a de gente, es una experiencia común en este proceso."),
        reco(SOLEDAD, "El silencio de la casa o del día puede sentirse más pesado después de una ruptura."),
        reco(SOLEDAD, "El vacío no significa que algo falte en ti, sino que algo importante cambió en tu vida."),
        reco(SOLEDAD, "Sentir soledad no es una señal de debilidad, es una respuesta natural a una pérdida."),

        reen(SOLEDAD, ESTOICO, "No depende de ti que el vacío desaparezca hoy; sí depende de ti cómo ocupas este tiempo."),
        reen(SOLEDAD, ESTOICO, "La soledad puede ser también un espacio para conocerte mejor, no solo una carencia."),
        reen(SOLEDAD, ESTOICO, "Puedes sostener este vacío sin huir de él ni dejarte arrastrar por él."),
        reen(SOLEDAD, ESTOICO, "El silencio no tiene por qué ser enemigo; a veces es solo espacio sin llenar todavía."),
        reen(SOLEDAD, ESTOICO, "Construir una vida con sentido propio es un ejercicio de fortaleza, no un castigo."),
        reen(SOLEDAD, ESTOICO, "El vacío de hoy no define tu capacidad futura de sentirte acompañado/a."),

        reen(SOLEDAD, CATOLICO, "No estás verdaderamente solo/a, aunque el vacío lo haga sentir así."),
        reen(SOLEDAD, CATOLICO, "Puedes entregar este vacío en oración y permitir que se convierta en espacio de encuentro."),
        reen(SOLEDAD, CATOLICO, "La soledad también puede ser tiempo de escucha interior, no solo de ausencia."),
        reen(SOLEDAD, CATOLICO, "Confiar no borra el vacío, pero te acompaña mientras lo atraviesas."),
        reen(SOLEDAD, CATOLICO, "Puedes buscar comunidad, oración o compañía sencilla para sostener este tiempo."),
        reen(SOLEDAD, CATOLICO, "El vacío que sientes hoy no es el final de tu historia, es una etapa dentro de ella."),

        reen(SOLEDAD, PSICOLOGIA, "El vacío emocional después de una ruptura es una respuesta esperable, ligada al apego que se rompió."),
        reen(SOLEDAD, PSICOLOGIA, "La soledad y el aislamiento no son lo mismo; puedes sentir soledad incluso con gente cerca."),
        reen(SOLEDAD, PSICOLOGIA, "Construir pequeños momentos de conexión ayuda a suavizar el vacío con el tiempo."),
        reen(SOLEDAD, PSICOLOGIA, "El vacío suele ser más intenso al principio y tiende a modificarse con nuevas rutinas."),
        reen(SOLEDAD, PSICOLOGIA, "Nombrar el vacío como una emoción, no como un hecho permanente, ayuda a sostenerlo mejor."),
        reen(SOLEDAD, PSICOLOGIA, "Cultivar otras relaciones no reemplaza lo perdido, pero sí construye una base distinta."),

        acc(SOLEDAD, "Llama o escribe a alguien de tu red de apoyo, aunque sea un mensaje breve."),
        acc(SOLEDAD, "Sal a un lugar con gente, aunque no necesites hablar con nadie, solo para no estar en el vacío de casa."),
        acc(SOLEDAD, "Haz una actividad que disfrutes hacer sola o solo, para reconectar contigo mismo/a."),
        acc(SOLEDAD, "Escribe en tu diario qué tipo de compañía echas de menos exactamente."),
        acc(SOLEDAD, "Apúntate a una actividad grupal, aunque sea pequeña, para ampliar tu red poco a poco."),
        acc(SOLEDAD, "Dedica un rato a cuidar una planta, una mascota o un espacio de tu casa, como gesto de presencia."),

        // ================= AUTOESTIMA_RECHAZO =================
        reco(AUTOESTIMA, "Sentirte poco valioso/a después de una ruptura es común, aunque no sea un reflejo real de tu valor."),
        reco(AUTOESTIMA, "El rechazo duele, pero no define lo que vales como persona."),
        reco(AUTOESTIMA, "Que una relación terminara no significa que no seas digno/a de ser querido/a."),
        reco(AUTOESTIMA, "El dolor del rechazo puede activar dudas antiguas sobre tu propio valor."),
        reco(AUTOESTIMA, "Cuestionar tu valía en este momento es parte del dolor, no una verdad sobre ti."),

        reen(AUTOESTIMA, ESTOICO, "Tu valor no depende de la opinión o decisión de otra persona sobre ti."),
        reen(AUTOESTIMA, ESTOICO, "El juicio de que no vales nada es una interpretación, no un hecho verificable."),
        reen(AUTOESTIMA, ESTOICO, "Puedes examinar este pensamiento con la misma calma con la que examinarías cualquier otro juicio."),
        reen(AUTOESTIMA, ESTOICO, "Lo que otra persona decida sobre la relación no depende de ti; tu valor tampoco depende de esa decisión."),
        reen(AUTOESTIMA, ESTOICO, "Construir tu carácter día a día es lo que sí está en tu mano, más allá del rechazo vivido."),
        reen(AUTOESTIMA, ESTOICO, "El rechazo habla de una relación que no encajaba, no de tu valor como persona."),

        reen(AUTOESTIMA, CATOLICO, "Tu dignidad no depende de que alguien te haya elegido o dejado de elegir."),
        reen(AUTOESTIMA, CATOLICO, "Eres valioso/a más allá de cualquier relación humana, por lo que eres, no por lo que otros decidan."),
        reen(AUTOESTIMA, CATOLICO, "Puedes pedir que este dolor no se convierta en una herida sobre tu propio valor."),
        reen(AUTOESTIMA, CATOLICO, "El rechazo de una persona no cambia el amor incondicional que sostiene tu existencia."),
        reen(AUTOESTIMA, CATOLICO, "Puedes recordar, aunque hoy cueste creerlo, que tu valor no se mide por esa relación."),
        reen(AUTOESTIMA, CATOLICO, "Confiar en tu propia dignidad es también un acto de fe en medio del dolor."),

        reen(AUTOESTIMA, PSICOLOGIA, "El rechazo activa circuitos cerebrales similares al dolor físico; el malestar es real, no exagerado."),
        reen(AUTOESTIMA, PSICOLOGIA, "La autoestima puede verse afectada temporalmente sin que eso signifique un cambio permanente en tu valía."),
        reen(AUTOESTIMA, PSICOLOGIA, "Separar el hecho de que terminó la relación del juicio de que no vales ayuda a procesar mejor el dolor."),
        reen(AUTOESTIMA, PSICOLOGIA, "Reforzar otras áreas de tu vida (trabajo, amistades, aficiones) ayuda a sostener una autoestima más estable."),
        reen(AUTOESTIMA, PSICOLOGIA, "Tratarte con la misma compasión que tratarías a alguien querido en tu situación mejora cómo atraviesas este momento."),
        reen(AUTOESTIMA, PSICOLOGIA, "El rechazo de una persona no es un veredicto sobre tu valor general como persona."),

        acc(AUTOESTIMA, "Escribe tres cualidades tuyas que no dependen de esa relación."),
        acc(AUTOESTIMA, "Haz algo hoy en lo que te sientas competente o capaz, por pequeño que sea."),
        acc(AUTOESTIMA, "Habla con alguien que te conozca bien y te recuerde quién eres más allá de esta ruptura."),
        acc(AUTOESTIMA, "Anota un logro reciente, aunque sea pequeño, que no tenga relación con esa persona."),
        acc(AUTOESTIMA, "Escribe una frase amable hacia ti mismo/a y repítela cuando aparezca la duda sobre tu valor."),
        acc(AUTOESTIMA, "Dedica un rato a una actividad que disfrutes y que te recuerde partes de ti que valoras.")
    )
}

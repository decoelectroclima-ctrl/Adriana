package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.data.SoltarFramework

/**
 * Contenido del grupo MENTE (D3, parte 2 de 3): AMBIVALENCIA_EMOCIONAL, METAPREGUNTAS_PROCESO, ESTANCAMIENTO_PROCESO, MIEDO_FUTURO_SOLEDAD.
 */
object ContentDatabaseMente2 {

    private val AMBIVALENCIA = setOf(ClinicalCategory.AMBIVALENCIA_EMOCIONAL)
    private val METAPREGUNTAS = setOf(ClinicalCategory.METAPREGUNTAS_PROCESO)
    private val ESTANCAMIENTO = setOf(ClinicalCategory.ESTANCAMIENTO_PROCESO)
    private val MIEDO = setOf(ClinicalCategory.MIEDO_FUTURO_SOLEDAD)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)
    private val PRIMEROS = AtomContext(phase = TimePhase.PRIMEROS_DIAS)
    private val TRAS_UN_MES = AtomContext(phase = TimePhase.TRAS_UN_MES)

    private fun reco(tags: Set<ClinicalCategory>, text: String) = atom(Slot.RECONOCIMIENTO, text, tags = tags)
    private fun reen(tags: Set<ClinicalCategory>, fw: Set<SoltarFramework>, text: String, ctx: AtomContext = AtomContext()) =
        atom(Slot.REENCUADRE, text, tags = tags, frameworks = fw, context = ctx)
    private fun acc(tags: Set<ClinicalCategory>, text: String) = atom(Slot.ACCION, text, tags = tags)

    val atoms: List<ContentAtom> = listOf(

        // ================= AMBIVALENCIA_EMOCIONAL =================
        reco(AMBIVALENCIA, "Sentir alivio y tristeza a la vez no es contradictorio; ambas cosas pueden ser ciertas al mismo tiempo."),
        reco(AMBIVALENCIA, "Extrañar y sentir alivio de no estar en esa relación pueden convivir sin anularse."),
        reco(AMBIVALENCIA, "No hace falta elegir una sola emoción; el proceso suele traer varias a la vez."),
        reco(AMBIVALENCIA, "Sentir cosas opuestas sobre lo mismo es parte habitual de un proceso de este tipo."),
        reco(AMBIVALENCIA, "La confusión emocional no significa que algo vaya mal en cómo lo estás llevando."),

        reen(AMBIVALENCIA, ESTOICO, "No necesitas resolver hoy la contradicción; puedes observarla sin exigirte una única respuesta."),
        reen(AMBIVALENCIA, ESTOICO, "Dos emociones opuestas pueden convivir; lo que depende de ti es no dejar que te paralicen."),
        reen(AMBIVALENCIA, ESTOICO, "Aceptar la mezcla de sentimientos sin pelear contra ella es también un ejercicio de serenidad."),
        reen(AMBIVALENCIA, ESTOICO, "La claridad no siempre llega de golpe; a veces se construye con el paso del tiempo."),
        reen(AMBIVALENCIA, ESTOICO, "Puedes actuar con coherencia aunque por dentro sientas cosas distintas."),
        reen(AMBIVALENCIA, ESTOICO, "No juzgues la ambivalencia como debilidad; a menudo es solo honestidad con lo complejo."),

        reen(AMBIVALENCIA, CATOLICO, "Puedes sostener sentimientos encontrados y aun así confiar en que el camino se irá aclarando."),
        reen(AMBIVALENCIA, CATOLICO, "No hace falta entenderlo todo hoy; puedes entregar esta confusión y seguir caminando con paciencia."),
        reen(AMBIVALENCIA, CATOLICO, "El corazón dividido también merece ternura, no exigencia de claridad inmediata."),
        reen(AMBIVALENCIA, CATOLICO, "Confiar no significa no sentir contradicciones, sino no quedarte paralizado/a por ellas."),
        reen(AMBIVALENCIA, CATOLICO, "Puedes pedir paz para sostener lo que sientes, aunque sea contradictorio por ahora."),
        reen(AMBIVALENCIA, CATOLICO, "Dos emociones distintas no dividen tu valor como persona."),

        reen(AMBIVALENCIA, PSICOLOGIA, "Sentir emociones contradictorias a la vez es normal y tiene una explicación: distintas partes del proceso avanzan a ritmos diferentes."),
        reen(AMBIVALENCIA, PSICOLOGIA, "No hace falta resolver la ambivalencia para seguir funcionando en el día a día."),
        reen(AMBIVALENCIA, PSICOLOGIA, "Nombrar ambas emociones por separado ayuda a entenderlas mejor que intentar fundirlas en una sola."),
        reen(AMBIVALENCIA, PSICOLOGIA, "La ambivalencia suele reducirse con el tiempo, no forzándola a desaparecer."),
        reen(AMBIVALENCIA, PSICOLOGIA, "Aceptar la confusión como parte del proceso reduce la ansiedad de intentar resolverla ya."),
        reen(AMBIVALENCIA, PSICOLOGIA, "No es indecisión, es que el cambio emocional lleva su propio tiempo."),

        acc(AMBIVALENCIA, "Escribe en una columna lo que sientes de alivio y en otra lo que sientes de tristeza, sin mezclarlas."),
        acc(AMBIVALENCIA, "Habla con alguien de confianza sobre esta mezcla de emociones, sin buscar que la resuelva por ti."),
        acc(AMBIVALENCIA, "Date permiso para sentir ambas cosas hoy, sin elegir una como la única válida."),
        acc(AMBIVALENCIA, "Haz algo que te calme el cuerpo mientras la mente sigue procesando la mezcla de emociones."),
        acc(AMBIVALENCIA, "Anota en tu diario cómo cambia esta mezcla de un día a otro, sin buscar una conclusión todavía."),
        acc(AMBIVALENCIA, "Respira despacio unos minutos y permite que ambas emociones estén presentes sin pelear entre ellas."),

        // ================= METAPREGUNTAS_PROCESO =================
        reco(METAPREGUNTAS, "Preguntarte si vas bien o cuánto falta es parte de querer entender un proceso que no tiene un mapa claro."),
        reco(METAPREGUNTAS, "No tener una respuesta exacta sobre cuánto falta no significa que no estés avanzando."),
        reco(METAPREGUNTAS, "Medir el propio proceso es difícil porque no avanza en línea recta."),
        reco(METAPREGUNTAS, "Querer una certeza sobre el ritmo del proceso es comprensible, aunque rara vez exista esa certeza."),
        reco(METAPREGUNTAS, "Preguntarte constantemente si vas bien puede convertirse en otra forma de rumiación."),

        reen(METAPREGUNTAS, ESTOICO, "No depende de ti controlar el ritmo exacto del proceso; sí depende de ti cómo actúas cada día."),
        reen(METAPREGUNTAS, ESTOICO, "Medir el progreso día a día suele generar más ansiedad que claridad real."),
        reen(METAPREGUNTAS, ESTOICO, "El buen juicio se fija en las acciones de hoy, no en un cronómetro imaginario."),
        reen(METAPREGUNTAS, ESTOICO, "Preguntarte constantemente cuánto falta no acelera el proceso, solo lo vuelve más pesado."),
        reen(METAPREGUNTAS, ESTOICO, "Puedes soltar la necesidad de un plazo exacto y centrarte en lo que sí controlas hoy."),
        reen(METAPREGUNTAS, ESTOICO, "El progreso se mide mejor en el conjunto de semanas que en la comparación de un día con otro."),

        reen(METAPREGUNTAS, CATOLICO, "No necesitas saber el plazo exacto; puedes confiar en el proceso sin tener todas las respuestas."),
        reen(METAPREGUNTAS, CATOLICO, "Cada etapa tiene su tiempo, aunque no puedas verlo con claridad ahora."),
        reen(METAPREGUNTAS, CATOLICO, "Puedes pedir paciencia para sostener la incertidumbre de no saber cuánto falta."),
        reen(METAPREGUNTAS, CATOLICO, "La confianza no exige un mapa exacto del camino, solo dar el paso de hoy."),
        reen(METAPREGUNTAS, CATOLICO, "Preguntarte tanto por el resultado puede alejarte de vivir el hoy con más paz."),
        reen(METAPREGUNTAS, CATOLICO, "Puedes soltar la exigencia de un plazo y confiar en que el proceso sigue su curso."),

        reen(METAPREGUNTAS, PSICOLOGIA, "Los procesos de duelo y recuperación no siguen un cronograma fijo ni lineal."),
        reen(METAPREGUNTAS, PSICOLOGIA, "Preguntarte constantemente si vas bien puede convertirse en una forma de ansiedad sobre el proceso."),
        reen(METAPREGUNTAS, PSICOLOGIA, "Los indicadores de progreso suelen verse mejor en semanas o meses, no de un día para otro."),
        reen(METAPREGUNTAS, PSICOLOGIA, "Comparar tu proceso con el de otras personas rara vez da una medida útil."),
        reen(METAPREGUNTAS, PSICOLOGIA, "Registrar hechos concretos (qué hiciste, cómo dormiste) da una medida más fiable que la sensación del momento."),
        reen(METAPREGUNTAS, PSICOLOGIA, "Soltar la necesidad de certeza sobre el plazo suele reducir la ansiedad asociada al proceso."),

        acc(METAPREGUNTAS, "Anota tres cosas concretas que hayas conseguido sostener esta semana, por pequeñas que sean."),
        acc(METAPREGUNTAS, "Revisa tu diario de hace unas semanas y compara con cómo te sientes hoy, sin buscar una conclusión definitiva."),
        acc(METAPREGUNTAS, "Escribe qué necesitarías sentir para saber que vas bien, y si eso es realmente medible."),
        acc(METAPREGUNTAS, "Haz una actividad concreta hoy en lugar de seguir preguntándote por el conjunto del proceso."),
        acc(METAPREGUNTAS, "Habla con alguien de confianza sobre cómo ves tu propio avance, sin buscar que te dé un plazo."),
        acc(METAPREGUNTAS, "Elige una sola cosa pequeña que puedas hacer hoy, sin pensar en el proceso completo."),

        // ================= ESTANCAMIENTO_PROCESO =================
        reco(ESTANCAMIENTO, "Sentir que no avanzas no significa que el proceso se haya detenido del todo."),
        reco(ESTANCAMIENTO, "Hay etapas del proceso que se sienten más planas, aunque algo siga moviéndose por dentro."),
        reco(ESTANCAMIENTO, "El estancamiento percibido a veces es solo una meseta antes de otro paso."),
        reco(ESTANCAMIENTO, "No sentir avances visibles no invalida el trabajo que ya has hecho."),
        reco(ESTANCAMIENTO, "Sentirte estancado/a puede convivir con cambios que aún no notas del todo."),

        reen(ESTANCAMIENTO, ESTOICO, "No depende de ti sentir que avanzas cada día; sí depende de ti seguir actuando con constancia."),
        reen(ESTANCAMIENTO, ESTOICO, "Una meseta no es un retroceso; es parte del mismo camino visto de cerca."),
        reen(ESTANCAMIENTO, ESTOICO, "El progreso constante rara vez se siente como progreso mientras ocurre.", TRAS_UN_MES),
        reen(ESTANCAMIENTO, ESTOICO, "Puedes revisar tus acciones de la semana en lugar de juzgar solo por cómo te sientes hoy."),
        reen(ESTANCAMIENTO, ESTOICO, "Mantener el hábito, aunque no se sienta avance, es en sí mismo un acto de disciplina."),
        reen(ESTANCAMIENTO, ESTOICO, "No juzgues el conjunto del camino por la sensación de un solo día."),

        reen(ESTANCAMIENTO, CATOLICO, "Los tiempos de espera también forman parte del camino, aunque no se vean frutos todavía."),
        reen(ESTANCAMIENTO, CATOLICO, "Confiar incluye aceptar etapas donde no se siente avance visible."),
        reen(ESTANCAMIENTO, CATOLICO, "Puedes pedir paciencia para sostener este momento sin exigirte resultados inmediatos."),
        reen(ESTANCAMIENTO, CATOLICO, "Lo que parece estancado a veces está madurando por dentro, aunque no se note por fuera.", TRAS_UN_MES),
        reen(ESTANCAMIENTO, CATOLICO, "No tienes que sentir avance cada día para seguir confiando en el proceso."),
        reen(ESTANCAMIENTO, CATOLICO, "Puedes descansar en la certeza de que sigues caminando, aunque hoy no lo sientas."),

        reen(ESTANCAMIENTO, PSICOLOGIA, "El progreso emocional no es lineal; suele incluir mesetas donde parece que nada cambia."),
        reen(ESTANCAMIENTO, PSICOLOGIA, "Muchos cambios se consolidan de forma poco visible antes de notarse hacia fuera.", TRAS_UN_MES),
        reen(ESTANCAMIENTO, PSICOLOGIA, "Comparar el hoy con hace unas semanas suele mostrar más avance del que se siente día a día."),
        reen(ESTANCAMIENTO, PSICOLOGIA, "Sentirte estancado/a puede ser señal de que toca ajustar algo, no de que el proceso haya fallado."),
        reen(ESTANCAMIENTO, PSICOLOGIA, "El cerebro tiende a notar más los retrocesos que los avances graduales."),
        reen(ESTANCAMIENTO, PSICOLOGIA, "Mantener las rutinas de cuidado durante una meseta suele preceder a un nuevo avance."),

        acc(ESTANCAMIENTO, "Revisa tu diario de hace un mes y compara con cómo estás hoy, buscando cambios concretos."),
        acc(ESTANCAMIENTO, "Anota una sola cosa pequeña que hagas hoy distinta a como la hacías hace unas semanas."),
        acc(ESTANCAMIENTO, "Habla con alguien de confianza sobre esta sensación de estancamiento."),
        acc(ESTANCAMIENTO, "Prueba una actividad nueva esta semana, aunque sea pequeña, para romper la rutina."),
        acc(ESTANCAMIENTO, "Escribe qué rutinas de cuidado sigues sosteniendo, aunque no sientas avance."),
        acc(ESTANCAMIENTO, "Date una semana más antes de sacar conclusiones sobre si el proceso avanza o no."),

        // ================= MIEDO_FUTURO_SOLEDAD =================
        reco(MIEDO, "Pensar en un futuro en soledad puede generar miedo, aunque ese futuro aún no esté escrito."),
        reco(MIEDO, "El miedo a quedarte solo/a suele aparecer con fuerza en los primeros tiempos de un proceso así."),
        reco(MIEDO, "Imaginar la soledad futura no es lo mismo que estar viviéndola ahora."),
        reco(MIEDO, "El miedo al futuro suele exagerar lo que realmente puede pasar."),
        reco(MIEDO, "Sentir temor sobre estar solo/a no significa que esa sea la única posibilidad por delante."),

        reen(MIEDO, ESTOICO, "El futuro no depende de ti del todo; lo que sí depende de ti es cómo construyes el presente."),
        reen(MIEDO, ESTOICO, "Temer algo que aún no ha ocurrido gasta energía que podrías usar en lo que hoy sí puedes hacer."),
        reen(MIEDO, ESTOICO, "La soledad imaginada suele ser peor que la soledad real cuando llega."),
        reen(MIEDO, ESTOICO, "Puedes prepararte para el futuro sin necesidad de vivirlo ya en tu cabeza."),
        reen(MIEDO, ESTOICO, "El buen juicio distingue entre precaución razonable y miedo que paraliza."),
        reen(MIEDO, ESTOICO, "Construir vínculos y hábitos hoy es lo que sí está en tu mano frente al miedo al mañana."),

        reen(MIEDO, CATOLICO, "No estás solo/a en este camino, aunque el miedo lo haga sentir así."),
        reen(MIEDO, CATOLICO, "Puedes entregar este temor en oración y descansar de cargarlo tú solo/a."),
        reen(MIEDO, CATOLICO, "Confiar no borra el miedo, pero te ayuda a no quedarte paralizado/a por él."),
        reen(MIEDO, CATOLICO, "El futuro está en manos más grandes que tu sola capacidad de controlarlo."),
        reen(MIEDO, CATOLICO, "Puedes pedir compañía, tanto humana como espiritual, para no sentir que enfrentas esto en soledad."),
        reen(MIEDO, CATOLICO, "El miedo al futuro no tiene por qué decidir cómo vives el día de hoy."),

        reen(MIEDO, PSICOLOGIA, "El miedo anticipatorio suele exagerar los escenarios negativos más de lo que la realidad después confirma."),
        reen(MIEDO, PSICOLOGIA, "Este miedo suele ser más intenso al principio del proceso y tiende a suavizarse con el tiempo.", PRIMEROS),
        reen(MIEDO, PSICOLOGIA, "Construir apoyos concretos (amistades, rutinas) reduce el miedo mejor que solo pensarlo."),
        reen(MIEDO, PSICOLOGIA, "Diferenciar el miedo real de la anticipación catastrófica ayuda a manejarlo mejor."),
        reen(MIEDO, PSICOLOGIA, "El cerebro tiende a imaginar el peor escenario cuando hay incertidumbre; no es una predicción fiable."),
        reen(MIEDO, PSICOLOGIA, "Centrarte en pasos concretos de hoy reduce la sensación de amenaza sobre un futuro incierto."),

        acc(MIEDO, "Escribe qué apoyos reales tienes hoy, aunque el miedo diga que estás solo/a."),
        acc(MIEDO, "Contacta a alguien de tu red de apoyo esta semana, aunque sea con un mensaje breve."),
        acc(MIEDO, "Anota tres cosas concretas que podrías hacer si la soledad llegara, en vez de solo temerla."),
        acc(MIEDO, "Dedica un rato a una actividad que fortalezca un vínculo que ya tienes."),
        acc(MIEDO, "Respira despacio y pregúntate si el miedo habla del futuro real o de un escenario imaginado."),
        acc(MIEDO, "Apúntate a algo nuevo, por pequeño que sea, que te conecte con otras personas.")
    )
}

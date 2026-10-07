package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.data.SoltarFramework

/** Contenido del grupo CUERPO (D2): ANSIEDAD_SOMATICA, SINTOMAS_FISICOS, INSOMNIO_NOCHE. */
object ContentDatabaseCuerpo {

    private val ANSIEDAD = setOf(ClinicalCategory.ANSIEDAD_SOMATICA)
    private val SINTOMAS = setOf(ClinicalCategory.SINTOMAS_FISICOS)
    private val INSOMNIO = setOf(ClinicalCategory.INSOMNIO_NOCHE)
    private val CUERPO = setOf(ContentGroup.CUERPO)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)
    private val REPETIDO = AtomContext(repeated = true)
    private val NOCHE = AtomContext(timeOfDay = TimeOfDay.NOCHE)
    private val MADRUGADA = AtomContext(timeOfDay = TimeOfDay.MADRUGADA)
    private val PRIMEROS = AtomContext(phase = TimePhase.PRIMEROS_DIAS)

    private fun reco(tags: Set<ClinicalCategory>, text: String) = atom(Slot.RECONOCIMIENTO, text, tags = tags)
    private fun reen(tags: Set<ClinicalCategory>, fw: Set<SoltarFramework>, text: String, ctx: AtomContext = AtomContext()) =
        atom(Slot.REENCUADRE, text, tags = tags, frameworks = fw, context = ctx)
    private fun acc(tags: Set<ClinicalCategory>, text: String) = atom(Slot.ACCION, text, tags = tags)
    private fun cierre(fw: Set<SoltarFramework>, text: String) = atom(Slot.CIERRE, text, groups = CUERPO, frameworks = fw)
    private fun cierreRep(text: String) = atom(Slot.CIERRE, text, groups = CUERPO, context = REPETIDO)
    private fun cita(fw: Set<SoltarFramework>, text: String, author: String, source: String = "") =
        atom(Slot.CITA, text, groups = CUERPO, frameworks = fw, author = author, source = source)

    val atoms: List<ContentAtom> = listOf(

        // ================= ANSIEDAD_SOMATICA =================
        reco(ANSIEDAD, "El pecho apretado y el pulso acelerado son ansiedad tomando forma en el cuerpo."),
        reco(ANSIEDAD, "Ese nudo en el estómago no significa que algo vaya mal ahora mismo; es alarma sin peligro real delante."),
        reco(ANSIEDAD, "La respiración corta es una señal del cuerpo, no una orden que debas obedecer sin más."),
        reco(ANSIEDAD, "Notar el cuerpo tenso es el primer paso para poder soltarlo poco a poco."),
        reco(ANSIEDAD, "El cuerpo reacciona antes que la mente entienda del todo lo que pasa."),

        reen(ANSIEDAD, ESTOICO, "El cuerpo se agita, pero tu juicio sobre lo que significa esa agitación sigue siendo tuyo para examinar."),
        reen(ANSIEDAD, ESTOICO, "No depende de ti sentir el pulso acelerado ahora; sí depende de ti qué haces mientras dura."),
        reen(ANSIEDAD, ESTOICO, "Una sensación intensa no es una prueba de peligro real, solo una señal a observar sin prisa."),
        reen(ANSIEDAD, ESTOICO, "Entrenar la calma es dejar que el cuerpo se agite sin que decida por ti lo que haces después."),
        reen(ANSIEDAD, ESTOICO, "Puedes distinguir entre la sensación en el pecho y la historia que le añades encima."),
        reen(ANSIEDAD, ESTOICO, "Respirar con orden es una forma pequeña de ejercer lo que sí controlas en este momento."),

        reen(ANSIEDAD, CATOLICO, "Tu cuerpo también merece cuidado; llevar la ansiedad a un instante de silencio no la resuelve, pero la acompaña."),
        reen(ANSIEDAD, CATOLICO, "No tienes que entenderlo todo ahora; basta con sostener este momento con calma."),
        reen(ANSIEDAD, CATOLICO, "Confiar no borra el temblor del cuerpo, pero te sostiene mientras pasa."),
        reen(ANSIEDAD, CATOLICO, "Puedes ofrecer esta inquietud en lugar de pelear a solas contra ella."),
        reen(ANSIEDAD, CATOLICO, "Cuidar tu cuerpo hoy también es una forma de cuidar tu alma."),
        reen(ANSIEDAD, CATOLICO, "La paz no siempre llega de golpe; a veces se construye respiración a respiración."),

        reen(ANSIEDAD, PSICOLOGIA, "La ansiedad activa el sistema de alarma del cuerpo aunque no haya un peligro inmediato delante."),
        reen(ANSIEDAD, PSICOLOGIA, "Respirar despacio le indica al cuerpo que puede bajar la alarma poco a poco."),
        reen(ANSIEDAD, PSICOLOGIA, "Nombrar la sensación como ansiedad ayuda a no confundirla con una amenaza real."),
        reen(ANSIEDAD, PSICOLOGIA, "El pico de activación suele bajar solo en unos minutos si no lo alimentas con más pensamientos de alarma."),
        reen(ANSIEDAD, PSICOLOGIA, "Observar la sensación sin huir de ella suele reducirla más rápido que intentar apartarla."),
        reen(ANSIEDAD, PSICOLOGIA, "El cuerpo tenso también se relaja poco a poco cuando le das una instrucción clara y simple."),

        acc(ANSIEDAD, "Respira contando cuatro tiempos al inhalar, mantén dos y suelta el aire en seis tiempos, varias veces."),
        acc(ANSIEDAD, "Pon los pies firmes en el suelo y nombra cinco cosas que puedas ver a tu alrededor."),
        acc(ANSIEDAD, "Aprieta los puños cinco segundos y suéltalos despacio, repite varias veces."),
        acc(ANSIEDAD, "Bebe agua despacio, notando la temperatura y el trago al pasar."),
        acc(ANSIEDAD, "Apoya una mano en el pecho y otra en el abdomen y respira notando cuál se mueve más."),
        acc(ANSIEDAD, "Sal al aire libre unos minutos y camina despacio prestando atención a tus pasos."),

        // ================= SINTOMAS_FISICOS =================
        reco(SINTOMAS, "El cuerpo también carga con esta ruptura: cansancio, falta de apetito o dolores que antes no estaban."),
        reco(SINTOMAS, "No comer bien o dormir mal estos días es una respuesta común del cuerpo al dolor, no un fallo tuyo."),
        reco(SINTOMAS, "El malestar físico y el emocional suelen ir juntos cuando algo importante se rompe."),
        reco(SINTOMAS, "Sentir el cuerpo cansado o pesado tiene sentido después de estos días."),
        reco(SINTOMAS, "Cuidar el cuerpo ahora es parte de cuidar el proceso entero."),

        reen(SINTOMAS, ESTOICO, "No elegiste este malestar en el cuerpo, pero sí puedes elegir cómo cuidarlo hoy."),
        reen(SINTOMAS, ESTOICO, "El cuerpo pide lo básico: agua, comida y descanso; darle eso está en tu mano."),
        reen(SINTOMAS, ESTOICO, "Un cuerpo cuidado sostiene mejor cualquier proceso difícil que atravieses."),
        reen(SINTOMAS, ESTOICO, "No es debilidad atender el cuerpo; es una forma de disciplina serena."),
        reen(SINTOMAS, ESTOICO, "Postergar el cuidado del cuerpo no acorta el dolor, solo le resta apoyo."),
        reen(SINTOMAS, ESTOICO, "Ocuparte de lo básico hoy es un acto pequeño y firme que sí depende de ti."),

        reen(SINTOMAS, CATOLICO, "Tu cuerpo también es digno de cuidado en medio del dolor, no solo tu ánimo."),
        reen(SINTOMAS, CATOLICO, "Comer algo, aunque sea poco, es también un gesto de cuidado hacia ti."),
        reen(SINTOMAS, CATOLICO, "No tienes que sanar hoy del todo; basta con darle al cuerpo lo que necesita en este momento."),
        reen(SINTOMAS, CATOLICO, "Descansar no es rendirse: es una forma de cuidar lo que Dios te ha confiado, tu propia vida."),
        reen(SINTOMAS, CATOLICO, "Puedes pedir ayuda para lo físico igual que para lo emocional; no hace falta cargarlo en soledad."),
        reen(SINTOMAS, CATOLICO, "Un cuerpo cansado también necesita paciencia y ternura, no solo el corazón."),

        reen(SINTOMAS, PSICOLOGIA, "El estrés prolongado afecta el sueño, el apetito y la energía; es una respuesta fisiológica esperable."),
        reen(SINTOMAS, PSICOLOGIA, "Cuidar lo básico (comer, beber agua, moverte un poco) ayuda a regular el sistema nervioso."),
        reen(SINTOMAS, PSICOLOGIA, "El cuerpo y la mente están conectados: cuidar uno ayuda a sostener al otro."),
        reen(SINTOMAS, PSICOLOGIA, "No hace falta comer mucho ni dormir perfecto hoy, solo dar un paso pequeño hacia cuidarte."),
        reen(SINTOMAS, PSICOLOGIA, "Registrar cómo está tu cuerpo hoy ayuda a notar patrones y a pedir ayuda si hace falta."),
        reen(SINTOMAS, PSICOLOGIA, "Un cuerpo agotado tiene menos recursos para regular las emociones; descansar ayuda a las dos cosas."),

        acc(SINTOMAS, "Come algo sencillo ahora, aunque sea poco: una fruta, un yogur o un puñado de frutos secos."),
        acc(SINTOMAS, "Bebe un vaso de agua despacio y ponte otro cerca para el resto del día."),
        acc(SINTOMAS, "Haz una lista corta de lo mínimo que tu cuerpo necesita hoy: comer, beber agua, moverte un poco."),
        acc(SINTOMAS, "Estírate suavemente cuello y hombros durante un par de minutos."),
        acc(SINTOMAS, "Si el malestar físico se repite mucho, anótalo y coméntalo con un profesional de salud."),
        acc(SINTOMAS, "Sal a caminar diez minutos a paso tranquilo, sin más objetivo que moverte."),

        // ================= INSOMNIO_NOCHE =================
        reco(INSOMNIO, "La cabeza suele ponerse más ruidosa justo cuando el cuerpo busca descansar."),
        reco(INSOMNIO, "No poder dormir estas noches tiene sentido: la mente sigue procesando lo ocurrido."),
        reco(INSOMNIO, "Dar vueltas en la cama no significa que hagas algo mal; el sueño a veces tarda en volver."),
        reco(INSOMNIO, "El silencio de la noche deja más espacio para que los pensamientos se hagan notar."),
        reco(INSOMNIO, "Una noche sin dormir bien no define cómo será el resto del proceso."),

        reen(INSOMNIO, ESTOICO, "No depende de ti dormirte al instante; sí depende de ti cómo tratas tu cuerpo mientras esperas el sueño."),
        reen(INSOMNIO, ESTOICO, "Forzar el sueño solo añade tensión; sostener la calma sin exigir resultados es lo que puedes hacer ahora."),
        reen(INSOMNIO, ESTOICO, "Una noche difícil es solo eso, una noche; no hace falta juzgarla como un fracaso.", MADRUGADA),
        reen(INSOMNIO, ESTOICO, "Puedes aceptar que esta noche cuesta más sin pelear contra ese hecho."),
        reen(INSOMNIO, ESTOICO, "Mantener una rutina serena antes de dormir está en tu mano, aunque el sueño tarde en llegar."),
        reen(INSOMNIO, ESTOICO, "El descanso del cuerpo, aunque no sea sueño profundo, también cuenta como cuidado."),

        reen(INSOMNIO, CATOLICO, "Puedes entregar en silencio los pensamientos que no te dejan dormir, en vez de cargarlos tú solo/a."),
        reen(INSOMNIO, CATOLICO, "La noche también puede ser un tiempo de calma si dejas descansar la mente en algo más grande que tus preocupaciones."),
        reen(INSOMNIO, CATOLICO, "No hace falta resolver nada a estas horas; basta con descansar el cuerpo lo que se pueda.", MADRUGADA),
        reen(INSOMNIO, CATOLICO, "Una oración breve y sencilla puede ayudarte a soltar el ruido de la mente antes de dormir."),
        reen(INSOMNIO, CATOLICO, "Confiar no siempre trae sueño inmediato, pero sí un poco más de paz mientras esperas."),
        reen(INSOMNIO, CATOLICO, "Tu descanso importa; cuidarlo también es una forma de cuidar tu entrega de cada día."),

        reen(INSOMNIO, PSICOLOGIA, "La mente activa por la noche suele repasar lo que no pudo procesar durante el día."),
        reen(INSOMNIO, PSICOLOGIA, "Intentar dormir a la fuerza suele producir más alerta, no menos; soltar la exigencia ayuda más."),
        reen(INSOMNIO, PSICOLOGIA, "Escribir los pensamientos antes de dormir puede ayudar a que la mente los suelte un poco.", MADRUGADA),
        reen(INSOMNIO, PSICOLOGIA, "Una rutina simple y repetida antes de dormir le indica al cuerpo que se acerca el descanso."),
        reen(INSOMNIO, PSICOLOGIA, "Levantarte un rato si llevas mucho tiempo despierto/a suele ayudar más que quedarte dándole vueltas en la cama."),
        reen(INSOMNIO, PSICOLOGIA, "El cuerpo cansado por dormir poco también descansa, aunque de forma distinta a un sueño completo."),

        acc(INSOMNIO, "Apaga las pantallas al menos veinte minutos antes de intentar dormir."),
        acc(INSOMNIO, "Escribe en una nota los pensamientos que te rondan, para dejarlos fuera de la cabeza esta noche."),
        acc(INSOMNIO, "Si llevas más de veinte minutos despierto/a, levántate, haz algo tranquilo y vuelve a la cama después."),
        acc(INSOMNIO, "Respira despacio contando hasta cuatro al inhalar y hasta seis al soltar, varias veces seguidas."),
        acc(INSOMNIO, "Baja la luz de la habitación y busca una postura cómoda sin buscar el móvil."),
        acc(INSOMNIO, "Escucha algo suave y repetitivo (lluvia, respiración guiada) en lugar de dar vueltas a pensamientos.")
    )

    // ---- CIERRE del grupo CUERPO (por marco)
    val cierres: List<ContentAtom> = listOf(
        cierre(ESTOICO, "¿Qué necesita tu cuerpo ahora que sí está en tu mano darle?"),
        cierre(ESTOICO, "¿Qué parte de este malestar puedes simplemente observar sin pelear contra ella?"),
        cierre(ESTOICO, "¿Qué pequeño cuidado del cuerpo puedes elegir en los próximos minutos?"),
        cierre(CATOLICO, "¿Qué necesita hoy tu cuerpo para sentirse un poco más sostenido?"),
        cierre(CATOLICO, "¿A quién podrías contarle cómo está tu cuerpo estos días?"),
        cierre(CATOLICO, "¿Qué gesto sencillo de cuidado puedes ofrecerte esta noche?"),
        cierre(PSICOLOGIA, "¿Qué necesita tu cuerpo en este momento: agua, comida, descanso o movimiento?"),
        cierre(PSICOLOGIA, "¿En qué parte del cuerpo notas la tensión ahora mismo?"),
        cierre(PSICOLOGIA, "¿Qué pequeño cambio te ayudaría a descansar un poco mejor hoy?"),
        cierre(ESTOICO, "¿Qué gesto pequeño y firme puedes ofrecerle a tu cuerpo antes de dormir?"),
        cierre(ESTOICO, "¿Qué parte de tu rutina de hoy sí dependió de ti, aunque el cuerpo estuviera cansado?"),
        atom(Slot.CIERRE, "¿Qué necesita tu cuerpo esta noche para descansar un poco más tranquilo?", groups = CUERPO, frameworks = ESTOICO, context = NOCHE),
        cierre(CATOLICO, "¿Qué parte de tu cansancio puedes entregar esta noche en lugar de cargarla tú solo/a?"),
        cierre(CATOLICO, "¿Qué pequeño gesto de cuidado te haría bien antes de dormir?"),
        atom(Slot.CIERRE, "¿Qué puedes soltar esta noche para descansar el cuerpo un poco más?", groups = CUERPO, frameworks = CATOLICO, context = NOCHE),
        cierre(PSICOLOGIA, "¿Qué rutina sencilla podría ayudarte a dormir un poco mejor esta semana?"),
        cierre(PSICOLOGIA, "¿Qué le dirías a tu cuerpo cansado con la misma amabilidad que a alguien querido?"),
        atom(Slot.CIERRE, "¿Qué necesita tu cuerpo esta noche para bajar un poco la activación?", groups = CUERPO, frameworks = PSICOLOGIA, context = NOCHE),
        cierreRep("Si el cuerpo lleva muchos días así, contárselo a un profesional de salud es un paso razonable."),
        cierreRep("Cuando el malestar físico se repite, anotarlo unos días ayuda a verlo con más claridad."),
        cierreRep("Si esto se repite noche tras noche, hablarlo con alguien de confianza o un profesional puede aliviar la carga.")
    )

    // ---- CITA del grupo CUERPO (por marco)
    val citas: List<ContentAtom> = listOf(
        cita(ESTOICO, "Cuidar el cuerpo es también una forma de cuidar el ánimo que vive en él.", "Idea estoica"),
        cita(ESTOICO, "«Ejercítate en las cosas pequeñas, y desde ahí pasa a las mayores.»", "Epicteto", "Enquiridión, 1"),
        cita(ESTOICO, "No es el cuerpo el que decide tu carácter, sino cómo lo tratas mientras te sostiene.", "Idea estoica"),
        cita(ESTOICO, "Cuidar lo simple del cuerpo es una forma de entrenar la firmeza del ánimo.", "Idea estoica"),
        cita(ESTOICO, "«El hábito de resistir se fortalece con la práctica diaria de lo pequeño.»", "Epicteto", "Enquiridión, 47 (adaptado)"),
        cita(ESTOICO, "Lo que haces con tu cuerpo hoy forma el hábito que tendrás mañana.", "Idea estoica"),
        cita(ESTOICO, "Aceptar una noche difícil sin exigirle más de lo que puede dar es también sabiduría.", "Idea estoica"),
        cita(ESTOICO, "«No busques que las cosas sucedan como deseas, sino desea que sucedan como suceden.»", "Epicteto", "Enquiridión, 8"),

        cita(CATOLICO, "«Glorificad a Dios en vuestro cuerpo.»", "San Pablo", "1 Corintios 6, 20"),
        cita(CATOLICO, "«Venid a mí todos los que estáis cansados y agobiados, y yo os aliviaré.»", "Evangelio de Mateo", "Mateo 11, 28"),
        cita(CATOLICO, "«El Señor es mi pastor, nada me falta... me hace descansar.»", "Salmo 23", "Salmo 23, 1-2"),
        cita(CATOLICO, "Cuidar el cuerpo cansado también es una forma de gratitud por la vida recibida.", "Tradición cristiana"),
        cita(CATOLICO, "«En paz me acuesto y en seguida me duermo, porque tú solo, Señor, me haces vivir tranquilo.»", "Salmo 4", "Salmo 4, 9"),
        cita(CATOLICO, "Entregar el cansancio del cuerpo en oración no lo resuelve todo, pero alivia la carga.", "Tradición cristiana"),
        cita(CATOLICO, "«No os angustiéis por nada.»", "San Pablo", "Filipenses 4, 6"),
        cita(CATOLICO, "El descanso también forma parte del orden que Dios quiso para la vida humana.", "Tradición cristiana"),

        cita(PSICOLOGIA, "El cuerpo y la mente se regulan juntos: cuidar uno ayuda a sostener al otro.", "Psicología de la salud"),
        cita(PSICOLOGIA, "La respiración lenta activa la respuesta de calma del sistema nervioso.", "Regulación fisiológica del estrés"),
        cita(PSICOLOGIA, "Nombrar una sensación física reduce su intensidad percibida con el tiempo.", "Terapia cognitivo-conductual"),
        cita(PSICOLOGIA, "El estrés sostenido afecta el sueño y el apetito de forma esperable, no como señal de fallo personal.", "Psicoeducación sobre estrés"),
        cita(PSICOLOGIA, "Levantarse de la cama cuando el sueño no llega reduce la asociación entre cama y desvelo.", "Terapia cognitivo-conductual del insomnio"),
        cita(PSICOLOGIA, "Una rutina repetida antes de dormir ayuda al cuerpo a anticipar el descanso.", "Higiene del sueño"),
        cita(PSICOLOGIA, "Observar una sensación sin huir de ella suele reducirla más rápido que evitarla.", "Terapia de aceptación y compromiso (ACT)"),
        cita(PSICOLOGIA, "Cuidar lo básico del cuerpo mejora también la capacidad de regular las emociones.", "Psicología de la salud")
    )
}

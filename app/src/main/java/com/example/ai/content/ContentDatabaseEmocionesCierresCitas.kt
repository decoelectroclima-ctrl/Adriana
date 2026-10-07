package com.example.ai.content

import com.example.data.SoltarFramework

/**
 * Cierres y citas del grupo EMOCIONES (D3, parte final). Con esto el grupo EMOCIONES queda completo.
 */
object ContentDatabaseEmocionesCierresCitas {

    private val EMOCIONES = setOf(ContentGroup.EMOCIONES)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)
    private val REPETIDO = AtomContext(repeated = true)

    private fun cierre(fw: Set<SoltarFramework>, text: String) = atom(Slot.CIERRE, text, groups = EMOCIONES, frameworks = fw)
    private fun cierreRep(text: String) = atom(Slot.CIERRE, text, groups = EMOCIONES, context = REPETIDO)
    private fun cita(fw: Set<SoltarFramework>, text: String, author: String, source: String = "") =
        atom(Slot.CITA, text, groups = EMOCIONES, frameworks = fw, author = author, source = source)

    val cierres: List<ContentAtom> = listOf(
        cierre(ESTOICO, "¿Qué parte de esta emoción sí puedes gobernar ahora mismo?"),
        cierre(ESTOICO, "¿Qué juicio le estás añadiendo a lo que sientes en este momento?"),
        cierre(ESTOICO, "¿Qué harías ahora si dejaras que la emoción pasara sin pelear contra ella?"),
        cierre(ESTOICO, "¿Qué acción pequeña y firme puedes tomar hoy respecto a esto?"),
        cierre(ESTOICO, "¿Qué necesitas soltar hoy para sentir un poco más de calma?"),
        cierre(ESTOICO, "¿Qué le dirías a alguien que sintiera exactamente esto que tú sientes?"),

        cierre(CATOLICO, "¿Qué puedes entregar en oración de lo que sientes ahora?"),
        cierre(CATOLICO, "¿Qué necesita tu corazón en este momento: consuelo, compañía o silencio?"),
        cierre(CATOLICO, "¿A quién podrías contarle cómo te sientes hoy?"),
        cierre(CATOLICO, "¿Qué gesto de ternura hacia ti mismo/a te haría bien ahora?"),
        cierre(CATOLICO, "¿Qué parte de esta emoción puedes soltar hoy, aunque sea un poco?"),
        cierre(CATOLICO, "¿Qué te recuerda hoy que tu valor no depende de esto que sientes?"),

        cierre(PSICOLOGIA, "¿Qué nombre le pondrías exactamente a lo que sientes ahora?"),
        cierre(PSICOLOGIA, "¿Qué necesitas en este momento: expresar la emoción, calmarla o entenderla?"),
        cierre(PSICOLOGIA, "¿Qué le dirías a un amigo que sintiera exactamente esto?"),
        cierre(PSICOLOGIA, "¿Esta emoción te está dando información útil o solo repitiendo el mismo dolor?"),
        cierre(PSICOLOGIA, "¿Qué pequeño gesto de autocuidado podrías darte ahora mismo?"),
        cierre(PSICOLOGIA, "¿Cómo cambiaría esta emoción si la miraras con la misma compasión que a un amigo?"),

        atom(Slot.CIERRE, "¿Qué necesita tu corazón esta noche para descansar un poco de esta emoción?", groups = EMOCIONES, frameworks = ESTOICO, context = AtomContext(timeOfDay = TimeOfDay.NOCHE)),
        atom(Slot.CIERRE, "¿Qué puedes entregar esta noche para descansar de este peso?", groups = EMOCIONES, frameworks = CATOLICO, context = AtomContext(timeOfDay = TimeOfDay.NOCHE)),
        atom(Slot.CIERRE, "¿Qué necesitas esta noche para bajar un poco la intensidad de lo que sientes?", groups = EMOCIONES, frameworks = PSICOLOGIA, context = AtomContext(timeOfDay = TimeOfDay.NOCHE)),

        cierreRep("Si esta emoción se repite con mucha intensidad, hablarlo con un profesional de salud mental puede ayudarte."),
        cierreRep("Cuando esto vuelve una y otra vez, contárselo a alguien de confianza suele aliviar la carga."),
        cierreRep("Si el malestar se mantiene muchos días seguidos, buscar apoyo profesional es un paso razonable.")
    )

    val citas: List<ContentAtom> = listOf(
        cita(ESTOICO, "«No es la cosa en sí lo que perturba, sino la opinión que tenemos de ella.»", "Epicteto", "Enquiridión, 5"),
        cita(ESTOICO, "«Quien no tiene miedo a la muerte, nada puede temer en la vida.»", "Epicteto", "Disertaciones (adaptado)"),
        cita(ESTOICO, "El rencor sostenido pesa más sobre quien lo carga que sobre quien lo provocó.", "Idea estoica"),
        cita(ESTOICO, "«Acostúmbrate a considerar que nada es tan propio del hombre como el bien y el mal que depende de su voluntad.»", "Epicteto", "Enquiridión, 1 (adaptado)"),
        cita(ESTOICO, "Tu valor como persona no depende del juicio ni de la decisión de otro.", "Idea estoica"),
        cita(ESTOICO, "«Lo que puede turbarnos no es lo que nos sucede, sino nuestro juicio sobre ello.»", "Epicteto", "Enquiridión, 5 (variante)"),
        cita(ESTOICO, "Sostener una emoción intensa sin actuarla de inmediato es un ejercicio de fortaleza.", "Idea estoica"),
        cita(ESTOICO, "«Ejercítate en las cosas pequeñas, y desde ahí pasa a las mayores.»", "Epicteto", "Enquiridión, 1"),

        cita(CATOLICO, "«El amor es paciente, es servicial.»", "San Pablo", "1 Corintios 13, 4"),
        cita(CATOLICO, "«Bienaventurados los que sufren, porque ellos serán consolados.»", "Evangelio de Mateo", "Mateo 5, 4"),
        cita(CATOLICO, "«Perdona nuestras ofensas, como también nosotros perdonamos.»", "Padre Nuestro", "Evangelio de Mateo, 6, 12"),
        cita(CATOLICO, "«Nos hiciste para ti, y nuestro corazón está inquieto hasta que descanse en ti.»", "Agustín de Hipona", "Confesiones, I, 1"),
        cita(CATOLICO, "«Mi gracia te basta, porque mi poder se manifiesta en la debilidad.»", "San Pablo", "2 Corintios 12, 9"),
        cita(CATOLICO, "El perdón es un camino, no un interruptor que se acciona de golpe.", "Tradición cristiana"),
        cita(CATOLICO, "«El Señor está cerca de los que tienen el corazón quebrantado.»", "Salmo 34", "Salmo 34, 19"),
        cita(CATOLICO, "Tu dignidad no depende de lo que otra persona haya decidido sobre ti.", "Tradición cristiana"),

        cita(PSICOLOGIA, "Nombrar una emoción con precisión reduce su intensidad en el sistema nervioso.", "Regulación emocional"),
        cita(PSICOLOGIA, "El rechazo activa circuitos cerebrales similares al dolor físico.", "Neurociencia del dolor social"),
        cita(PSICOLOGIA, "La autocompasión reduce el impacto de la autocrítica severa mejor que el juicio duro hacia uno mismo.", "Autocompasión (Kristin Neff)"),
        cita(PSICOLOGIA, "El vacío emocional después de una pérdida importante es una respuesta esperable del apego.", "Psicología del apego"),
        cita(PSICOLOGIA, "La confianza rota por una traición suele necesitar más tiempo de reparación que otras heridas.", "Psicología de la confianza"),
        cita(PSICOLOGIA, "Expresar una emoción de forma segura ayuda a procesarla mejor que reprimirla.", "Terapia cognitivo-conductual"),
        cita(PSICOLOGIA, "Buscar validación externa de forma constante rara vez calma la inseguridad de fondo.", "Psicología de la autoestima"),
        cita(PSICOLOGIA, "Tratarte con la misma amabilidad que a alguien querido mejora la forma de atravesar el dolor.", "Autocompasión (Kristin Neff)")
    )
}

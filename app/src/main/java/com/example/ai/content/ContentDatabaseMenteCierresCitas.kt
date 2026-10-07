package com.example.ai.content

import com.example.data.SoltarFramework

/**
 * Cierres y citas del grupo MENTE (D3, parte 4 de 4). Con esto el grupo MENTE queda completo.
 */
object ContentDatabaseMenteCierresCitas {

    private val MENTE = setOf(ContentGroup.MENTE)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)
    private val REPETIDO = AtomContext(repeated = true)

    private fun cierre(fw: Set<SoltarFramework>, text: String) = atom(Slot.CIERRE, text, groups = MENTE, frameworks = fw)
    private fun cierreRep(text: String) = atom(Slot.CIERRE, text, groups = MENTE, context = REPETIDO)
    private fun cita(fw: Set<SoltarFramework>, text: String, author: String, source: String = "") =
        atom(Slot.CITA, text, groups = MENTE, frameworks = fw, author = author, source = source)

    val cierres: List<ContentAtom> = listOf(
        cierre(ESTOICO, "¿Qué pensamiento de los últimos minutos merece de verdad tu atención?"),
        cierre(ESTOICO, "¿Qué harías ahora si dejaras de darle vueltas a esto un rato?"),
        cierre(ESTOICO, "¿Qué parte de lo que piensas hoy depende realmente de ti?"),
        cierre(ESTOICO, "¿Qué juicio estás dando por hecho sin haberlo revisado?"),
        cierre(ESTOICO, "¿Qué acción pequeña te devolvería al presente ahora mismo?"),
        cierre(ESTOICO, "¿Qué le dirías a un amigo que estuviera pensando exactamente esto?"),

        cierre(CATOLICO, "¿Qué puedes entregar en oración de lo que hoy ronda tu cabeza?"),
        cierre(CATOLICO, "¿Qué necesita tu corazón para descansar un poco de este pensamiento?"),
        cierre(CATOLICO, "¿A quién podrías contarle lo que llevas pensando estos días?"),
        cierre(CATOLICO, "¿Qué certeza pequeña puedes sostener hoy, aunque el resto sea incierto?"),
        cierre(CATOLICO, "¿Qué gesto de paz puedes darte en este momento?"),
        cierre(CATOLICO, "¿Qué parte de esto puedes soltar hoy, aunque sea solo por unas horas?"),

        cierre(PSICOLOGIA, "¿Qué necesitas ahora: distraerte, escribirlo o hablarlo con alguien?"),
        cierre(PSICOLOGIA, "¿Este pensamiento te está ayudando a resolver algo o solo dando vueltas?"),
        cierre(PSICOLOGIA, "¿Qué actividad concreta podría interrumpir este bucle ahora mismo?"),
        cierre(PSICOLOGIA, "¿Qué evidencia real tienes de este pensamiento, más allá de la sensación?"),
        cierre(PSICOLOGIA, "¿Cómo te sentirás mañana si sigues dándole vueltas a esto toda la noche?"),
        cierre(PSICOLOGIA, "¿Qué le dirías a alguien querido que tuviera este mismo pensamiento?"),

        atom(Slot.CIERRE, "¿Qué pensamiento te acompaña esta noche y qué necesitas para soltarlo un rato?", groups = MENTE, frameworks = ESTOICO, context = AtomContext(timeOfDay = TimeOfDay.NOCHE)),
        atom(Slot.CIERRE, "¿Qué puedes entregar esta noche para descansar la mente un poco más?", groups = MENTE, frameworks = CATOLICO, context = AtomContext(timeOfDay = TimeOfDay.NOCHE)),
        atom(Slot.CIERRE, "¿Qué ayudaría a tu mente a bajar el ritmo antes de dormir esta noche?", groups = MENTE, frameworks = PSICOLOGIA, context = AtomContext(timeOfDay = TimeOfDay.NOCHE)),

        cierreRep("Si este pensamiento vuelve una y otra vez, escribirlo en el diario puede ayudarte a verlo con más distancia."),
        cierreRep("Cuando la mente insiste tanto en lo mismo, contárselo a alguien de confianza suele aliviar la carga."),
        cierreRep("Si esto se repite mucho, un profesional de salud mental puede ayudarte a manejarlo mejor.")
    )

    val citas: List<ContentAtom> = listOf(
        cita(ESTOICO, "«No es la cosa en sí lo que perturba, sino la opinión que tenemos de ella.»", "Epicteto", "Enquiridión, 5"),
        cita(ESTOICO, "«El alma se tiñe del color de sus pensamientos.»", "Marco Aurelio", "Meditaciones, V, 16"),
        cita(ESTOICO, "«Hoy he escapado de toda circunstancia agobiante, o mejor dicho, he desechado toda circunstancia agobiante.»", "Marco Aurelio", "Meditaciones, IX, 13 (adaptado)"),
        cita(ESTOICO, "Un pensamiento repetido no gana veracidad por el número de veces que lo piensas.", "Idea estoica"),
        cita(ESTOICO, "Examinar un juicio antes de aceptarlo es la base de la libertad interior.", "Idea estoica"),
        cita(ESTOICO, "«Ejercítate en pensar solo aquello que, si te preguntaran de repente, podrías responder sin rubor.»", "Marco Aurelio", "Meditaciones, III, 4 (adaptado)"),
        cita(ESTOICO, "Volver la atención al presente es un ejercicio, no algo que ocurre por sí solo.", "Idea estoica"),
        cita(ESTOICO, "«No busques que los sucesos ocurran como deseas, sino desea que ocurran como ocurren.»", "Epicteto", "Enquiridión, 8"),

        cita(CATOLICO, "«No os angustiéis por nada.»", "San Pablo", "Filipenses 4, 6"),
        cita(CATOLICO, "«Echa sobre el Señor tu cuidado, y él te sustentará.»", "Salmo 55", "Salmo 55, 23"),
        cita(CATOLICO, "«Venid a mí todos los que estáis cansados y agobiados, y yo os aliviaré.»", "Evangelio de Mateo", "Mateo 11, 28"),
        cita(CATOLICO, "«La paz os dejo, mi paz os doy.»", "Evangelio de Juan", "Juan 14, 27"),
        cita(CATOLICO, "Entregar un pensamiento repetido en oración no lo resuelve de golpe, pero alivia su peso.", "Tradición cristiana"),
        cita(CATOLICO, "«No se turbe vuestro corazón.»", "Evangelio de Juan", "Juan 14, 1"),
        cita(CATOLICO, "El silencio también es una forma de descanso para una mente cansada de pensar.", "Tradición cristiana"),
        cita(CATOLICO, "«Mi gracia te basta.»", "San Pablo", "2 Corintios 12, 9"),

        cita(PSICOLOGIA, "La rumiación mantiene activa la emoción sin aportar soluciones nuevas.", "Psicología cognitivo-conductual"),
        cita(PSICOLOGIA, "Un pensamiento es un evento mental, no una orden que debas obedecer.", "Terapia de aceptación y compromiso (ACT)"),
        cita(PSICOLOGIA, "Escribir un pensamiento ayuda a la mente a soltarlo mejor que repetirlo en silencio.", "Terapia cognitivo-conductual"),
        cita(PSICOLOGIA, "Nombrar el pensamiento como tal ayuda a tomar distancia de él.", "Defusión cognitiva (ACT)"),
        cita(PSICOLOGIA, "El progreso emocional no sigue una línea recta; las mesetas también forman parte del proceso.", "Psicoeducación sobre el duelo"),
        cita(PSICOLOGIA, "La memoria tiende a idealizar el pasado cuando el presente resulta incierto.", "Sesgo de memoria positiva"),
        cita(PSICOLOGIA, "La incertidumbre sobre el futuro genera más malestar cuando se imagina en detalle que cuando se acepta como tal.", "Psicología de la incertidumbre"),
        cita(PSICOLOGIA, "Dar por seguro el peor escenario posible no lo hace más probable.", "Terapia cognitivo-conductual")
    )
}

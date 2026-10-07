package com.example.ai.content

import com.example.data.SoltarFramework

/**
 * Cierres y citas del grupo SITUACIONES (D4). Con esto el grupo SITUACIONES queda completo.
 */
object ContentDatabaseSituacionesCierresCitas {

    private val SITUACIONES = setOf(ContentGroup.SITUACIONES)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)
    private val REPETIDO = AtomContext(repeated = true)

    private fun cierre(fw: Set<SoltarFramework>, text: String) = atom(Slot.CIERRE, text, groups = SITUACIONES, frameworks = fw)
    private fun cierreRep(text: String) = atom(Slot.CIERRE, text, groups = SITUACIONES, context = REPETIDO)
    private fun cita(fw: Set<SoltarFramework>, text: String, author: String, source: String = "") =
        atom(Slot.CITA, text, groups = SITUACIONES, frameworks = fw, author = author, source = source)

    val cierres: List<ContentAtom> = listOf(
        cierre(ESTOICO, "¿Qué parte de esta situación sí depende de ti resolver hoy?"),
        cierre(ESTOICO, "¿Qué límite necesitas sostener con más firmeza en este momento?"),
        cierre(ESTOICO, "¿Qué acción pequeña y concreta puedes tomar respecto a esto?"),
        cierre(ESTOICO, "¿Qué le dirías a un amigo que estuviera en esta misma situación?"),
        cierre(ESTOICO, "¿Qué necesitas soltar hoy para afrontar esto con más calma?"),
        cierre(ESTOICO, "¿Qué depende realmente de ti en esta situación y qué no?"),

        cierre(CATOLICO, "¿Qué puedes entregar en oración de esta situación concreta?"),
        cierre(CATOLICO, "¿Qué necesita tu corazón para atravesar esto con más paz?"),
        cierre(CATOLICO, "¿A quién podrías pedirle apoyo para sostener esta situación?"),
        cierre(CATOLICO, "¿Qué gesto de cuidado hacia ti mismo/a cabe hoy en medio de esto?"),
        cierre(CATOLICO, "¿Qué certeza pequeña puedes sostener hoy en esta situación?"),
        cierre(CATOLICO, "¿Qué parte de esto puedes confiar, aunque no la controles del todo?"),

        cierre(PSICOLOGIA, "¿Qué necesitas ahora: información, apoyo o simplemente tiempo?"),
        cierre(PSICOLOGIA, "¿Qué estrategia concreta podría ayudarte a manejar mejor esta situación?"),
        cierre(PSICOLOGIA, "¿Qué parte de esto está bajo tu control y qué parte no?"),
        cierre(PSICOLOGIA, "¿Cómo te gustaría sentirte al final de esta situación?"),
        cierre(PSICOLOGIA, "¿Qué apoyo necesitas pedir que aún no has pedido?"),
        cierre(PSICOLOGIA, "¿Qué le dirías a alguien querido que atravesara esto mismo?"),

        cierreRep("Si esta situación se repite mucho y te desborda, hablarlo con un profesional puede ayudarte a manejarla mejor."),
        cierreRep("Cuando esto vuelve una y otra vez, contárselo a alguien de confianza suele aliviar la carga."),
        cierreRep("Si el malestar por esta situación no baja con el tiempo, buscar apoyo especializado es un paso razonable.")
    )

    val citas: List<ContentAtom> = listOf(
        cita(ESTOICO, "«No es la cosa en sí lo que perturba, sino la opinión que tenemos de ella.»", "Epicteto", "Enquiridión, 5"),
        cita(ESTOICO, "«Ejercítate en las cosas pequeñas, y desde ahí pasa a las mayores.»", "Epicteto", "Enquiridión, 1"),
        cita(ESTOICO, "Un límite sostenido con calma protege más que uno impuesto con rabia.", "Idea estoica"),
        cita(ESTOICO, "«No busques que los sucesos ocurran como deseas, sino desea que ocurran como ocurren.»", "Epicteto", "Enquiridión, 8"),
        cita(ESTOICO, "Lo que otra persona decida hacer con su vida no resta valor a la tuya.", "Idea estoica"),
        cita(ESTOICO, "«Aplica de inmediato a cada cosa difícil el principio de que no es una desgracia soportarla.»", "Marco Aurelio", "Meditaciones, IV, 49"),
        cita(ESTOICO, "Sostener un límite es también una forma de dominio sobre uno mismo.", "Idea estoica"),
        cita(ESTOICO, "«Acostúmbrate a considerar que nada es tan propio del hombre como el bien y el mal que depende de su voluntad.»", "Epicteto", "Enquiridión, 1"),

        cita(CATOLICO, "«El amor es paciente.»", "San Pablo", "1 Corintios 13, 4"),
        cita(CATOLICO, "«No se turbe vuestro corazón.»", "Evangelio de Juan", "Juan 14, 1"),
        cita(CATOLICO, "«Venid a mí todos los que estáis cansados y agobiados, y yo os aliviaré.»", "Evangelio de Mateo", "Mateo 11, 28"),
        cita(CATOLICO, "«El Señor está cerca de los que tienen el corazón quebrantado.»", "Salmo 34", "Salmo 34, 19"),
        cita(CATOLICO, "Poner un límite con caridad también es una forma de cuidar el bien común.", "Tradición cristiana"),
        cita(CATOLICO, "«Mi gracia te basta.»", "San Pablo", "2 Corintios 12, 9"),
        cita(CATOLICO, "Confiar en el tiempo de Dios ayuda a sostener lo que hoy parece incierto.", "Tradición cristiana"),
        cita(CATOLICO, "«Echa sobre el Señor tu cuidado, y él te sustentará.»", "Salmo 55", "Salmo 55, 23"),

        cita(PSICOLOGIA, "Los límites claros reducen la exposición a estímulos que dificultan el proceso de sanar.", "Psicología del apego"),
        cita(PSICOLOGIA, "Separar los roles (pareja, coprogenitor) ayuda a reducir el desgaste emocional.", "Psicología familiar"),
        cita(PSICOLOGIA, "Planificar con antelación una situación difícil reduce la ansiedad anticipatoria.", "Terapia cognitivo-conductual"),
        cita(PSICOLOGIA, "Un diagnóstico formal requiere evaluación profesional, no una impresión a distancia.", "Psicoeducación clínica"),
        cita(PSICOLOGIA, "Describir conductas concretas ayuda más que las etiquetas generales para protegerte en el futuro.", "Psicología cognitivo-conductual"),
        cita(PSICOLOGIA, "El malestar ante disparadores concretos suele disminuir con exposiciones repetidas y manejadas con apoyo.", "Terapia de exposición"),
        cita(PSICOLOGIA, "La comparación social intensifica el malestar sin aportar información útil.", "Psicología social"),
        cita(PSICOLOGIA, "Cada situación difícil bien afrontada refuerza la confianza para la siguiente.", "Psicología del afrontamiento")
    )
}

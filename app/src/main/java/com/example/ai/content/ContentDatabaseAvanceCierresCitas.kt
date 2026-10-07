package com.example.ai.content

import com.example.data.SoltarFramework

/**
 * Cierres y citas del grupo AVANCE (D4, final de las 32 etiquetas). Con esto D4 termina.
 */
object ContentDatabaseAvanceCierresCitas {

    private val AVANCE = setOf(ContentGroup.AVANCE)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)

    private fun cierre(fw: Set<SoltarFramework>, text: String) = atom(Slot.CIERRE, text, groups = AVANCE, frameworks = fw)
    private fun cita(fw: Set<SoltarFramework>, text: String, author: String, source: String = "") =
        atom(Slot.CITA, text, groups = AVANCE, frameworks = fw, author = author, source = source)

    val cierres: List<ContentAtom> = listOf(
        cierre(ESTOICO, "¿Qué acción de hoy quieres reconocer como parte de tu propio avance?"),
        cierre(ESTOICO, "¿Qué hábito pequeño te gustaría sostener también en los días difíciles?"),
        cierre(ESTOICO, "¿Qué parte de este progreso depende de seguir actuando, no de sentir?"),
        cierre(ESTOICO, "¿Qué te gustaría construir a partir de este buen momento?"),
        cierre(ESTOICO, "¿Cómo puedes sostener esta calma cuando llegue un día más difícil?"),
        cierre(ESTOICO, "¿Qué parte de tu carácter se ha fortalecido en este proceso?"),

        cierre(CATOLICO, "¿Por qué te gustaría dar gracias hoy en este momento de tu camino?"),
        cierre(CATOLICO, "¿Qué esperanza puedes sostener a partir de este buen momento?"),
        cierre(CATOLICO, "¿Con quién te gustaría compartir esta sensación de avance?"),
        cierre(CATOLICO, "¿Qué parte de tu reconstrucción sientes que está en manos de Dios?"),
        cierre(CATOLICO, "¿Qué certeza te acompaña hoy que antes no tenías?"),
        cierre(CATOLICO, "¿Cómo te gustaría seguir cuidando este proceso de sanación?"),

        cierre(PSICOLOGIA, "¿Qué hiciste distinto hoy que te gustaría repetir?"),
        cierre(PSICOLOGIA, "¿Qué aprendizaje de este proceso te gustaría llevar contigo?"),
        cierre(PSICOLOGIA, "¿Qué meta pequeña te gustaría marcarte para esta semana?"),
        cierre(PSICOLOGIA, "¿Qué parte de tu identidad sientes que está creciendo en este proceso?"),
        cierre(PSICOLOGIA, "¿Cómo te gustaría celebrar este avance, aunque sea de forma sencilla?"),
        cierre(PSICOLOGIA, "¿Qué apoyo te ha ayudado más a llegar hasta este momento?")
    )

    val citas: List<ContentAtom> = listOf(
        cita(ESTOICO, "«No es lo que te sucede, sino cómo reaccionas, lo que importa.»", "Epicteto", "Disertaciones (adaptado)"),
        cita(ESTOICO, "«El progreso no consiste en leer muchos libros, sino en vivir conforme a la razón.»", "Epicteto", "Disertaciones, I, 4 (adaptado)"),
        cita(ESTOICO, "Cada acción constante construye, poco a poco, el carácter que quieres tener.", "Idea estoica"),
        cita(ESTOICO, "«Ejercítate en las cosas pequeñas, y desde ahí pasa a las mayores.»", "Epicteto", "Enquiridión, 1"),
        cita(ESTOICO, "Sostener la calma en los días buenos también es parte del entrenamiento del ánimo.", "Idea estoica"),
        cita(ESTOICO, "«El obstáculo para la acción avanza la acción. Lo que se interpone en el camino se convierte en el camino.»", "Marco Aurelio", "Meditaciones, V, 20"),
        cita(ESTOICO, "Reconocer el propio esfuerzo con serenidad, sin vanidad, es también sabiduría.", "Idea estoica"),
        cita(ESTOICO, "«Nada es tan propio del hombre como el bien y el mal que depende de su voluntad.»", "Epicteto", "Enquiridión, 1"),

        cita(CATOLICO, "«Todo lo puedo en Aquel que me fortalece.»", "San Pablo", "Filipenses 4, 13"),
        cita(CATOLICO, "«Den gracias en toda circunstancia.»", "San Pablo", "1 Tesalonicenses 5, 18"),
        cita(CATOLICO, "«Yo he venido para que tengan vida, y la tengan en abundancia.»", "Evangelio de Juan", "Juan 10, 10"),
        cita(CATOLICO, "«La esperanza no defrauda.»", "San Pablo", "Romanos 5, 5"),
        cita(CATOLICO, "Cada paso de reconstrucción también es un signo de la vida que sigue abriéndose paso.", "Tradición cristiana"),
        cita(CATOLICO, "«El Señor hace nuevas todas las cosas.»", "Apocalipsis", "Apocalipsis 21, 5"),
        cita(CATOLICO, "Dar gracias por lo recibido fortalece la confianza en lo que viene.", "Tradición cristiana"),
        cita(CATOLICO, "«Mi gracia te basta.»", "San Pablo", "2 Corintios 12, 9"),

        cita(PSICOLOGIA, "El progreso emocional se consolida con la repetición de pequeñas acciones de cuidado.", "Psicología del cambio de hábitos"),
        cita(PSICOLOGIA, "Registrar los avances, aunque sean pequeños, refuerza la motivación para seguir.", "Psicología conductual"),
        cita(PSICOLOGIA, "Reconstruir la identidad después de una pérdida es un proceso activo, no solo esperar que pase el tiempo.", "Psicología del duelo"),
        cita(PSICOLOGIA, "Los días de bienestar también forman parte válida del proceso de recuperación.", "Psicoeducación sobre el duelo"),
        cita(PSICOLOGIA, "Probar actividades nuevas ayuda a redescubrir intereses propios tras una ruptura.", "Psicología positiva"),
        cita(PSICOLOGIA, "El apoyo social sostenido es uno de los factores más asociados a una recuperación estable.", "Psicología social"),
        cita(PSICOLOGIA, "Celebrar los logros pequeños refuerza los cambios de conducta a largo plazo.", "Psicología conductual"),
        cita(PSICOLOGIA, "La autocompasión en los días buenos también ayuda a sostener los días difíciles.", "Autocompasión (Kristin Neff)")
    )
}

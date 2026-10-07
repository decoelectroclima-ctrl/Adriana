package com.example.ai.content

import com.example.data.SoltarFramework

/**
 * Citas que faltaban para el grupo AHORA_MISMO (el contenido semilla de D1 solo traia 2-3 por marco; el minimo es 8).
 */
object ContentDatabaseAhoraMismoCitas {

    private val AHORA = setOf(ContentGroup.AHORA_MISMO)
    private val ESTOICO = setOf(SoltarFramework.ESTOICO)
    private val CATOLICO = setOf(SoltarFramework.CATOLICO)
    private val PSICOLOGIA = setOf(SoltarFramework.PSICOLOGIA_MODERNA)

    private fun cita(fw: Set<SoltarFramework>, text: String, author: String, source: String = "") =
        atom(Slot.CITA, text, groups = AHORA, frameworks = fw, author = author, source = source)

    val atoms: List<ContentAtom> = listOf(

        // ESTOICO (habia 3, se añaden 5)
        cita(ESTOICO, "«Si quieres mejorar, permite que te consideren necio e ignorante en las cosas externas.»", "Epicteto", "Enquiridión, 13"),
        cita(ESTOICO, "«No es la cosa en sí lo que perturba, sino la opinión que tenemos de ella.»", "Epicteto", "Enquiridión, 5"),
        cita(ESTOICO, "Un impulso fuerte pesa más si le añades un relato encima; sin ese relato, pasa antes.", "Idea estoica"),
        cita(ESTOICO, "«Aplica de inmediato a cada cosa difícil el principio de que no es una desgracia soportarla.»", "Marco Aurelio", "Meditaciones, IV, 49"),
        cita(ESTOICO, "Actuar con calma en el momento difícil entrena el carácter que quieres tener mañana.", "Idea estoica"),

        // CATOLICO (habia 2, se añaden 6)
        cita(CATOLICO, "«Mi gracia te basta, porque mi poder se manifiesta en la debilidad.»", "San Pablo", "2 Corintios 12, 9"),
        cita(CATOLICO, "«Echa sobre el Señor tu cuidado, y él te sustentará.»", "Salmo 55", "Salmo 55, 23"),
        cita(CATOLICO, "«Bienaventurados los que sufren, porque ellos serán consolados.»", "Evangelio de Mateo", "Mateo 5, 4"),
        cita(CATOLICO, "Pedir fuerza en el momento del impulso también es una forma de oración sencilla.", "Tradición cristiana"),
        cita(CATOLICO, "«El amor es paciente.»", "San Pablo", "1 Corintios 13, 4"),
        cita(CATOLICO, "Sostener un momento difícil con calma es también un acto de confianza silenciosa.", "Tradición cristiana"),

        // PSICOLOGIA_MODERNA (habia 3, se añaden 5)
        cita(PSICOLOGIA, "Poner nombre a lo que sientes reduce su intensidad en el sistema nervioso.", "Regulación emocional"),
        cita(PSICOLOGIA, "El impulso más intenso rara vez dura más de veinte minutos si no lo alimentas.", "Prevención de recaídas: urge surfing"),
        cita(PSICOLOGIA, "Actuar por impulso alivia rápido y refuerza el ciclo; esperar sin actuar lo debilita con el tiempo.", "Terapia cognitivo-conductual"),
        cita(PSICOLOGIA, "Un pensamiento intenso no es una orden que debas obedecer de inmediato.", "Terapia de aceptación y compromiso (ACT)"),
        cita(PSICOLOGIA, "Tratarte con la misma amabilidad que a alguien querido ayuda a regular el malestar del momento.", "Autocompasión (Kristin Neff)")
    )
}

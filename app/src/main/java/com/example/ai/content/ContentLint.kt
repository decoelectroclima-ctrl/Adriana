package com.example.ai.content

import java.text.Normalizer

/**
 * Reglas de seguridad y estilo del contenido. Se usa en los tests de contenido y (fase D5) para validar
 * cualquier texto reescrito por la IA local. Devuelve nombres de regla incumplida; lista vacía = correcto.
 */
object ContentLint {

    val MAX_WORDS: Map<Slot, Int> = mapOf(
        Slot.RECONOCIMIENTO to 22,
        Slot.REENCUADRE to 45,
        Slot.ACCION to 32,
        Slot.CIERRE to 26,
        Slot.CITA to 40
    )
    const val MIN_WORDS = 3

    private fun rx(p: String) = Regex(p, RegexOption.IGNORE_CASE)

    private val PROMESA = rx("""\b(volvera|volveras a el|volveras a ella|te buscara|se arrepentira|te echara de menos|vais a volver|volvereis|acabara volviendo|volvera a buscarte)\b""")
    private val DIAGNOSTICO = rx("""\b(narcisista|psicopata|sociopata|toxic[oa]s?|bipolar|borderline|trastornos?|depresion clinica)\b""")
    private val CURACION = rx("""(el tiempo (lo )?cura|todo pasa por algo|\ben \d+ (dias|semanas|meses)|\bsanaras\b|\bte curaras\b|superaras (esto )?pronto)""")
    private val CULPA_DEBER = rx("""((debes|tienes que|deberias) perdonar|es tu culpa|mereces sufrir|no sabes amar)""")
    private val ORDEN_CONTACTO = rx("""\b(escribele|llamale|contactale|hablale|buscale|mandale)\b""")
    private val CRISIS = rx("""(suicid|\bmat(arme|arte|arse)\b|\bmorir|quitar(me|te|se) la vida|hacer(me|te|se) dano)""")
    private val URL = rx("""(https?://|www\.)""")
    private val SALUDO = rx("""(^\s*(hola|buenas|querid[oa])\b|amigo/a|,\s*(amig|querid)[oa]\s*[,.!?])""")
    private val CIFRAS = rx("""(\d+\s*%|estudios (demuestran|muestran)|segun la ciencia|esta demostrado)""")
    private val IA = rx("""(como (una )?ia\b|modelo de lenguaje|asistente virtual)""")
    private val ANAFORA = rx("""^\W*(esto|eso|ademas|por eso|pero|y|asi que|entonces|tambien)\b""")
    private val PRIMERA_PERSONA = rx("""\b(yo|mi|mis|mio|mia|nosotros|nosotras|nuestro|nuestra|nuestros|nuestras)\b""")
    private val ABSOLUTOS = rx("""\b(siempre|nunca|todos|nadie|jamas)\b""")
    private val COMILLAS = Regex("""[«»"“”]""")
    private val FORMATO = Regex("""[*_#`>\[\]{}]""")

    /** Minúsculas y sin tildes/diéresis (la ñ pasa a n). */
    fun normalize(text: String): String =
        Normalizer.normalize(text.lowercase(), Normalizer.Form.NFD).replace(Regex("\\p{Mn}+"), "")

    private fun hasEmoji(text: String): Boolean {
        var i = 0
        while (i < text.length) {
            val cp = text.codePointAt(i)
            val type = Character.getType(cp)
            if (cp >= 0x1F000 || type == Character.OTHER_SYMBOL.toInt() || cp in 0x2600..0x27BF) return true
            i += Character.charCount(cp)
        }
        return false
    }

    fun wordCount(text: String): Int = text.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.size

    fun check(text: String, slot: Slot): List<String> {
        val v = mutableListOf<String>()
        val n = normalize(text)
        val words = wordCount(text)

        if (words < MIN_WORDS) v += "LONGITUD_MINIMA"
        val max = MAX_WORDS.getValue(slot)
        if (words > max) v += "LONGITUD_MAXIMA($max)"

        if (PROMESA.containsMatchIn(n)) v += "PROMESA_O_PREDICCION"
        if (DIAGNOSTICO.containsMatchIn(n)) v += "DIAGNOSTICO"
        if (CURACION.containsMatchIn(n)) v += "CURACION_O_PLAZO"
        if (CULPA_DEBER.containsMatchIn(n)) v += "CULPA_O_DEBER"
        if (ORDEN_CONTACTO.containsMatchIn(n)) v += "ORDEN_DE_CONTACTO"
        if (CRISIS.containsMatchIn(n)) v += "TEMA_DE_CRISIS"
        if (FORMATO.containsMatchIn(text) || hasEmoji(text) || URL.containsMatchIn(n)) v += "FORMATO_PROHIBIDO"
        if (SALUDO.containsMatchIn(n)) v += "SALUDO_O_TRATAMIENTO"
        if (CIFRAS.containsMatchIn(n)) v += "CIFRAS_O_CIENCIA_VAGA"
        if (IA.containsMatchIn(n)) v += "REFERENCIA_A_IA"

        if (slot != Slot.CITA) {
            if (ANAFORA.containsMatchIn(n)) v += "ANAFORA_INICIAL"
            if (PRIMERA_PERSONA.containsMatchIn(n)) v += "PRIMERA_PERSONA"
            if (COMILLAS.containsMatchIn(text)) v += "COMILLAS_FUERA_DE_CITA"
        }
        return v
    }

    /** Avisos que no bloquean pero conviene revisar. */
    fun warnings(text: String): List<String> {
        val w = mutableListOf<String>()
        if (ABSOLUTOS.containsMatchIn(normalize(text))) w += "ABSOLUTO"
        return w
    }

    /** Comprobación completa de un átomo: ámbito, campos de cita y texto. */
    fun checkAtom(atom: ContentAtom): List<String> {
        val v = mutableListOf<String>()
        if (atom.tags.isEmpty() && atom.groups.isEmpty()) v += "SIN_AMBITO"
        if (atom.frameworks.isEmpty()) v += "SIN_MARCO"
        if (atom.slot == Slot.CITA) {
            if (atom.author.isBlank()) v += "CITA_SIN_AUTOR"
            if (atom.text.contains('«') || atom.text.contains('»') || atom.text.contains('"')) {
                if (atom.source.isBlank()) v += "CITA_LITERAL_SIN_FUENTE"
            }
        } else {
            if (atom.author.isNotBlank() || atom.source.isNotBlank()) v += "AUTOR_O_FUENTE_FUERA_DE_CITA"
        }
        v += check(atom.text, atom.slot)
        return v
    }
}

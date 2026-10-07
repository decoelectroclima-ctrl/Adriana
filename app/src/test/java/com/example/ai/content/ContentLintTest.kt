package com.example.ai.content

import com.example.ai.ClinicalCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentLintTest {

    private fun viola(text: String, slot: Slot, regla: String) =
        assertTrue("'$text' debía violar $regla pero dio ${ContentLint.check(text, slot)}", ContentLint.check(text, slot).any { it.startsWith(regla) })

    @Test
    fun `cada regla detecta su caso malo`() {
        viola("Tu ex volverá contigo cuando entienda lo que perdió.", Slot.REENCUADRE, "PROMESA_O_PREDICCION")
        viola("Vais a volver a estar juntos si tienes paciencia.", Slot.REENCUADRE, "PROMESA_O_PREDICCION")
        viola("Tu ex es un narcisista que juega contigo hoy.", Slot.REENCUADRE, "DIAGNOSTICO")
        viola("Esa relación era tóxica desde el principio, ya lo sabes.", Slot.REENCUADRE, "DIAGNOSTICO")
        viola("El tiempo lo cura todo, solo tienes que esperar sentado.", Slot.REENCUADRE, "CURACION_O_PLAZO")
        viola("En 21 días estarás mucho mejor que ahora, créeme.", Slot.REENCUADRE, "CURACION_O_PLAZO")
        viola("Todo pasa por algo y esto también pasará pronto.", Slot.REENCUADRE, "CURACION_O_PLAZO")
        viola("Deberías perdonar ya, es lo que hace una buena persona.", Slot.REENCUADRE, "CULPA_O_DEBER")
        viola("Es tu culpa que la relación terminara así de mal.", Slot.REENCUADRE, "CULPA_O_DEBER")
        viola("Escríbele ahora y cuéntale todo lo que sientes.", Slot.ACCION, "ORDEN_DE_CONTACTO")
        viola("Llámale esta noche y aclara todas las dudas pendientes.", Slot.ACCION, "ORDEN_DE_CONTACTO")
        viola("Piensas en quitarte la vida cuando el dolor aprieta fuerte.", Slot.REENCUADRE, "TEMA_DE_CRISIS")
        viola("A veces quieres morir de tristeza y no sabes por qué.", Slot.REENCUADRE, "TEMA_DE_CRISIS")
        viola("Respira **hondo** y suelta el aire muy despacio ahora.", Slot.ACCION, "FORMATO_PROHIBIDO")
        viola("Respira hondo 😊 y suelta el aire muy despacio ahora.", Slot.ACCION, "FORMATO_PROHIBIDO")
        viola("Mira https://ejemplo.com para más información útil.", Slot.ACCION, "FORMATO_PROHIBIDO")
        viola("Hola, hoy es un buen día para respirar con calma.", Slot.RECONOCIMIENTO, "SALUDO_O_TRATAMIENTO")
        viola("Tranquilo, amigo/a, esto también es parte del camino.", Slot.RECONOCIMIENTO, "SALUDO_O_TRATAMIENTO")
        viola("Respira despacio, amiga, que este momento va a pasar.", Slot.RECONOCIMIENTO, "SALUDO_O_TRATAMIENTO")
        viola("Querido lector, hoy toca cuidarte con mucha calma.", Slot.RECONOCIMIENTO, "SALUDO_O_TRATAMIENTO")
        viola("Un 80% de las personas mejora con este ejercicio diario.", Slot.REENCUADRE, "CIFRAS_O_CIENCIA_VAGA")
        viola("Los estudios demuestran que esperar siempre funciona bien.", Slot.REENCUADRE, "CIFRAS_O_CIENCIA_VAGA")
        viola("Como una IA, te sugiero respirar despacio y con calma.", Slot.REENCUADRE, "REFERENCIA_A_IA")
        viola("Además, respirar despacio te ayuda a calmarte mucho.", Slot.REENCUADRE, "ANAFORA_INICIAL")
        viola("Pero puedes elegir quedarte quieto unos minutos hoy.", Slot.REENCUADRE, "ANAFORA_INICIAL")
        viola("Yo creo que respirar despacio te va a ayudar mucho.", Slot.REENCUADRE, "PRIMERA_PERSONA")
        viola("Piensa en «tengo que escribirle» y déjalo pasar despacio.", Slot.REENCUADRE, "COMILLAS_FUERA_DE_CITA")
        viola("Respira.", Slot.ACCION, "LONGITUD_MINIMA")
    }

    @Test
    fun `los limites de longitud dependen de la ranura`() {
        val veintitres = List(23) { "palabra" }.joinToString(" ")
        viola(veintitres, Slot.RECONOCIMIENTO, "LONGITUD_MAXIMA")
        assertFalse(ContentLint.check(veintitres, Slot.REENCUADRE).any { it.startsWith("LONGITUD_MAXIMA") })
        val cuarentaYSeis = List(46) { "palabra" }.joinToString(" ")
        viola(cuarentaYSeis, Slot.REENCUADRE, "LONGITUD_MAXIMA")
    }

    @Test
    fun `los casos buenos pasan`() {
        val buenos = listOf(
            "Las ganas de escribirle llegan como una ola: intensas, pero con principio y final." to Slot.RECONOCIMIENTO,
            "Pon un temporizador de diez minutos y sal a caminar sin el móvil." to Slot.ACCION,
            "¿Qué harías si el impulso estuviera ahí, pero no mandara?" to Slot.CIERRE,
            "Si un amigo sintiera lo mismo, ¿qué le aconsejarías mantener firme?" to Slot.CIERRE,
            "Tu dignidad no depende de su respuesta. Puedes entregar esta ansiedad en un minuto de silencio antes de decidir nada." to Slot.REENCUADRE
        )
        for ((t, s) in buenos) assertEquals("'$t'", emptyList<String>(), ContentLint.check(t, s))
    }

    @Test
    fun `los acentos no permiten esquivar las reglas`() {
        viola("ESCRÍBELE ahora mismo, no esperes ni un minuto más.", Slot.ACCION, "ORDEN_DE_CONTACTO")
        viola("Tu ex VOLVERÁ cuando menos lo esperes, ya verás.", Slot.REENCUADRE, "PROMESA_O_PREDICCION")
        viola("Sentirás ganas de suicidarte y no debes callarlo así.", Slot.REENCUADRE, "TEMA_DE_CRISIS")
    }

    @Test
    fun `una cita literal exige fuente y toda cita exige autor`() {
        val sinFuente = atom(Slot.CITA, "«Frase de prueba con comillas de verdad.»", groups = setOf(ContentGroup.MENTE), author = "Alguien")
        assertTrue(ContentLint.checkAtom(sinFuente).contains("CITA_LITERAL_SIN_FUENTE"))
        val sinAutor = atom(Slot.CITA, "Una idea sin comillas pero sin autor tampoco.", groups = setOf(ContentGroup.MENTE))
        assertTrue(ContentLint.checkAtom(sinAutor).contains("CITA_SIN_AUTOR"))
        val correcta = atom(Slot.CITA, "«De las cosas, unas dependen de nosotros y otras no.»", groups = setOf(ContentGroup.MENTE), author = "Epicteto", source = "Enquiridión, 1")
        assertEquals(emptyList<String>(), ContentLint.checkAtom(correcta))
    }

    @Test
    fun `un atomo sin ambito o con autor fuera de cita es invalido`() {
        val sinAmbito = atom(Slot.ACCION, "Respira despacio durante dos minutos completos.")
        assertTrue(ContentLint.checkAtom(sinAmbito).contains("SIN_AMBITO"))
        val autorSuelto = atom(Slot.ACCION, "Respira despacio durante dos minutos completos.", tags = setOf(ClinicalCategory.IMPULSO_CONTACTAR), author = "X")
        assertTrue(ContentLint.checkAtom(autorSuelto).contains("AUTOR_O_FUENTE_FUERA_DE_CITA"))
    }

    @Test
    fun `los absolutos son solo aviso y no fallo`() {
        val t = "Nunca es tarde para elegir con calma lo que necesitas ahora."
        assertEquals(emptyList<String>(), ContentLint.check(t, Slot.REENCUADRE))
        assertEquals(listOf("ABSOLUTO"), ContentLint.warnings(t))
    }

    @Test
    fun `todo el contenido de la base pasa el lint sin fallos`() {
        val fallos = ContentDatabase.atoms.flatMap { a -> ContentLint.checkAtom(a).map { "[$it] ${a.text}" } }
        assertEquals("Fallos de lint:\n" + fallos.joinToString("\n"), 0, fallos.size)
        val avisos = ContentDatabase.atoms.flatMap { a -> ContentLint.warnings(a.text).map { "[$it] ${a.text}" } }
        println("AVISOS (no bloquean): ${avisos.size}"); avisos.forEach { println("  $it") }
    }

    @Test
    fun `los ids son unicos y estables`() {
        val ids = ContentDatabase.atoms.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
        assertEquals(ContentDatabase.atoms.map { it.id }, ids)
    }
}

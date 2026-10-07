package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.ai.SoltarUserContext
import com.example.data.SoltarFramework
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CardComposerTest {

    private val cat = ClinicalCategory.IMPULSO_CONTACTAR
    private val fw = SoltarFramework.ESTOICO
    private val HOUR = 3_600_000L
    private val DAY = 24 * HOUR

    private class FakeLegacy(var list: List<LegacyVariant>) : LegacySource {
        override fun variants(category: ClinicalCategory, framework: SoltarFramework, userContext: SoltarUserContext) = list
    }

    private class FakeQuotes : QuoteSource {
        var calls = 0
        override fun choose(category: ClinicalCategory, framework: SoltarFramework, store: FreshnessStore, exclude: Set<String>, random: Random): QuoteChoice {
            calls++
            return QuoteChoice("cap_fake", "cita de respaldo", "Autor de respaldo", "accion de la capsula")
        }
    }

    private fun a(slot: Slot, text: String, context: AtomContext = AtomContext()) =
        atom(slot, text, tags = setOf(cat), frameworks = setOf(fw), context = context)

    private fun legacy(n: Int) = List(n) { LegacyVariant("legacy_$it", "Titular $it", "Cuerpo completo $it") }

    private fun composer(atoms: List<ContentAtom>, leg: List<LegacyVariant> = legacy(2), hour: Int = 10) =
        CardComposer(atoms, FakeLegacy(leg), FakeQuotes(), hourOf = { hour })

    private fun basicAtoms() = listOf(
        a(Slot.RECONOCIMIENTO, "Reconocimiento uno de prueba"), a(Slot.RECONOCIMIENTO, "Reconocimiento dos de prueba"),
        a(Slot.REENCUADRE, "Reencuadre uno de prueba"), a(Slot.REENCUADRE, "Reencuadre dos de prueba"),
        a(Slot.REENCUADRE, "Reencuadre tres de prueba"), a(Slot.REENCUADRE, "Reencuadre cuatro de prueba"),
        a(Slot.ACCION, "Accion uno de prueba"), a(Slot.ACCION, "Accion dos de prueba"),
        a(Slot.CIERRE, "Cierre uno de prueba"), a(Slot.CIERRE, "Cierre dos de prueba"),
        atom(Slot.CITA, "Cita de prueba", tags = setOf(cat), frameworks = setOf(fw), author = "Autora", source = "Obra, 1")
    )

    private fun req(exclude: Set<String> = emptySet(), count: Boolean = true, relapse: Boolean = false, streak: Int = 10) =
        ComposeRequest(cat, fw, SoltarUserContext(streakDays = streak), "accion de respaldo", exclude, count, relapse)

    @Test
    fun `la tarjeta compuesta usa una pieza de cada ranura y ensambla titular cuerpo cita y accion`() {
        val store = InMemoryFreshnessStore()
        val leg = legacy(2)
        // legacy visto hace poco: fuerza tarjeta compuesta
        store.markSeen("legacy_0", 1_000_000L)
        val card = composer(basicAtoms(), leg).compose(req(), store, now = 1_000_000L + HOUR, random = Random(1))
        assertEquals(CardSource.COMPOSED, card.source)
        assertTrue(card.headline.startsWith("Reconocimiento"))
        assertTrue(card.body.startsWith("Reencuadre") && card.body.contains("Cierre"))
        assertEquals("Cita de prueba", card.quote)
        assertEquals("Autora, Obra, 1", card.author)
        assertTrue(card.action.startsWith("Accion"))
        assertEquals(5, card.atomIds.size)
    }

    @Test
    fun `nunca aparece saludo ni amigo-a en tarjetas compuestas`() {
        val store = InMemoryFreshnessStore()
        val c = composer(basicAtoms())
        store.markSeen("legacy_0", 0L)
        repeat(40) { i ->
            val card = c.compose(req(), store, now = i * HOUR, random = Random(i))
            assertFalse(card.body.contains("amigo", ignoreCase = true) || card.headline.startsWith("Hola"))
        }
    }

    @Test
    fun `excludeIds se respeta en todas las ranuras`() {
        val store = InMemoryFreshnessStore()
        store.markSeen("legacy_0", 0L)
        val c = composer(basicAtoms())
        val first = c.compose(req(count = false), store, now = HOUR, random = Random(5))
        repeat(30) { i ->
            val next = c.compose(req(exclude = first.atomIds.toSet(), count = false), store, now = (i + 2) * HOUR, random = Random(i))
            assertTrue("Repite piezas excluidas: ${next.atomIds} vs ${first.atomIds}",
                next.atomIds.none { it in first.atomIds && !it.startsWith("cap_") })
        }
    }

    @Test
    fun `una pieza nocturna solo sale de noche`() {
        val night = a(Slot.REENCUADRE, "Reencuadre nocturno especial", AtomContext(timeOfDay = TimeOfDay.NOCHE))
        val atoms = basicAtoms() + night
        val day = composer(atoms, hour = 10); val nightC = composer(atoms, hour = 23)
        val storeD = InMemoryFreshnessStore().also { it.markSeen("legacy_0", 0L) }
        val storeN = InMemoryFreshnessStore().also { it.markSeen("legacy_0", 0L) }
        var nightSeenAtDay = false; var nightSeenAtNight = false
        repeat(40) { i ->
            if (day.compose(req(count = false), storeD, now = i * HOUR, random = Random(i)).body.contains("nocturno")) nightSeenAtDay = true
            if (nightC.compose(req(count = false), storeN, now = i * HOUR, random = Random(i)).body.contains("nocturno")) nightSeenAtNight = true
        }
        assertFalse(nightSeenAtDay)
        assertTrue(nightSeenAtNight)
    }

    @Test
    fun `la fase temporal filtra piezas`() {
        val first = a(Slot.REENCUADRE, "Reencuadre para primeros dias", AtomContext(phase = TimePhase.PRIMEROS_DIAS))
        val atoms = basicAtoms() + first
        val store = InMemoryFreshnessStore().also { it.markSeen("legacy_0", 0L) }
        val c = composer(atoms)
        var seenLate = false
        repeat(40) { i -> if (c.compose(req(count = false, streak = 60), store, now = i * HOUR, random = Random(i)).body.contains("primeros dias")) seenLate = true }
        assertFalse(seenLate)
        var seenEarly = false
        repeat(40) { i -> if (c.compose(req(count = false, streak = 1), store, now = i * HOUR, random = Random(i)).body.contains("primeros dias")) seenEarly = true }
        assertTrue(seenEarly)
    }

    @Test
    fun `la escalada solo aparece con 3 usos de la etiqueta en 7 dias`() {
        val esc = a(Slot.CIERRE, "Cierre de escalada de prueba", AtomContext(repeated = true))
        val atoms = basicAtoms() + esc
        val c = composer(atoms)

        val two = InMemoryFreshnessStore().also { it.markSeen("legacy_0", 0L) }
        val now = 10 * DAY
        two.markTagUse(cat, now - DAY); two.markTagUse(cat, now - 2 * DAY)
        repeat(30) { i -> assertFalse(c.compose(req(count = false), two, now = now + i * HOUR, random = Random(i)).body.contains("escalada")) }

        val three = InMemoryFreshnessStore().also { it.markSeen("legacy_0", now - HOUR) }
        three.markTagUse(cat, now - DAY); three.markTagUse(cat, now - 2 * DAY); three.markTagUse(cat, now - 3 * DAY)
        val conEscalada = (0 until 10).count { i -> c.compose(req(count = false), three, now = now + i, random = Random(i)).body.contains("escalada") }
        // Se alterna con el cierre normal para no insistir: ni siempre ni nunca
        assertTrue("escaladas=$conEscalada de 10", conEscalada in 4..6)

        val old = InMemoryFreshnessStore().also { it.markSeen("legacy_0", now - HOUR) }
        old.markTagUse(cat, now - 8 * DAY); old.markTagUse(cat, now - 9 * DAY); old.markTagUse(cat, now - 10 * DAY)
        assertFalse(c.compose(req(count = false), old, now = now, random = Random(3)).body.contains("escalada"))
    }

    @Test
    fun `otra mirada no cuenta como uso nuevo de la etiqueta y elegir la etiqueta si`() {
        val store = InMemoryFreshnessStore()
        val c = composer(basicAtoms())
        c.compose(req(count = false), store, now = HOUR, random = Random(1))
        assertEquals(0, store.tagUses(cat).size)
        c.compose(req(count = true), store, now = 2 * HOUR, random = Random(2))
        assertEquals(1, store.tagUses(cat).size)
    }

    @Test
    fun `los huecos de ranura se rellenan sin fallar`() {
        val store = InMemoryFreshnessStore().also { it.markSeen("legacy_0", 0L) }
        // Con cita propia y sin acciones propias, la acción es la de reserva de la petición
        val sinAccion = composer(basicAtoms().filter { it.slot != Slot.ACCION }).compose(req(count = false), store, now = HOUR, random = Random(1))
        assertEquals("accion de respaldo", sinAccion.action)
        val sinCita = composer(basicAtoms().filter { it.slot != Slot.CITA }).compose(req(count = false), store, now = HOUR, random = Random(1))
        assertEquals("cita de respaldo", sinCita.quote); assertTrue(sinCita.atomIds.contains("cap_fake"))
        val sinCierreNiReco = composer(basicAtoms().filter { it.slot != Slot.CIERRE && it.slot != Slot.RECONOCIMIENTO }).compose(req(count = false), store, now = HOUR, random = Random(1))
        assertEquals("", sinCierreNiReco.headline)
        assertTrue(sinCierreNiReco.body.startsWith("Reencuadre") && !sinCierreNiReco.body.contains("Cierre"))
    }

    @Test
    fun `sin acciones propias usa la accion de la cita de respaldo y despues la de reserva`() {
        val store = InMemoryFreshnessStore().also { it.markSeen("legacy_0", 0L) }
        val atoms = basicAtoms().filter { it.slot != Slot.ACCION && it.slot != Slot.CITA }
        val card = composer(atoms).compose(req(count = false), store, now = HOUR, random = Random(1))
        assertEquals("accion de la capsula", card.action)
    }

    @Test
    fun `con menos de 3 reencuadres usa tarjeta completa`() {
        val pocos = basicAtoms().filter { !(it.slot == Slot.REENCUADRE && it.text.contains("tres") || it.text.contains("cuatro")) }
        val card = composer(pocos).compose(req(), InMemoryFreshnessStore(), now = HOUR, random = Random(1))
        assertEquals(CardSource.LEGACY, card.source)
        assertTrue(card.body.startsWith("Cuerpo completo"))
    }

    @Test
    fun `si no hay tarjetas completas usa las compuestas aunque sean pocas`() {
        val pocos = basicAtoms().filter { !(it.slot == Slot.REENCUADRE && (it.text.contains("tres") || it.text.contains("cuatro"))) }
        val card = composer(pocos, leg = emptyList()).compose(req(), InMemoryFreshnessStore(), now = HOUR, random = Random(1))
        assertEquals(CardSource.COMPOSED, card.source)
    }

    @Test
    fun `sin ninguna pieza devuelve una tarjeta de reserva y no lanza excepcion`() {
        val card = composer(emptyList(), leg = emptyList()).compose(req(), InMemoryFreshnessStore(), now = HOUR, random = Random(1))
        assertEquals(CardSource.FALLBACK, card.source)
        assertEquals("accion de respaldo", card.body)
        assertTrue(card.quote.isNotBlank())
    }

    @Test
    fun `si solo hay una tarjeta completa y esta excluida la vuelve a permitir en lugar de fallar`() {
        val card = composer(emptyList(), leg = legacy(1)).compose(req(exclude = setOf("legacy_0")), InMemoryFreshnessStore(), now = HOUR, random = Random(1))
        assertEquals(CardSource.LEGACY, card.source)
    }

    @Test
    fun `la tarjeta completa no vuelve durante 14 dias y despues puede volver`() {
        val c = composer(basicAtoms())
        val store = InMemoryFreshnessStore()
        var legacyPrimera = -1
        for (i in 0 until 60) {
            val card = c.compose(req(count = false), store, now = i * HOUR, random = Random(i))
            if (card.source == CardSource.LEGACY) { legacyPrimera = i; break }
        }
        assertTrue("Debe salir alguna tarjeta completa en 60 usos", legacyPrimera >= 0)
        // Durante los 14 dias siguientes no puede salir otra
        for (j in 1..200) {
            val card = c.compose(req(count = false), store, now = legacyPrimera * HOUR + j * HOUR, random = Random(1000 + j))
            if (j * HOUR < 14 * DAY) assertNotEquals(CardSource.LEGACY, card.source)
        }
        // Pasados 14 dias y un poco, vuelve a ser elegible
        var vuelve = false
        for (j in 0 until 200) {
            val t = legacyPrimera * HOUR + 15 * DAY + j * HOUR
            if (c.compose(req(count = false), store, now = t, random = Random(5000 + j)).source == CardSource.LEGACY) { vuelve = true; break }
        }
        assertTrue(vuelve)
    }

    @Test
    fun `es determinista con la misma semilla y el mismo almacen`() {
        fun serie(): List<List<String>> {
            val store = InMemoryFreshnessStore(); val c = composer(basicAtoms()); val rnd = Random(42)
            return (0 until 20).map { i -> c.compose(req(), store, now = i * 6 * HOUR, random = rnd).atomIds }
        }
        assertEquals(serie(), serie())
    }

    @Test
    fun `pickFresh nunca repite antes de min(pool-1, 5) selecciones`() {
        for (pool in 2..12) {
            val items = (1..pool).map { "p$it" }
            val store = InMemoryFreshnessStore(); val rnd = Random(pool)
            val orden = mutableListOf<String>()
            for (i in 0 until 200) {
                val x = pickFresh(items, { it }, { 1 }, store, rnd)!!
                store.markSeen(x, i.toLong()); orden.add(x)
            }
            val minGap = minOf(pool - 1, MIN_GAP)
            for (i in orden.indices) for (g in 1..minGap) {
                if (i + g < orden.size) assertNotEquals("pool=$pool: '${orden[i]}' repetida a distancia $g", orden[i], orden[i + g])
            }
        }
    }
}

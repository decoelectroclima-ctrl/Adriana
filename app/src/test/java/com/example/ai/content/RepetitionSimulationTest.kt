package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.ai.ClinicalLegacySource
import com.example.ai.SoltarUserContext
import com.example.data.SoltarFramework
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Simula uso real con el contenido y las tarjetas completas reales: 60 usos, uno cada 6 horas. */
class RepetitionSimulationTest {

    private val HOUR = 3_600_000L
    private val atomsById = ContentDatabase.atoms.associateBy { it.id }

    private fun run(fw: SoltarFramework, uses: Int = 60, category: ClinicalCategory = ClinicalCategory.IMPULSO_CONTACTAR, seed: Int = 7): List<ComposedCard> {
        val store = InMemoryFreshnessStore()
        val composer = CardComposer(ContentDatabase.atoms, ClinicalLegacySource, ClinicalLegacySource, hourOf = { 10 })
        val rnd = Random(seed)
        val ctx = SoltarUserContext(streakDays = 12)
        val t0 = 1_700_000_000_000L
        return (0 until uses).map { i ->
            composer.compose(ComposeRequest(category, fw, ctx, "accion de reserva", countAsNewUse = true), store, now = t0 + i * 6 * HOUR, random = rnd)
        }
    }

    private fun key(c: ComposedCard) = c.source.name + ":" + c.atomIds.joinToString(",")

    @Test
    fun `las tarjetas no se repiten enteras en los primeros 30 usos`() {
        // Cada pieza rota con su propio ciclo (5, 6, 6, 3, 3 en el contenido semilla), asi que la tarjeta completa
        // no puede repetirse antes del minimo comun multiplo de esos ciclos (30). Con los tamanos minimos de D2-D4
        // (5, 6, 6, 6, 8) el ciclo pasa a 120.
        for (fw in SoltarFramework.values()) {
            val cards = run(fw, uses = 60)
            val first30 = cards.take(30).map { key(it) }
            val ratio30 = first30.toSet().size.toDouble() / first30.size
            val ratio60 = cards.map { key(it) }.toSet().size.toDouble() / cards.size
            println("  ${fw.name}: 30 usos -> ${"%.0f".format(ratio30 * 100)}% distintas; 60 usos -> ${"%.0f".format(ratio60 * 100)}% distintas; completas=${cards.count { it.source == CardSource.LEGACY }}, compuestas=${cards.count { it.source == CardSource.COMPOSED }}")
            assertTrue("${fw.name}: solo ${"%.1f".format(ratio30 * 100)}% distintas en 30 usos", ratio30 >= 0.95)
        }
    }

    @Test
    fun `ninguna tarjeta identica en dos usos consecutivos`() {
        for (fw in SoltarFramework.values()) {
            val cards = run(fw)
            for (i in 1 until cards.size) assertNotEquals("${fw.name} uso $i", key(cards[i - 1]), key(cards[i]))
        }
    }

    @Test
    fun `ninguna pieza se repite antes de min(pool-1, 5) selecciones en cada ranura`() {
        for (fw in SoltarFramework.values()) {
            val cards = run(fw).filter { it.source == CardSource.COMPOSED }
            for (slot in listOf(Slot.RECONOCIMIENTO, Slot.REENCUADRE, Slot.ACCION)) {
                val pool = ContentDatabase.atoms.count { it.slot == slot && it.appliesTo(ClinicalCategory.IMPULSO_CONTACTAR, fw) }
                val seq = cards.mapNotNull { c -> c.atomIds.firstOrNull { atomsById[it]?.slot == slot } }
                val gap = minOf(pool - 1, MIN_GAP)
                for (i in seq.indices) for (g in 1..gap) if (i + g < seq.size)
                    assertNotEquals("${fw.name}/$slot: '${seq[i]}' a distancia $g (pool=$pool)", seq[i], seq[i + g])
            }
        }
    }

    @Test
    fun `la tarjeta completa aparece al menos una vez y sin saludo generico`() {
        for (fw in SoltarFramework.values()) {
            val cards = run(fw)
            assertTrue("${fw.name}: nunca salió una tarjeta completa", cards.any { it.source == CardSource.LEGACY })
            cards.forEach {
                assertTrue(it.body.isNotBlank())
                assertTrue(!it.body.contains("amigo/a") && !it.body.contains("**"))
            }
        }
    }

    @Test
    fun `las 32 etiquetas funcionan con contenido propio y rotan sin repetir en usos consecutivos`() {
        for (fw in SoltarFramework.values()) {
            for (cat in ClinicalCategory.values()) {
                val cards = run(fw, uses = 12, category = cat)
                assertTrue("${fw.name}/${cat.name}: no generó tarjetas válidas", cards.all { it.body.isNotBlank() && it.action.isNotBlank() && it.quote.isNotBlank() })
                for (i in 1 until cards.size) {
                    assertNotEquals("${fw.name}/${cat.name}: idéntica en usos consecutivos ($i)", key(cards[i - 1]), key(cards[i]))
                }
            }
        }
    }

    @Test
    fun `con uso intensivo la escalada aparece alternada y sus piezas rotan`() {
        val cards = run(SoltarFramework.PSICOLOGIA_MODERNA, uses = 30)
        val compuestas = cards.filter { it.source == CardSource.COMPOSED }
        fun esEscalada(c: ComposedCard) = c.atomIds.any { atomsById[it]?.context?.repeated == true }
        val escaladas = compuestas.filter { esEscalada(it) }
        assertTrue("Debe haber escalada tras 3 usos en 7 días", escaladas.isNotEmpty())
        assertTrue("No debe insistir en todas las tarjetas", compuestas.count { !esEscalada(it) } > escaladas.size / 2)
        // Nunca dos tarjetas compuestas seguidas con escalada
        for (i in 1 until compuestas.size) assertTrue("Escalada en dos usos seguidos ($i)", !(esEscalada(compuestas[i - 1]) && esEscalada(compuestas[i])))
        val ids = escaladas.map { c -> c.atomIds.first { atomsById[it]?.context?.repeated == true } }
        for (i in 1 until ids.size) assertNotEquals(ids[i - 1], ids[i])
    }

    @Test
    fun `la misma etiqueta en los tres marcos da contenido distinto`() {
        val bodies = SoltarFramework.values().map { run(it, uses = 1).first().body }
        assertEquals(3, bodies.toSet().size)
    }
}

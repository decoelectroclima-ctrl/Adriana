package com.example.ai.content

import com.example.ai.ClinicalCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FreshnessStateTest {

    @Test
    fun `respeta el limite de 400 piezas y expulsa las mas antiguas`() {
        val s = FreshnessState()
        for (i in 1..450) s.markSeen("id$i", i.toLong())
        assertEquals(400, s.size())
        assertNull(s.seenAt("id1"))
        assertNull(s.seenAt("id50"))
        assertEquals(51L, s.seenAt("id51"))
        assertEquals(450L, s.seenAt("id450"))
    }

    @Test
    fun `volver a marcar una pieza la convierte en la mas reciente`() {
        val s = FreshnessState(maxSeen = 3)
        s.markSeen("a", 1); s.markSeen("b", 2); s.markSeen("c", 3)
        s.markSeen("a", 4)
        s.markSeen("d", 5) // expulsa la mas antigua: b
        assertNull(s.seenAt("b"))
        assertEquals(4L, s.seenAt("a"))
    }

    @Test
    fun `guarda como maximo 30 usos por etiqueta y conserva los mas recientes`() {
        val store = InMemoryFreshnessStore()
        for (i in 1..40) store.markTagUse(ClinicalCategory.IMPULSO_CONTACTAR, i.toLong())
        val uses = store.tagUses(ClinicalCategory.IMPULSO_CONTACTAR)
        assertEquals(30, uses.size)
        assertEquals(11L, uses.first())
        assertEquals(40L, uses.last())
    }

    @Test
    fun `serializar y leer devuelve el mismo estado`() {
        val s = FreshnessState()
        s.markSeen("legacy_ESTOICO_X_0", 100); s.markSeen("cap_7", 200)
        s.markTagUse("IMPULSO_CONTACTAR", 300); s.markTagUse("IMPULSO_CONTACTAR", 400)
        val r = FreshnessState.parse(s.serialize())
        assertEquals(100L, r.seenAt("legacy_ESTOICO_X_0"))
        assertEquals(200L, r.seenAt("cap_7"))
        assertEquals(listOf(300L, 400L), r.tagUses("IMPULSO_CONTACTAR"))
    }

    @Test
    fun `las lineas corruptas se ignoran sin lanzar excepcion`() {
        val texto = "basura\nS|a|noesnumero\nS|ok|100\nT|IMPULSO_CONTACTAR|xx\nT|IMPULSO_CONTACTAR|200\n||\nS||5\nS|a|b|c\n\u0000\n"
        val s = FreshnessState.parse(texto)
        assertEquals(100L, s.seenAt("ok"))
        assertNull(s.seenAt("a"))
        assertEquals(listOf(200L), s.tagUses("IMPULSO_CONTACTAR"))
        assertEquals(1, s.size())
    }

    @Test
    fun `texto nulo o vacio da un estado vacio`() {
        assertEquals(0, FreshnessState.parse(null).size())
        assertEquals(0, FreshnessState.parse("").size())
    }

    @Test
    fun `un id con barra vertical o salto de linea no rompe la serializacion`() {
        val s = FreshnessState()
        s.markSeen("raro|id\nmalo", 10)
        s.markSeen("normal", 20)
        val r = FreshnessState.parse(s.serialize())
        assertEquals(2, r.size())
        assertEquals(20L, r.seenAt("normal"))
    }

    @Test
    fun `recentIds ordena del mas reciente al mas antiguo y filtra por prefijo`() {
        val s = FreshnessState()
        s.markSeen("wis_1", 10); s.markSeen("cap_9", 20); s.markSeen("wis_2", 30); s.markSeen("wis_3", 5)
        assertEquals(listOf("wis_2", "wis_1"), s.recentIds("wis_", 2))
        assertEquals(listOf("cap_9"), s.recentIds("cap_", 5))
    }

    @Test
    fun `clearAll vacia piezas y usos`() {
        val store = InMemoryFreshnessStore()
        store.markSeen("a", 1); store.markTagUse(ClinicalCategory.RUMIACION_BUCLE, 1)
        store.clearAll()
        assertNull(store.seenAt("a"))
        assertTrue(store.tagUses(ClinicalCategory.RUMIACION_BUCLE).isEmpty())
    }

    @Test
    fun `acceso concurrente no lanza excepciones ni supera el limite`() {
        val s = FreshnessState()
        val threads = (1..8).map { t ->
            Thread { for (i in 1..300) { s.markSeen("t${t}_$i", (t * 1000 + i).toLong()); s.markTagUse("TAG$t", i.toLong()); s.serialize() } }
        }
        threads.forEach { it.start() }; threads.forEach { it.join() }
        assertTrue(s.size() <= 400)
        assertFalse(s.serialize().isEmpty())
    }
}

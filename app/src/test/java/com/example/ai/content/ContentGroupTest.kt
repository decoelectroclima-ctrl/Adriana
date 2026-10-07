package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.ui.screens.QuickReflectionTags
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentGroupTest {

    @Test
    fun `todas las categorias tienen grupo`() {
        val sinGrupo = ClinicalCategory.values().filter { it !in ContentGroup.mapped }
        assertTrue("Sin grupo: $sinGrupo", sinGrupo.isEmpty())
    }

    @Test
    fun `el grupo coincide con el de QuickReflectionTags para las 32 etiquetas`() {
        assertEquals(32, QuickReflectionTags.allTags.size)
        for (tag in QuickReflectionTags.allTags) {
            assertEquals(tag.category.name, tag.group.name, ContentGroup.of(tag.category).name)
        }
    }

    @Test
    fun `franjas horarias y fases temporales`() {
        assertEquals(TimeOfDay.MADRUGADA, TimeOfDay.of(0)); assertEquals(TimeOfDay.MADRUGADA, TimeOfDay.of(5))
        assertEquals(TimeOfDay.MANANA, TimeOfDay.of(6)); assertEquals(TimeOfDay.MANANA, TimeOfDay.of(12))
        assertEquals(TimeOfDay.TARDE, TimeOfDay.of(13)); assertEquals(TimeOfDay.TARDE, TimeOfDay.of(19))
        assertEquals(TimeOfDay.NOCHE, TimeOfDay.of(20)); assertEquals(TimeOfDay.NOCHE, TimeOfDay.of(23))
        assertEquals(TimePhase.PRIMEROS_DIAS, TimePhase.of(0)); assertEquals(TimePhase.PRIMEROS_DIAS, TimePhase.of(3))
        assertEquals(TimePhase.PRIMERA_SEMANA, TimePhase.of(4)); assertEquals(TimePhase.PRIMERA_SEMANA, TimePhase.of(7))
        assertEquals(TimePhase.PRIMER_MES, TimePhase.of(8)); assertEquals(TimePhase.PRIMER_MES, TimePhase.of(30))
        assertEquals(TimePhase.TRAS_UN_MES, TimePhase.of(31)); assertEquals(TimePhase.PRIMEROS_DIAS, TimePhase.of(-2))
    }
}

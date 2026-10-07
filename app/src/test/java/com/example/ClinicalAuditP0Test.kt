package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.ai.ClinicalCategory
import com.example.ai.ClinicalVariantsPsicologia
import com.example.ai.SoltarAiEngine
import com.example.ai.SoltarUserContext
import com.example.data.SoltarFramework
import com.example.ui.SoltarViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ClinicalAuditP0Test {

    private lateinit var application: Application
    private lateinit var viewModel: SoltarViewModel

    @Before
    fun setUp() {
        application = ApplicationProvider.getApplicationContext()
        viewModel = SoltarViewModel(application)
        com.example.ai.ClinicalVariantRegistry.clearMemory()
    }

    @Test
    fun testP0_4_WordBoundaryExDetectionNoFalsePositives() {
        // Words containing "ex" like "experiencia" or "texto" should not trigger false positive
        val exRegex = Regex("""\b(ex|él|el|ella|pareja)\b""", RegexOption.IGNORE_CASE)
        val matchesIsolated = exRegex.containsMatchIn("mi ex me llamó")
        val matchesInsideWord = exRegex.containsMatchIn("excelente texto flexible")
        assertTrue(matchesIsolated)
        assertFalse(matchesInsideWord)
    }

    @Test
    fun testP0_5_ClinicalVariantsConversationalNoBulletPoints() {
        val topCategories = listOf(
            ClinicalCategory.RECUPERAR_PAREJA,
            ClinicalCategory.SENALES_DIGITALES,
            ClinicalCategory.RUMIACION_BUCLE,
            ClinicalCategory.IMPULSO_CONTACTAR,
            ClinicalCategory.DEPENDENCIA_EMOCIONAL,
            ClinicalCategory.NOSTALGIA_IDEALIZACION,
            ClinicalCategory.CULPA_RENCOR_RABIA,
            ClinicalCategory.CONTACTO_CERO_LIMITES
        )

        for (category in topCategories) {
            val variants = ClinicalVariantsPsicologia.getVariants(category)
            assertTrue("Debe tener variantes para $category", variants.isNotEmpty())
            for (v in variants) {
                assertFalse("No debe contener viñetas con • en ${category.name}", v.bodyText.contains("•"))
                assertFalse("No debe contener viñetas numeradas '1.' en ${category.name}", v.bodyText.contains("1. "))
                val wordCount = v.bodyText.trim().split(Regex("\\s+")).size
                assertTrue("La variante en ${category.name} debe tener una longitud conversacional adecuada ($wordCount palabras)", wordCount in 20..100)
            }
        }
    }
}

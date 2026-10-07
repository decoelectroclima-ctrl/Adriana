package com.example.ai.content

import com.example.ai.ClinicalLegacySource

/**
 * Punto único de acceso al motor de tarjetas. El almacén de frescura se sustituye por la versión persistente al
 * arrancar la app (SoltarViewModel); por defecto es en memoria para que los tests no dependan de Android.
 */
object ContentEngine {
    @Volatile
    var store: FreshnessStore = InMemoryFreshnessStore()

    private val composer by lazy { CardComposer(ContentDatabase.atoms, ClinicalLegacySource, ClinicalLegacySource) }

    fun compose(req: ComposeRequest): ComposedCard = composer.compose(req, store)
}

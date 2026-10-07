package com.example.ai.content

import com.example.ai.ClinicalCategory
import com.example.ai.SoltarUserContext
import com.example.data.SoltarFramework
import java.util.Calendar
import kotlin.math.max
import kotlin.random.Random

enum class CardSource { LEGACY, COMPOSED, AI_REWRITTEN, FALLBACK }

/** Tarjeta completa escrita a mano (las 216 variantes clínicas existentes). */
data class LegacyVariant(val id: String, val headline: String, val body: String)

/** Cita y, si la fuente lo trae, una micro-acción de la misma categoría. */
data class QuoteChoice(val id: String, val quote: String, val author: String, val action: String?)

interface LegacySource {
    fun variants(category: ClinicalCategory, framework: SoltarFramework, userContext: SoltarUserContext): List<LegacyVariant>
}

interface QuoteSource {
    fun choose(
        category: ClinicalCategory,
        framework: SoltarFramework,
        store: FreshnessStore,
        exclude: Set<String>,
        random: Random
    ): QuoteChoice
}

data class ComposeRequest(
    val category: ClinicalCategory,
    val framework: SoltarFramework,
    val userContext: SoltarUserContext = SoltarUserContext(),
    val fallbackAction: String = "Respira lento cinco veces y anota cómo te sientes.",
    val excludeIds: Set<String> = emptySet(),
    /** true al elegir una etiqueta; false en "Otra mirada" (no cuenta como un uso nuevo de la etiqueta). */
    val countAsNewUse: Boolean = true,
    /** Recaída registrada en las últimas 72 h, si el llamador puede saberlo. Nunca se infiere. */
    val recentRelapse: Boolean = false
)

data class ComposedCard(
    val source: CardSource,
    val atomIds: List<String>,
    val headline: String,
    val body: String,
    val quote: String,
    val author: String,
    val action: String
)

/** Mínimo de separación entre repeticiones de una misma pieza: min(tamaño del pool - 1, MIN_GAP). */
const val MIN_GAP = 5

/**
 * Elige del pool la pieza menos reciente. Ventana = las (pool - MIN_GAP) más antiguas (mínimo 1), de modo que
 * una pieza no puede volver antes de MIN_GAP selecciones (o pool - 1 si el pool es menor).
 * Dentro de la ventana, las piezas con [weightOf] mayor tienen más probabilidad. Los empates de "nunca vista"
 * se rompen al azar para que dos personas no vean el mismo orden.
 */
fun <T> pickFresh(
    items: List<T>,
    idOf: (T) -> String,
    weightOf: (T) -> Int,
    store: FreshnessStore,
    random: Random
): T? {
    if (items.isEmpty()) return null
    val sorted = items.shuffled(random).sortedBy { store.seenAt(idOf(it)) ?: Long.MIN_VALUE }
    val window = sorted.take(max(1, sorted.size - MIN_GAP))
    val weights = window.map { max(1, weightOf(it)) }
    var roll = random.nextInt(weights.sum())
    for (i in window.indices) {
        roll -= weights[i]
        if (roll < 0) return window[i]
    }
    return window.last()
}

class CardComposer(
    private val atoms: List<ContentAtom>,
    private val legacy: LegacySource,
    private val quotes: QuoteSource,
    private val hourOf: (Long) -> Int = { now -> Calendar.getInstance().apply { timeInMillis = now }.get(Calendar.HOUR_OF_DAY) }
) {
    companion object {
        const val MIN_REENCUADRE_POOL = 3
        const val LEGACY_PROBABILITY = 0.35
        const val LEGACY_QUIET_DAYS = 14L
        private const val DAY_MS = 24L * 60 * 60 * 1000
    }

    fun signals(req: ComposeRequest, store: FreshnessStore, now: Long): ContentSignals {
        val weekAgo = now - 7 * DAY_MS
        val usesThisWeek = store.tagUses(req.category).count { it >= weekAgo }
        return ContentSignals(
            timeOfDay = TimeOfDay.of(hourOf(now)),
            phase = TimePhase.of(req.userContext.streakDays),
            repeated = usesThisWeek >= 3,
            recentRelapse = req.recentRelapse
        )
    }

    fun compose(
        req: ComposeRequest,
        store: FreshnessStore,
        now: Long = System.currentTimeMillis(),
        random: Random = Random.Default
    ): ComposedCard {
        val signals = signals(req, store, now)

        val scoped = atoms.filter { it.appliesTo(req.category, req.framework) && it.id !in req.excludeIds }
        val compatible = scoped.filter { signals.satisfies(it.context) }
        val reencuadres = compatible.filter { it.slot == Slot.REENCUADRE }

        val allLegacy = legacy.variants(req.category, req.framework, req.userContext)
        val legacyPool = allLegacy.filter { it.id !in req.excludeIds }
        val legacyRecentlyShown = allLegacy.any { v ->
            store.seenAt(v.id)?.let { now - it < LEGACY_QUIET_DAYS * DAY_MS } == true
        }
        val composedEligible = reencuadres.size >= MIN_REENCUADRE_POOL

        val card: ComposedCard = when {
            composedEligible && legacyPool.isNotEmpty() && !legacyRecentlyShown && random.nextDouble() < LEGACY_PROBABILITY ->
                legacyCard(req, legacyPool, store, now, random)
            composedEligible ->
                composedCard(req, compatible, signals, store, now, random)
            legacyPool.isNotEmpty() ->
                legacyCard(req, legacyPool, store, now, random)
            reencuadres.isNotEmpty() ->
                composedCard(req, compatible, signals, store, now, random)
            allLegacy.isNotEmpty() ->
                legacyCard(req, allLegacy, store, now, random)
            else ->
                fallbackCard(req, store, now, random)
        }

        if (req.countAsNewUse) store.markTagUse(req.category, now)
        return card
    }

    private fun composedCard(
        req: ComposeRequest,
        compatible: List<ContentAtom>,
        signals: ContentSignals,
        store: FreshnessStore,
        now: Long,
        random: Random
    ): ComposedCard {
        fun pool(slot: Slot) = compatible.filter { it.slot == slot }
        fun weight(a: ContentAtom) = if (a.context.isNeutral) 1 else 3
        fun pick(list: List<ContentAtom>) = pickFresh(list, { it.id }, ::weight, store, random)

        val reencuadre = pick(pool(Slot.REENCUADRE))!!
        val reconocimiento = pick(pool(Slot.RECONOCIMIENTO))

        var cierres = pool(Slot.CIERRE)
        if (signals.repeated) {
            // Escalada alternada: tras una pieza de escalada se vuelve al cierre normal del marco, para no insistir.
            val escalada = cierres.filter { it.context.repeated == true }
            val normales = cierres.filter { it.context.repeated != true }
            if (escalada.isNotEmpty() && normales.isNotEmpty()) {
                val ultimo = cierres.filter { store.seenAt(it.id) != null }.maxByOrNull { store.seenAt(it.id)!! }
                cierres = if (ultimo?.context?.repeated == true) normales else escalada
            } else if (escalada.isNotEmpty()) {
                cierres = escalada
            }
        }
        val cierre = pick(cierres)
        val accion = pick(pool(Slot.ACCION))
        val cita = pick(pool(Slot.CITA))

        val quoteChoice = if (cita == null) quotes.choose(req.category, req.framework, store, req.excludeIds, random) else null

        val author = when {
            cita == null -> quoteChoice!!.author
            cita.source.isNotBlank() -> "${cita.author}, ${cita.source}"
            else -> cita.author
        }

        val ids = listOfNotNull(reconocimiento?.id, reencuadre.id, cierre?.id, accion?.id, cita?.id, quoteChoice?.id)
        ids.forEach { store.markSeen(it, now) }

        return ComposedCard(
            source = CardSource.COMPOSED,
            atomIds = ids,
            headline = reconocimiento?.text.orEmpty(),
            body = listOfNotNull(reencuadre.text, cierre?.text).joinToString(" "),
            quote = cita?.text ?: quoteChoice!!.quote,
            author = author,
            action = accion?.text ?: quoteChoice?.action ?: req.fallbackAction
        )
    }

    private fun legacyCard(
        req: ComposeRequest,
        pool: List<LegacyVariant>,
        store: FreshnessStore,
        now: Long,
        random: Random
    ): ComposedCard {
        val variant = pickFresh(pool, { it.id }, { 1 }, store, random)!!
        val quote = quotes.choose(req.category, req.framework, store, req.excludeIds, random)
        store.markSeen(variant.id, now)
        store.markSeen(quote.id, now)
        return ComposedCard(
            source = CardSource.LEGACY,
            atomIds = listOf(variant.id, quote.id),
            headline = variant.headline,
            body = variant.body,
            quote = quote.quote,
            author = quote.author,
            action = quote.action ?: req.fallbackAction
        )
    }

    /** Solo si no existe ninguna pieza para el par etiqueta/marco. No debería ocurrir; nunca lanza excepción. */
    private fun fallbackCard(req: ComposeRequest, store: FreshnessStore, now: Long, random: Random): ComposedCard {
        val quote = quotes.choose(req.category, req.framework, store, req.excludeIds, random)
        store.markSeen(quote.id, now)
        return ComposedCard(
            source = CardSource.FALLBACK,
            atomIds = listOf(quote.id),
            headline = "",
            body = req.fallbackAction,
            quote = quote.quote,
            author = quote.author,
            action = quote.action ?: req.fallbackAction
        )
    }
}

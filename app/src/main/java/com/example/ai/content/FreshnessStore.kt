package com.example.ai.content

import com.example.ai.ClinicalCategory

/**
 * Memoria de "frescura": qué piezas se mostraron y cuándo, y cuántas veces se eligió cada etiqueta.
 * Todo queda en el dispositivo; nunca se exporta ni se sincroniza.
 */
interface FreshnessStore {
    fun seenAt(id: String): Long?
    fun markSeen(id: String, now: Long)
    /** Ids vistos cuyo identificador empieza por [prefix], del más reciente al más antiguo. */
    fun recentIds(prefix: String, limit: Int): List<String>
    fun tagUses(tag: ClinicalCategory): List<Long>
    fun markTagUse(tag: ClinicalCategory, now: Long)
    fun clearAll()
}

/**
 * Estado puro (sin Android) con política LRU y serialización propia en texto:
 *   S|id|timestamp        pieza vista
 *   T|ETIQUETA|timestamp  uso de una etiqueta
 * Las líneas corruptas se ignoran. Todo el acceso está sincronizado.
 */
class FreshnessState(
    private val maxSeen: Int = MAX_SEEN,
    private val maxTagUses: Int = MAX_TAG_USES
) {
    private val seen = LinkedHashMap<String, Long>()
    private val tags = HashMap<String, ArrayList<Long>>()

    @Synchronized fun seenAt(id: String): Long? = seen[clean(id)]

    @Synchronized fun markSeen(id: String, now: Long) {
        val key = clean(id)
        if (key.isEmpty()) return
        seen.remove(key)
        seen[key] = now
        while (seen.size > maxSeen) {
            val eldest = seen.entries.iterator()
            eldest.next()
            eldest.remove()
        }
    }

    @Synchronized fun recentIds(prefix: String, limit: Int): List<String> =
        seen.entries.filter { it.key.startsWith(prefix) }
            .sortedByDescending { it.value }
            .take(limit)
            .map { it.key }

    @Synchronized fun tagUses(tag: String): List<Long> = tags[tag]?.toList() ?: emptyList()

    @Synchronized fun markTagUse(tag: String, now: Long) {
        val list = tags.getOrPut(tag) { ArrayList() }
        list.add(now)
        list.sort()
        while (list.size > maxTagUses) list.removeAt(0)
    }

    @Synchronized fun size(): Int = seen.size

    @Synchronized fun clear() { seen.clear(); tags.clear() }

    @Synchronized fun serialize(): String {
        val sb = StringBuilder()
        for ((id, ts) in seen) sb.append("S|").append(id).append('|').append(ts).append('\n')
        for ((tag, list) in tags) for (ts in list) sb.append("T|").append(tag).append('|').append(ts).append('\n')
        return sb.toString()
    }

    companion object {
        const val MAX_SEEN = 400
        const val MAX_TAG_USES = 30

        private fun clean(id: String) = id.replace('|', '_').replace('\n', ' ').replace('\r', ' ').trim()

        fun parse(text: String?, maxSeen: Int = MAX_SEEN, maxTagUses: Int = MAX_TAG_USES): FreshnessState {
            val state = FreshnessState(maxSeen, maxTagUses)
            if (text.isNullOrEmpty()) return state
            val seenEntries = ArrayList<Pair<String, Long>>()
            val tagEntries = ArrayList<Pair<String, Long>>()
            for (line in text.split('\n')) {
                val parts = line.split('|')
                if (parts.size != 3) continue
                val ts = parts[2].trim().toLongOrNull() ?: continue
                val name = parts[1].trim()
                if (name.isEmpty()) continue
                when (parts[0]) {
                    "S" -> seenEntries.add(name to ts)
                    "T" -> tagEntries.add(name to ts)
                }
            }
            seenEntries.sortedBy { it.second }.forEach { state.markSeen(it.first, it.second) }
            tagEntries.sortedBy { it.second }.forEach { state.markTagUse(it.first, it.second) }
            return state
        }
    }
}

class InMemoryFreshnessStore(private val state: FreshnessState = FreshnessState()) : FreshnessStore {
    override fun seenAt(id: String) = state.seenAt(id)
    override fun markSeen(id: String, now: Long) = state.markSeen(id, now)
    override fun recentIds(prefix: String, limit: Int) = state.recentIds(prefix, limit)
    override fun tagUses(tag: ClinicalCategory) = state.tagUses(tag.name)
    override fun markTagUse(tag: ClinicalCategory, now: Long) = state.markTagUse(tag.name, now)
    override fun clearAll() = state.clear()
}

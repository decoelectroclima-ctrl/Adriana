package com.example.ai.content

import android.content.Context
import com.example.ai.ClinicalCategory

/** Persistencia en SharedPreferences (un solo archivo, una sola clave). */
class SharedPrefsFreshnessStore(context: Context) : FreshnessStore {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val state: FreshnessState = FreshnessState.parse(runCatching { prefs.getString(KEY, null) }.getOrNull())

    private fun persist() {
        val snapshot = state.serialize()
        prefs.edit().putString(KEY, snapshot).apply()
    }

    override fun seenAt(id: String) = state.seenAt(id)
    override fun markSeen(id: String, now: Long) { state.markSeen(id, now); persist() }
    override fun recentIds(prefix: String, limit: Int) = state.recentIds(prefix, limit)
    override fun tagUses(tag: ClinicalCategory) = state.tagUses(tag.name)
    override fun markTagUse(tag: ClinicalCategory, now: Long) { state.markTagUse(tag.name, now); persist() }
    override fun clearAll() { state.clear(); prefs.edit().clear().apply() }

    private companion object {
        const val PREFS = "freshness_content"
        const val KEY = "state"
    }
}

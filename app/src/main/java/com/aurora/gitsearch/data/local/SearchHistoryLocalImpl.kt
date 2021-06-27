package com.aurora.gitsearch.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import java.lang.reflect.Type

internal class SearchHistoryLocalImpl(
    private val dataStore: DataStore<Preferences>,
    moshi: Moshi = Moshi.Builder().build()
) : SearchHistoryLocal {

    private val stringSetType: Type = Types.newParameterizedType(
        Set::class.java,
        String::class.java
    )
    private val jsonAdapter: JsonAdapter<Set<String>> = moshi.adapter(stringSetType)

    @ExperimentalCoroutinesApi
    private val searchHistoryFlow = dataStore.data.mapLatest { pref ->
        pref[SEARCH_HISTORY_KEY]?.let {
            jsonAdapter.fromJson(it)
        } ?: emptySet()
    }

    @ExperimentalCoroutinesApi
    override fun keys(): Flow<Set<String>> {
        return searchHistoryFlow
    }

    override suspend fun add(key: String) {
        dataStore.edit { pref ->
            val copy = (pref[SEARCH_HISTORY_KEY]?.let { jsonAdapter.fromJson(it) } ?: emptySet())
                .toMutableList()
                .also {
                    it.remove(key)
                }

            if (copy.size == MAX_HISTORY_SIZE) {
                copy.removeLast()
            }

            copy.add(0, key)

            pref[SEARCH_HISTORY_KEY] = jsonAdapter.toJson(copy.toSet())
        }
    }

    companion object {
        private const val MAX_HISTORY_SIZE = 5
        // we're going to use string instead of set and do manual de/serialization
        // due to set in pref does not preserve order
        private val SEARCH_HISTORY_KEY = stringPreferencesKey("searchHistoryKeys")
    }
}

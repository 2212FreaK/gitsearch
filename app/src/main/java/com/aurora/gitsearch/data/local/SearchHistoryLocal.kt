package com.aurora.gitsearch.data.local

import kotlinx.coroutines.flow.Flow

interface SearchHistoryLocal {

    /**
     * Returns the search history keys
     */
    fun keys(): Flow<Set<String>>

    /**
     * Adds the given [key] to the history cache which
     * implements a LRU cache.
     */
    suspend fun add(key: String)
}

package com.aurora.gitsearch.data.repository

import com.aurora.gitsearch.data.model.GitSearchResults
import com.aurora.gitsearch.data.model.Result
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    /**
     * Searches for the given [key] and [page] number and returns
     * git projects
     */
    suspend fun search(key: String, page: Int): Result<GitSearchResults>

    /**
     * Adds the search [key] to the history
     */
    suspend fun addSearchKey(key: String)

    /**
     * Returns the [Flow] of search keys added in [addSearchKey]
     */
    fun searchKeys(): Flow<Set<String>>
}

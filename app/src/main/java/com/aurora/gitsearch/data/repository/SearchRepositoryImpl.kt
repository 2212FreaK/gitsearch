package com.aurora.gitsearch.data.repository

import com.aurora.gitsearch.data.local.SearchHistoryLocal
import com.aurora.gitsearch.data.mapper.toGitSearchResults
import com.aurora.gitsearch.data.model.GitSearchResults
import com.aurora.gitsearch.data.model.Result
import com.aurora.gitsearch.data.remote.GithubClient
import com.aurora.gitsearch.data.repository.tools.APIRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

internal class SearchRepositoryImpl(
    private val local: SearchHistoryLocal,
    private val remote: GithubClient,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SearchRepository, APIRepository(dispatcher) {
    override suspend fun search(key: String, page: Int): Result<GitSearchResults> {
        return safeApiCall { remote.searchRepositories(key = key, page = page).toGitSearchResults() }
    }

    override suspend fun addSearchKey(key: String) {
        local.add(key)
    }

    override fun searchKeys(): Flow<Set<String>> {
        return local.keys()
    }
}

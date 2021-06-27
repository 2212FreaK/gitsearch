package com.aurora.gitsearch.data.remote

import com.aurora.gitsearch.data.dto.GitSearchResultsDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubClient {

    @GET("/search/repositories")
    suspend fun searchRepositories(
        @Query("q") key: String,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): GitSearchResultsDTO
}

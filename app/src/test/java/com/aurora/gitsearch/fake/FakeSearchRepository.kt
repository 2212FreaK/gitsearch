package com.aurora.gitsearch.fake

import com.aurora.gitsearch.data.dto.APIErrorDTO
import com.aurora.gitsearch.data.model.GitProject
import com.aurora.gitsearch.data.model.GitSearchResults
import com.aurora.gitsearch.data.model.Result
import com.aurora.gitsearch.data.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeSearchRepository : SearchRepository {
    override suspend fun search(key: String, page: Int): Result<GitSearchResults> {
        return when (key) {
            MOCKED_SUCCESS_QUERY -> {
                when (page) {
                    1 -> MOCKED_SEARCH_RESULTS_1
                    2 -> MOCKED_SEARCH_RESULTS_2
                    3 -> MOCKED_SEARCH_RESULTS_3
                    else -> MOCKED_EMPTY_RESULTS
                }
            }
            MOCKED_NO_RESULT_QUERY -> MOCKED_EMPTY_RESULTS
            MOCKED_NO_INTERNET_QUERY -> Result.NetworkError
            MOCKED_ERROR_QUERY -> MOCKED_GENERIC_ERROR
            else -> Result.UnknownError
        }
    }

    override suspend fun addSearchKey(key: String) {
        // do nothing
    }

    override fun searchKeys(): Flow<Set<String>> {
        return flow {
            emit(MOCKED_SEARCH_KEYS)
        }
    }

    companion object {
        val MOCKED_SEARCH_KEYS = setOf("history1", "history2", "history3")

        val MOCKED_GIT_PROJECTS_1 = listOf("1".toGitProject(), "2".toGitProject())
        val MOCKED_GIT_PROJECTS_2 = listOf("3".toGitProject(), "5".toGitProject())
        val MOCKED_GIT_PROJECTS_3 = listOf("5".toGitProject())
        val MOCKED_SEARCH_RESULTS_1 = Result.Success(
            GitSearchResults(
                totalCount = 5,
                items = MOCKED_GIT_PROJECTS_1
            )
        )
        val MOCKED_SEARCH_RESULTS_2 = Result.Success(
            GitSearchResults(
                totalCount = 5,
                items = MOCKED_GIT_PROJECTS_2
            )
        )
        val MOCKED_SEARCH_RESULTS_3 = Result.Success(
            GitSearchResults(
                totalCount = 5,
                items = MOCKED_GIT_PROJECTS_3
            )
        )
        val MOCKED_EMPTY_RESULTS = Result.Success(
            GitSearchResults(totalCount = 0, items = emptyList())
        )

        val MOCKED_ERROR = APIErrorDTO(message = "error", errors = emptyList())
        val MOCKED_GENERIC_ERROR = Result.GenericError(error = MOCKED_ERROR)

        const val MOCKED_SUBMITTED_QUERY = "query1"
        const val MOCKED_SUCCESS_QUERY = "successQuery"
        const val MOCKED_NO_RESULT_QUERY = "noResultQuery"
        const val MOCKED_NO_INTERNET_QUERY = "noInternetQuery"
        const val MOCKED_ERROR_QUERY = "errorQuery"

    }
}

internal fun String.toGitProject(): GitProject {
    return GitProject(
        id = this.toLong(),
        name = "name$this",
        description = "description$this",
        htmlUrl = "https://github.com/$this"
    )
}

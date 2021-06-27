package com.aurora.gitsearch.data.model

import com.aurora.gitsearch.data.dto.APIErrorDTO

/**
 * A wrapper for all API call response
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T): Result<T>()
    data class GenericError(val code: Int? = null, val error: APIErrorDTO?): Result<Nothing>()
    object UnknownError: Result<Nothing>()
    object NetworkError: Result<Nothing>()
}

package com.aurora.gitsearch.data.repository.tools

import com.aurora.gitsearch.data.dto.APIErrorDTO
import com.aurora.gitsearch.data.model.Result
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

abstract class APIRepository(protected val dispatcher: CoroutineDispatcher) {
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return withContext(dispatcher) {
            runCatching { Result.Success(apiCall.invoke()) }
                .getOrElse { throwable ->
                    when (throwable) {
                        is IOException -> Result.NetworkError
                        is HttpException -> Result.GenericError(
                            code = throwable.code(),
                            error = throwable.toErrorBody()
                        )
                        else -> Result.UnknownError
                    }
                }

        }
    }

    private fun HttpException.toErrorBody(): APIErrorDTO? {
        return runCatching {
            response()?.errorBody()?.source()?.let {
                val adapter = Moshi.Builder().build().adapter(APIErrorDTO::class.java)
                adapter.fromJson(it)
            }
        }.getOrNull()
    }
}

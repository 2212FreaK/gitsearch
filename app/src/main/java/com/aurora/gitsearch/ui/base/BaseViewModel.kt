package com.aurora.gitsearch.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurora.gitsearch.data.model.Result
import com.aurora.gitsearch.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    private val _genericErrorEvent = SingleLiveEvent<String>()
    val genericErrorEvent: LiveData<String> = _genericErrorEvent

    private val _unknownErrorEvent = SingleLiveEvent<Unit>()
    val unknownErrorEvent: LiveData<Unit> = _unknownErrorEvent

    private val _networkErrorEvent = SingleLiveEvent<Unit>()
    val networkErrorEvent: LiveData<Unit> = _networkErrorEvent

    /**
     * Handles the result and will call one of the error events [_genericErrorEvent],
     * [_unknownErrorEvent], [networkErrorEvent] if not [Result.Success],
     * otherwise do nothing.
     */
    protected fun <T> Result<T>.handleOnError(): Result<T> {
        when (this) {
            is Result.Success -> { /* do nothing/ */ }
            is Result.GenericError -> {
                error?.message?.let {
                    _genericErrorEvent.value = it
                } ?: run {
                    _unknownErrorEvent.value = Unit
                }
            }
            is Result.UnknownError -> {
                _unknownErrorEvent.value = Unit
            }
            is Result.NetworkError -> {
                _networkErrorEvent.value = Unit
            }
        }
        return this
    }

    /**
     * Handles the result and returns the [Result.Success.data] if [Result.Success],
     * otherwise do nothing
     */
    protected inline fun <T> Result<T>.callOnSuccess(callback: (T) -> Unit): Result<T> {
        when (this) {
            is Result.Success -> callback.invoke(data)
            else ->  { /* do nothing/ */ }
        }
        return this
    }

    protected fun ViewModel.launch(
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch { block() }
    }
}

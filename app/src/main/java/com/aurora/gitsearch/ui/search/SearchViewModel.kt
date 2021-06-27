package com.aurora.gitsearch.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurora.gitsearch.data.repository.SearchRepository
import com.aurora.gitsearch.utils.SingleLiveEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _querySubmittedEvent = SingleLiveEvent<String>()
    val querySubmittedEvent: LiveData<String> = _querySubmittedEvent

    private val _searchKeysEvent = SingleLiveEvent<Set<String>>()
    val searchKeysEvent: LiveData<Set<String>> = _searchKeysEvent

    fun loadSearchKeys() {
        searchRepository.searchKeys().onEach {
            _searchKeysEvent.value = it
        }.launchIn(viewModelScope)
    }

    fun onQueryTextSubmit(query: String) {
        viewModelScope.launch {
            searchRepository.addSearchKey(query)
            _querySubmittedEvent.value = query
        }
    }
}

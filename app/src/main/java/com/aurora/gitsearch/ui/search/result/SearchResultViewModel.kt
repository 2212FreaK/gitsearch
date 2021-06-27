package com.aurora.gitsearch.ui.search.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aurora.gitsearch.data.model.GitProject
import com.aurora.gitsearch.data.repository.SearchRepository
import com.aurora.gitsearch.ui.base.BaseViewModel
import com.aurora.gitsearch.utils.SingleLiveEvent

class SearchResultViewModel(
    private val query: String,
    private val searchRepository: SearchRepository
) : BaseViewModel() {

    private val _gitProjectsLiveData = MutableLiveData<List<GitProject>>()
    val gitProjectsLiveData: LiveData<List<GitProject>> = _gitProjectsLiveData

    private val _openUrlEvent = SingleLiveEvent<String>()
    val openUrlEvent: LiveData<String> = _openUrlEvent

    private var isLoading: Boolean = false
    private var page: Int = FIRST_PAGE

    init {
        search()
    }

    private fun search() {
        _gitProjectsLiveData.let { ld ->
            launch {
                isLoading = true
                searchRepository.search(query, page)
                    .also { isLoading = false }
                    .handleOnError()
                    .callOnSuccess { result ->
                        ld.value = (ld.value?.plus(result.items) ?: result.items).also {
                            page = if (it.size >= result.totalCount) {
                                END_PAGE
                            } else {
                                page + 1
                            }
                        }
                    }
            }
        }
    }

    fun loadMore() {
        if (isLoading || page == END_PAGE) return
        search()
    }

    fun onResultItemClicked(data: GitProject) {
        _openUrlEvent.value = data.htmlUrl
    }

    companion object {
        private const val FIRST_PAGE = 1
        private const val END_PAGE = -1
    }

}

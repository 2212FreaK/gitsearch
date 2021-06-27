package com.aurora.gitsearch.ui.extension

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.recyclerview.flingEvents

fun RecyclerView.enablePagination(
    scope: CoroutineScope,
    visibleThreshold: Int = 4,
    loadMoreAction: suspend () -> Unit
) {
    flingEvents()
        .filter { it.velocityY  > 0 }
        .filter {
            val layoutManager = it.view.layoutManager as LinearLayoutManager
            val visibleItemCount = this.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

            totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold
        }
        .onEach { loadMoreAction() }
        .launchIn(scope)
}

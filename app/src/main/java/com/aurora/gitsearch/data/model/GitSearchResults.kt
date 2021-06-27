package com.aurora.gitsearch.data.model

data class GitSearchResults(
    val totalCount: Long,
    val items: List<GitProject>
)

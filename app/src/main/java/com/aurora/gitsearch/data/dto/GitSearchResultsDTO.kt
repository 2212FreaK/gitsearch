package com.aurora.gitsearch.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitSearchResultsDTO(
    @Json(name = "total_count") val totalCount: Long,
    val items: List<GitProjectDTO>
)

package com.aurora.gitsearch.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitProjectDTO(
    val id: Long,
    val name: String,
    val description: String?,
    @Json(name = "html_url") val htmlUrl: String
)

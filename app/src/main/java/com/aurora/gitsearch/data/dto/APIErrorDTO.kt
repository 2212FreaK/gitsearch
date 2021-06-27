package com.aurora.gitsearch.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class APIErrorDTO(
    val message: String,
    val errors: List<ErrorDTO>
)

data class ErrorDTO(
    val resource: String,
    val field: String,
    val code: String
)

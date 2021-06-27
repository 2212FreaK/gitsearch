package com.aurora.gitsearch.data.mapper

import com.aurora.gitsearch.data.dto.GitSearchResultsDTO
import com.aurora.gitsearch.data.dto.GitProjectDTO
import com.aurora.gitsearch.data.model.GitSearchResults
import com.aurora.gitsearch.data.model.GitProject

internal fun GitSearchResultsDTO.toGitSearchResults(): GitSearchResults {
    return GitSearchResults(
        totalCount = totalCount,
        items = items.map { it.toGitProject() }
    )
}

internal fun GitProjectDTO.toGitProject(): GitProject {
    return GitProject(
        id = id,
        name = name,
        description = description.orEmpty(),
        htmlUrl = htmlUrl
    )
}

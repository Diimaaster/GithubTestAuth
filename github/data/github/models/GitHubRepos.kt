package com.kts.github.data.github.models

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class GitHubRepos(
    val name: String,
    val created_at: String,
    val updated_at: String,
    val language: String,

)
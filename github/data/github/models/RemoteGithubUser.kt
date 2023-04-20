package com.kts.github.data.github.models



import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class RemoteGithubUser(
    val id: Long,
    val login: String,
    val public_repos: Int,
    val followers: Int,
    val following: Int,
    val avatar_url: String,
)
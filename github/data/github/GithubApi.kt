package com.kts.github.data.github

import com.kts.github.data.github.models.GitHubRepos
import com.kts.github.data.github.models.RemoteGithubUser
import retrofit2.Response
import retrofit2.http.GET

interface GithubApi {
    @GET("user")
    suspend fun getCurrentUser(
    ): RemoteGithubUser
    @GET("users/Diimaaster/repos")
    suspend fun getRepoUser(
    ): Response<List<GitHubRepos>>
}

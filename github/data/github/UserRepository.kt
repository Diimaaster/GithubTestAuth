package com.kts.github.data.github

import com.kts.github.data.github.models.GitHubRepos
import com.kts.github.data.github.models.RemoteGithubUser
import com.kts.github.data.network.Networking
import retrofit2.Response

class UserRepository {
    suspend fun getUserInformation(): RemoteGithubUser {
        return Networking.githubApi.getCurrentUser()
    }

    suspend fun getUserRepo(): Response<List<GitHubRepos>> {
        return Networking.githubApi.getRepoUser()
    }
}
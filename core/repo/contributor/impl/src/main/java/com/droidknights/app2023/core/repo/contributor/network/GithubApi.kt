package com.droidknights.app2023.core.repo.contributor.network

import com.droidknights.app2023.core.repo.contributor.network.model.ContributorResponse
import retrofit2.http.GET
import retrofit2.http.Path

internal interface GithubApi {
    @GET("repos/{owner}/{name}/contributors")
    suspend fun getContributors(
        @Path("owner") owner: String,
        @Path("name") name: String,
    ): List<ContributorResponse>
}

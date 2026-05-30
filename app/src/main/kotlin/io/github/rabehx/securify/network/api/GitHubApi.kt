package io.github.rabehx.securify.network.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface GitHubApi {

    @GET
    suspend fun getRawContent(@Url url: String): Response<ResponseBody>
}

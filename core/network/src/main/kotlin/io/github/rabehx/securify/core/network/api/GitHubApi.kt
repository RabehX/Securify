package io.github.rabehx.securify.core.network.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface GitHubApi {

    /**
     * Fetch raw document contents (e.g. Terms of Service or Privacy Policy) from a GitHub raw URL.
     */
    @GET
    suspend fun getRawContent(@Url url: String): Response<ResponseBody>
}


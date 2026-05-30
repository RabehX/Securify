package io.github.rabehx.securify.network.api

import io.github.rabehx.securify.network.model.IntegrityResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IntegrityApi {

    /**
     * Submit the integrity token for verification.
     * Server decodes the token and returns the verdict payload or an error field.
     */
    @GET("api/check")
    suspend fun verifyIntegrity(
        @Query("token") token: String
    ): Response<IntegrityResult>
}

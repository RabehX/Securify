package io.github.rabehx.securify.repository

import io.github.rabehx.securify.utils.NetworkResult
import io.github.rabehx.securify.network.api.GitHubApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LegalRepository @Inject constructor(
    private val gitHubApi: GitHubApi,
) {
    fun fetchMarkdown(url: String): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = gitHubApi.getRawContent(url)

            if (response.isSuccessful && response.body() != null) {
                val rawMarkdown = response.body()!!.string()
                emit(NetworkResult.Success(rawMarkdown))
            } else {
                emit(NetworkResult.Error("Failed to fetch: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)
}

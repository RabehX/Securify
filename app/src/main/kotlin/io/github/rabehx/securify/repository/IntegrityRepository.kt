package io.github.rabehx.securify.repository

import io.github.rabehx.securify.integrity.TokenManager
import io.github.rabehx.securify.network.api.IntegrityApi
import io.github.rabehx.securify.network.model.IntegrityResult
import io.github.rabehx.securify.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.Response

/**
 * Repository for handling Play Integrity API operations.
 *
 * 1. Generate nonce
 * 2. Use that nonce to request an integrity token from Play Integrity API
 * 3. Submit the token to the backend for verification
 */
@Singleton
class IntegrityRepository @Inject constructor(
    private val integrityApi: IntegrityApi,
    private val tokenManager: TokenManager,
) {
    companion object {
        private const val NONCE_LENGTH = 50
        private const val ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    }

    private val secureRandom = SecureRandom()

    fun checkPlayIntegrity(): Flow<NetworkResult<IntegrityResult>> = flow {
        emit(NetworkResult.Loading)

        try {
            val nonce = generateLocalNonce()

            val token = tokenManager.getIntegrityToken(nonce)
            val response = integrityApi.verifyIntegrity(token)

            when (val result = response.toNetworkResult()) {
                is NetworkResult.Success -> emit(result)
                is NetworkResult.Error -> emit(result)
                NetworkResult.Loading -> Unit
            }
        } catch (e: Exception) {
            val errorMsg = e.message ?: "Unknown error occurred"
            emit(NetworkResult.Error(errorMsg))
        }
    }.flowOn(Dispatchers.IO)

    private fun Response<IntegrityResult>.toNetworkResult(): NetworkResult<IntegrityResult> {
        val body = body()

        if (!isSuccessful) {
            val details = errorBody()?.string()?.takeIf { it.isNotBlank() } ?: message()
            return NetworkResult.Error("Verification failed: ${code()} - $details")
        }

        if (body == null) {
            return NetworkResult.Error("Verification failed: empty server response")
        }

        body.error?.takeIf { it.isNotBlank() }?.let { error ->
            return NetworkResult.Error(error)
        }

        return NetworkResult.Success(body)
    }

    private fun generateLocalNonce(): String {
        return (1..NONCE_LENGTH)
            .map { ALLOWED_CHARS[secureRandom.nextInt(ALLOWED_CHARS.length)] }
            .joinToString("")
    }
}

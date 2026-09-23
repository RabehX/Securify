package io.github.rabehx.securify.core.network.model

/**
 * Represents the state of a network operation.
 */
sealed interface NetworkResult<out T> {
    data object Loading : NetworkResult<Nothing>
    data class Success<out T>(val data: T) : NetworkResult<T>
    data class Error(val message: String, val cause: Throwable? = null) : NetworkResult<Nothing>
}

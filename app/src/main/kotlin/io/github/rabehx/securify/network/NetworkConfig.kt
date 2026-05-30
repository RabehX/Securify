package io.github.rabehx.securify.network

/**
 * Configuration for the network layer.
 */
interface NetworkConfig {
    val apiUrl: String
    val isDebug: Boolean
}

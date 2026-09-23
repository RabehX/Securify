package io.github.rabehx.securify.core.network

/**
 * Configuration contract for the network layer, fulfilled by application-level modules.
 */
interface NetworkConfig {
    val apiUrl: String
    val isDebug: Boolean
}


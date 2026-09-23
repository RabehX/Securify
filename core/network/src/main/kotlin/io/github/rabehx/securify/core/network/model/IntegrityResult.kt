package io.github.rabehx.securify.core.network.model

import kotlinx.serialization.Serializable

/**
 * Server response from the integrity check endpoint.
 */
@Serializable
data class IntegrityResult(
    val deviceIntegrity: DeviceIntegrity? = null,
    val appIntegrity: AppIntegrity? = null,
    val accountDetails: AccountDetails? = null,
    val environmentDetails: EnvironmentDetails? = null,
    val appAccessRisk: AppAccessRisk? = null,
    val error: String? = null,
) {
    val deviceVerdict: List<String>?
        get() = deviceIntegrity?.deviceRecognitionVerdict

    val appVerdict: String?
        get() = appIntegrity?.appRecognitionVerdict

    val licensingVerdict: String?
        get() = accountDetails?.appLicensingVerdict

    val activityLevel: String?
        get() = deviceIntegrity?.recentDeviceActivity?.deviceActivityLevel

    val playProtectVerdict: String?
        get() = environmentDetails?.playProtectVerdict

    val accessRisk: AppAccessRisk?
        get() = appAccessRisk ?: environmentDetails?.appAccessRisk

    val meetsBasicIntegrity: Boolean
        get() = hasDeviceVerdict(MEETS_BASIC_INTEGRITY)

    val meetsDeviceIntegrity: Boolean
        get() = hasDeviceVerdict(MEETS_DEVICE_INTEGRITY)

    val meetsStrongIntegrity: Boolean
        get() = hasDeviceVerdict(MEETS_STRONG_INTEGRITY)

    val meetsVirtualIntegrity: Boolean
        get() = hasDeviceVerdict(MEETS_VIRTUAL_INTEGRITY)

    private fun hasDeviceVerdict(verdict: String): Boolean =
        deviceIntegrity?.deviceRecognitionVerdict.orEmpty().contains(verdict)

    companion object {
        const val MEETS_BASIC_INTEGRITY = "MEETS_BASIC_INTEGRITY"
        const val MEETS_DEVICE_INTEGRITY = "MEETS_DEVICE_INTEGRITY"
        const val MEETS_STRONG_INTEGRITY = "MEETS_STRONG_INTEGRITY"
        const val MEETS_VIRTUAL_INTEGRITY = "MEETS_VIRTUAL_INTEGRITY"
    }
}

@Serializable
data class AppIntegrity(
    val appRecognitionVerdict: String? = null,
    val packageName: String? = null,
    val certificateSha256Digest: List<String>? = null,
    val versionCode: String? = null,
)

@Serializable
data class DeviceIntegrity(
    val deviceRecognitionVerdict: List<String>? = null,
    val recentDeviceActivity: RecentDeviceActivity? = null,
)

@Serializable
data class RecentDeviceActivity(
    val deviceActivityLevel: String? = null,
)

@Serializable
data class AccountDetails(
    val appLicensingVerdict: String? = null,
)

@Serializable
data class EnvironmentDetails(
    val playProtectVerdict: String? = null,
    val appAccessRisk: AppAccessRisk? = null,
)

/**
 * Modern Play Integrity verdict detecting screen capture, overlays, or remote device controlling.
 */
@Serializable
data class AppAccessRisk(
    val otherAppsCapture: String? = null,
    val otherAppsOverlays: String? = null,
    val otherAppsControlling: String? = null,
)

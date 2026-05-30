package io.github.rabehx.securify.network.model

/**
 * Server response from the integrity check endpoint.
 *
 * Currently matches the raw decoded JWT structure from the server.
 * TODO: Update server to return sanitized IntegrityResult instead,
 *       with only verdicts and no raw JWT data (nonces, timestamps, etc.)
 */
data class IntegrityResult(
    val deviceIntegrity: DeviceIntegrity? = null,
    val appIntegrity: AppIntegrity? = null,
    val accountDetails: AccountDetails? = null,
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

    val meetsBasicIntegrity: Boolean
        get() = hasDeviceVerdict(MEETS_BASIC_INTEGRITY)

    val meetsDeviceIntegrity: Boolean
        get() = hasDeviceVerdict(MEETS_DEVICE_INTEGRITY)

    val meetsStrongIntegrity: Boolean
        get() = hasDeviceVerdict(MEETS_STRONG_INTEGRITY)

    private fun hasDeviceVerdict(verdict: String): Boolean =
        deviceIntegrity?.deviceRecognitionVerdict.orEmpty().contains(verdict)

    companion object {
        const val MEETS_BASIC_INTEGRITY = "MEETS_BASIC_INTEGRITY"
        const val MEETS_DEVICE_INTEGRITY = "MEETS_DEVICE_INTEGRITY"
        const val MEETS_STRONG_INTEGRITY = "MEETS_STRONG_INTEGRITY"
    }
}

data class AppIntegrity(
    val appRecognitionVerdict: String?,
    val packageName: String?,
    val certificateSha256Digest: List<String>?,
    val versionCode: String?
)

data class DeviceIntegrity(
    val deviceRecognitionVerdict: List<String>?,
    val recentDeviceActivity: RecentDeviceActivity?
)

data class RecentDeviceActivity(
    val deviceActivityLevel: String?
)

data class AccountDetails(
    val appLicensingVerdict: String?
)

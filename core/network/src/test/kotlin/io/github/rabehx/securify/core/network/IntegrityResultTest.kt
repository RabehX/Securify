package io.github.rabehx.securify.core.network

import io.github.rabehx.securify.core.network.model.IntegrityResult
import io.github.rabehx.securify.core.network.model.NetworkResult
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class IntegrityResultTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
        encodeDefaults = true
    }

    @Test
    fun deserializesFullPayloadSuccessfully() {
        val jsonString = """
            {
                "appIntegrity": {
                    "appRecognitionVerdict": "PLAY_RECOGNIZED",
                    "packageName": "io.github.rabehx.securify",
                    "certificateSha256Digest": ["abcdef123456"],
                    "versionCode": "20000"
                },
                "deviceIntegrity": {
                    "deviceRecognitionVerdict": [
                        "MEETS_BASIC_INTEGRITY",
                        "MEETS_DEVICE_INTEGRITY",
                        "MEETS_STRONG_INTEGRITY"
                    ],
                    "recentDeviceActivity": {
                        "deviceActivityLevel": "LEVEL_1"
                    }
                },
                "accountDetails": {
                    "appLicensingVerdict": "LICENSED"
                },
                "environmentDetails": {
                    "playProtectVerdict": "NO_ISSUES",
                    "appAccessRisk": {
                        "otherAppsCapture": "NO_RISK",
                        "otherAppsOverlays": "NO_RISK",
                        "otherAppsControlling": "NO_RISK"
                    }
                }
            }
        """.trimIndent()

        val result = json.decodeFromString<IntegrityResult>(jsonString)

        assertEquals("PLAY_RECOGNIZED", result.appVerdict)
        assertEquals("LICENSED", result.licensingVerdict)
        assertEquals("LEVEL_1", result.activityLevel)
        assertEquals("NO_ISSUES", result.playProtectVerdict)
        assertTrue(result.meetsBasicIntegrity)
        assertTrue(result.meetsDeviceIntegrity)
        assertTrue(result.meetsStrongIntegrity)
        assertFalse(result.meetsVirtualIntegrity)
        assertNotNull(result.accessRisk)
        assertEquals("NO_RISK", result.accessRisk?.otherAppsCapture)
    }

    @Test
    fun ignoresUnknownFieldsSafely() {
        val jsonWithUnknownFields = """
            {
                "requestDetails": {
                    "requestPackageName": "io.github.rabehx.securify",
                    "timestampMillis": 1720000000000,
                    "nonce": "testNonce123"
                },
                "deviceIntegrity": {
                    "deviceRecognitionVerdict": ["MEETS_BASIC_INTEGRITY"]
                },
                "futureGoogleField": {
                    "someNewVerdict": "UNKNOWN"
                }
            }
        """.trimIndent()

        val result = json.decodeFromString<IntegrityResult>(jsonWithUnknownFields)

        assertTrue(result.meetsBasicIntegrity)
        assertFalse(result.meetsDeviceIntegrity)
        assertFalse(result.meetsStrongIntegrity)
        assertNull(result.appVerdict)
        assertNull(result.error)
    }

    @Test
    fun handlesErrorPayload() {
        val errorJson = """
            {
                "error": "Failed to decode integrity token"
            }
        """.trimIndent()

        val result = json.decodeFromString<IntegrityResult>(errorJson)

        assertEquals("Failed to decode integrity token", result.error)
        assertFalse(result.meetsBasicIntegrity)
        assertNull(result.deviceVerdict)
    }

    @Test
    fun networkResultStatesWorkCorrectly() {
        val loading: NetworkResult<String> = NetworkResult.Loading
        val success: NetworkResult<String> = NetworkResult.Success("test-data")
        val error: NetworkResult<String> = NetworkResult.Error("Network failure")

        assertTrue(loading is NetworkResult.Loading)
        assertTrue(success is NetworkResult.Success && success.data == "test-data")
        assertTrue(error is NetworkResult.Error && error.message == "Network failure")
    }
}

package io.github.rabehx.securify.repository

import app.cash.turbine.test
import io.github.rabehx.securify.utils.NetworkResult
import io.github.rabehx.securify.network.api.IntegrityApi
import io.github.rabehx.securify.network.model.AccountDetails
import io.github.rabehx.securify.network.model.AppIntegrity
import io.github.rabehx.securify.network.model.DeviceIntegrity
import io.github.rabehx.securify.network.model.IntegrityResult
import io.github.rabehx.securify.network.model.RecentDeviceActivity
import io.github.rabehx.securify.integrity.TokenManager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class IntegrityRepositoryTest {

    private lateinit var integrityApi: IntegrityApi
    private lateinit var tokenManager: TokenManager
    private lateinit var repository: IntegrityRepository

    @Before
    fun setup() {
        integrityApi = mockk()
        tokenManager = mockk()
        repository = IntegrityRepository(integrityApi, tokenManager)
    }

    @Test
    fun `checkPlayIntegrity emits Loading first`() = runTest {
        coEvery { tokenManager.getIntegrityToken(any()) } returns "test-token"
        coEvery { integrityApi.verifyIntegrity(any()) } returns Response.success(integrityResult())

        repository.checkPlayIntegrity().test {
            assertTrue(awaitItem() is NetworkResult.Loading)
            awaitItem() // Success
            awaitComplete()
        }
    }

    @Test
    fun `checkPlayIntegrity emits Success on valid response`() = runTest {
        val expectedResponse = integrityResult()
        coEvery { tokenManager.getIntegrityToken(any()) } returns "test-token"
        coEvery { integrityApi.verifyIntegrity("test-token") } returns Response.success(expectedResponse)

        repository.checkPlayIntegrity().test {
            awaitItem() // Loading
            val result = awaitItem()
            assertTrue(result is NetworkResult.Success)
            assertEquals(expectedResponse, (result as NetworkResult.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `checkPlayIntegrity emits Error on API failure`() = runTest {
        coEvery { tokenManager.getIntegrityToken(any()) } returns "test-token"
        coEvery { integrityApi.verifyIntegrity(any()) } returns Response.error(
            401,
            "Unauthorized".toResponseBody()
        )

        repository.checkPlayIntegrity().test {
            awaitItem() // Loading
            val result = awaitItem()
            assertTrue(result is NetworkResult.Error)
            assertTrue((result as NetworkResult.Error).message.contains("401"))
            awaitComplete()
        }
    }

    @Test
    fun `checkPlayIntegrity emits Error on exception`() = runTest {
        coEvery { tokenManager.getIntegrityToken(any()) } throws RuntimeException("Network error")

        repository.checkPlayIntegrity().test {
            awaitItem()
            val result = awaitItem()
            assertTrue(result is NetworkResult.Error)
            assertEquals("Network error", (result as NetworkResult.Error).message)
            awaitComplete()
        }
    }

    @Test
    fun `checkPlayIntegrity emits Error on null body`() = runTest {
        coEvery { tokenManager.getIntegrityToken(any()) } returns "test-token"
        coEvery { integrityApi.verifyIntegrity(any()) } returns Response.success<IntegrityResult>(null)

        repository.checkPlayIntegrity().test {
            awaitItem()
            val result = awaitItem()
            assertTrue(result is NetworkResult.Error)
            awaitComplete()
        }
    }

    @Test
    fun `checkPlayIntegrity emits Error on server error response`() = runTest {
        coEvery { tokenManager.getIntegrityToken(any()) } returns "test-token"
        coEvery { integrityApi.verifyIntegrity(any()) } returns Response.success(
            IntegrityResult(error = "Google API Error")
        )

        repository.checkPlayIntegrity().test {
            awaitItem()
            val result = awaitItem()

            assertTrue(result is NetworkResult.Error)
            assertEquals("Google API Error", (result as NetworkResult.Error).message)
            awaitComplete()
        }
    }

    private fun integrityResult() = IntegrityResult(
        deviceIntegrity = DeviceIntegrity(
            deviceRecognitionVerdict = listOf(
                IntegrityResult.MEETS_BASIC_INTEGRITY,
                IntegrityResult.MEETS_DEVICE_INTEGRITY,
                IntegrityResult.MEETS_STRONG_INTEGRITY,
            ),
            recentDeviceActivity = RecentDeviceActivity("LEVEL_1"),
        ),
        appIntegrity = AppIntegrity(
            appRecognitionVerdict = "PLAY_RECOGNIZED",
            packageName = "io.github.rabehx.securify",
            certificateSha256Digest = listOf("test-cert"),
            versionCode = "10400",
        ),
        accountDetails = AccountDetails(
            appLicensingVerdict = "LICENSED",
        ),
    )
}

package io.github.rabehx.securify.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import io.github.rabehx.securify.utils.NetworkResult
import io.github.rabehx.securify.network.model.AccountDetails
import io.github.rabehx.securify.network.model.AppIntegrity
import io.github.rabehx.securify.network.model.DeviceIntegrity
import io.github.rabehx.securify.network.model.IntegrityResult
import io.github.rabehx.securify.network.model.RecentDeviceActivity
import io.github.rabehx.securify.repository.IntegrityRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: IntegrityRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is null`() = runTest {
        viewModel = HomeViewModel(repository)

        assertNull(viewModel.integrity.value)
    }

    @Test
    fun `checkPlayIntegrity updates state with Loading`() = runTest {
        val response = integrityResult()
        every { repository.checkPlayIntegrity() } returns flowOf(
            NetworkResult.Loading,
            NetworkResult.Success(response)
        )

        viewModel = HomeViewModel(repository)

        viewModel.integrity.test {
            assertNull(awaitItem())

            viewModel.checkPlayIntegrity()
            testDispatcher.scheduler.advanceUntilIdle()

            val loading = awaitItem()
            assertTrue(loading is NetworkResult.Loading)

            val success = awaitItem()
            assertTrue(success is NetworkResult.Success)
        }
    }

    @Test
    fun `checkPlayIntegrity updates state with Success`() = runTest {
        val expectedResult = integrityResult()
        every { repository.checkPlayIntegrity() } returns flowOf(
            NetworkResult.Loading,
            NetworkResult.Success(expectedResult)
        )

        viewModel = HomeViewModel(repository)
        viewModel.checkPlayIntegrity()
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.integrity.value
        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResult, (result as NetworkResult.Success).data)
    }

    @Test
    fun `checkPlayIntegrity updates state with Error`() = runTest {
        every { repository.checkPlayIntegrity() } returns flowOf(
            NetworkResult.Loading,
            NetworkResult.Error("Test error")
        )

        viewModel = HomeViewModel(repository)
        viewModel.checkPlayIntegrity()
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.integrity.value
        assertTrue(result is NetworkResult.Error)
        assertEquals("Test error", (result as NetworkResult.Error).message)
    }

    @Test
    fun `resetState clears integrity state`() = runTest {
        val response = integrityResult()
        every { repository.checkPlayIntegrity() } returns flowOf(
            NetworkResult.Success(response)
        )

        viewModel = HomeViewModel(repository)
        viewModel.checkPlayIntegrity()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.integrity.value is NetworkResult.Success)

        viewModel.resetState()

        assertNull(viewModel.integrity.value)
    }

    @Test
    fun `checkPlayIntegrity calls repository`() = runTest {
        every { repository.checkPlayIntegrity() } returns flowOf(NetworkResult.Loading)

        viewModel = HomeViewModel(repository)
        viewModel.checkPlayIntegrity()
        testDispatcher.scheduler.advanceUntilIdle()

        verify { repository.checkPlayIntegrity() }
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
        accountDetails = AccountDetails("LICENSED"),
    )
}
